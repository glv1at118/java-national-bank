package com.national.service;

import java.util.Calendar;
import java.util.List;

import com.national.dao.JavaBankDaoImpl;
import com.national.entry.AppEntry;
import com.national.model.Account;
import com.national.model.InfoList;
import com.national.model.User;

public class Service {
	private JavaBankDaoImpl daoObj;

	public Service() {
		this.daoObj = new JavaBankDaoImpl();
	}

	// 这个方法，从外部把Dao注入到Service实例中，替换Service中的daoObj。
	// 方便JUnit中Mockito通过假的Dao来调用Dao方法。
	public void setDaoField(JavaBankDaoImpl injectedDao) {
		this.daoObj = injectedDao;
	}

	public boolean createUser(String userName, String passWord, String userType) {
		// userName: all letters only, 5 <= String length <= 15
		// passWord: letters and/or numbers, 5 <= String length <= 15
		// userType: "customer", "employee", "admin"

		String user_name = userName.trim().toLowerCase();
		String pass_word = passWord.trim().toLowerCase();
		String user_type = userType.trim().toLowerCase();

		if (!user_name.matches("[a-z]{5,15}")) {
			System.out.println("User name should be all letters, length between 5 to 15.");
			return false;
		}
		if (!pass_word.matches("[a-z0-9]{5,15}")) {
			System.out.println("Password should be letters and/or numbers, length between 5 to 15.");
			return false;
		}
		if (!userType.equals("customer") && !userType.equals("employee") && !userType.equals("admin")) {
			System.out.println("User type should be one of these: customer, employee, admin.");
			return false;
		}

		if (this.daoObj.checkUserExists(userName)) {
			System.out.println("The user name already exists in the system, duplicate user names are not allowed.");
			return false;
		}

		this.daoObj.insertUser(user_name, passWord, user_type);
		AppEntry.log
				.info("A new user has been created, the user name is: " + userName + ", the user type is: " + userType);
		return true;
	}

	public boolean loginUser(String userName, String passWord) {
		String user_name = userName.trim().toLowerCase();
		String pass_word = passWord.trim().toLowerCase();

		if (!user_name.matches("[a-z]{5,15}")) {
			return false;
		}
		if (!pass_word.matches("[a-z0-9]{5,15}")) {
			return false;
		}

		boolean flag = this.daoObj.findUser(userName, passWord);
		if (flag) {
			return true;
		}
		return false;
	}

	public String acquireUserType(String userName, String passWord) {
		String type = this.daoObj.findUserType(userName, passWord);
		return type;
	}

	public void depositMoney(long number, String moneyAmount) {
		String regex = "(0\\.[0-9]{1,2})|([1-9][0-9]*\\.[0-9]{1,2})|([1-9][0-9]*)";
		if (!moneyAmount.matches(regex)) {
			System.out.println(
					"Input format is not allowed. Only positive integer or positive decimal with at least 1 decimal digit and at most 2 decimal digit is allowed.");
			return;
		}
		double amount = Double.parseDouble(moneyAmount);
		int accountIdToDeposit = daoObj.findAccountId(number);
		if (accountIdToDeposit == -1) {
			System.out.println("Deposit failure: No such account number exists.");
			return;
		}
		String accountState = daoObj.getAccountState(number);
		if (accountState.equals("frozen") || accountState.equals("pending") || accountState.equals("")) {
			System.out.println("Deposit failure: Account exists but it does not have an active state.");
			return;
		}

		if (AppEntry.currUserType.equals("customer")) {
			int desAccountId = this.daoObj.findAccountId(number);
			int currUserId = this.daoObj.findUserId(AppEntry.currUserName, AppEntry.currPassWord);
			boolean matched = this.daoObj.checkRecordInJunction(currUserId, desAccountId);
			if (!matched) {
				System.out.println(
						"Deposit failure: Customer type user is not allowed to deposit money to an account not owned.");
				return;
			}
		}

		daoObj.increaseAccountBalance(number, amount);
		System.out.println("Deposit success: $" + amount + " has been deposited to account number: " + number);
		AppEntry.log.info("$" + amount + " has been deposited to account number: " + number);
	}

	public boolean retrieveMoney(long number, String moneyAmount) { // zan shi void
		String regex = "(0\\.[0-9]{1,2})|([1-9][0-9]*\\.[0-9]{1,2})|([1-9][0-9]*)";
		if (!moneyAmount.matches(regex)) {
			System.out.println(
					"Input format is not allowed. Only positive integer or positive decimal with at least 1 decimal digit and at most 2 decimal digit is allowed.");
			return false;
		}

		int accountId = daoObj.findAccountId(number);
		int currUserId = daoObj.findUserId(AppEntry.currUserName, AppEntry.currPassWord);
		boolean hasRecord = daoObj.checkRecordInJunction(currUserId, accountId);
		if (!hasRecord) {
			System.out.println("Withdrawl failure: User does not have the account, or such account does not exist.");
			return false;
		}

		String accountState = daoObj.getAccountState(number);
		if (accountState.equals("frozen") || accountState.equals("pending") || accountState.equals("")) {
			System.out.println("Withdrawl failure: Account exists but it does not have an active state.");
			return false;
		}

		double balance = daoObj.getAccountBalance(number);
		if (balance < Double.parseDouble(moneyAmount)) {
			System.out.println("Withdrawl failure: Overdrafting is not allowed.");
			return false;
		}
		daoObj.decreaseAccountBalance(number, Double.parseDouble(moneyAmount));
		System.out.println("Withdrawl success: $" + moneyAmount + " has been withdrawn from account: " + number);
		AppEntry.log.info("$" + moneyAmount + " has been withdrawn from account: " + number);
		return true;

	}

	public void transferMoneyBetweenAccounts(long srcNumber, long desNumber, String moneyAmount) {
		int srcAccId = this.daoObj.findAccountId(srcNumber);
		int desAccId = this.daoObj.findAccountId(desNumber);
		if (srcAccId == -1 || desAccId == -1) {
			System.out.println("Transfer failed: Source or destination account is non-existent. Did you made a typo?");
			return;
		}

		if (srcNumber == desNumber) {
			System.out.println(
					"You're trying to transfer money to the same account number, which is redundant. Operation cancelled.");
			return;
		}

		String stateSrc = this.daoObj.getAccountState(srcNumber);
		String stateDes = this.daoObj.getAccountState(desNumber);

		if (!stateSrc.equals("active") || !stateDes.equals("active")) {
			System.out.println("Transfer failure: Pending and frozen accounts are not allowed in transfer.");
			return;
		}
		if (this.retrieveMoney(srcNumber, moneyAmount)) {
			this.depositMoney(desNumber, moneyAmount);
			System.out.println("Transfer success: $" + moneyAmount + " has been transfered.");
			AppEntry.log.info(
					"$" + moneyAmount + " has been transfered from account " + srcNumber + " to account " + desNumber);
		} else {
			System.out.println("Transfer failed due to retrieval failure with source account.");
		}
	}

	public void createAccount() {
		long number = Calendar.getInstance().getTimeInMillis();
		double balance = 0.0;
		String state = "pending";
		this.daoObj.insertAccount(number, balance, state);

		// when a customer creates an account, bind this account with this customer in
		// junction table
		int user_id = this.daoObj.findUserId(AppEntry.currUserName, AppEntry.currPassWord);
		int account_id = this.daoObj.findAccountId(number);
		this.daoObj.insertJunction(user_id, account_id);

		System.out.println("New account is created, account number: " + number);
		AppEntry.log.info("Customer " + AppEntry.currUserName + " has created a new bank account: " + number);
	}

	public void addExistingAccount(String partnerUserName, String partnerPassWord, long partnerAccountNumber) {
		String user_name = partnerUserName.trim().toLowerCase();
		String pass_word = partnerPassWord.trim().toLowerCase();

		if (user_name.equals(AppEntry.currUserName)) {
			System.out.println("Joint failure: You cannot make a joint account with yourself.");
			return;
		}

		if (!user_name.matches("[a-z]{5,15}")) {
			System.out.println("User name should be all letters, length between 5 to 15.");
			return;
		}
		if (!pass_word.matches("[a-z0-9]{5,15}")) {
			System.out.println("Password should be letters and/or numbers, length between 5 to 15.");
			return;
		}

		// 根据目标名和密码，找到userid。根据目标账号，找到账号id。然后在juntion里查，是否有这条记录。有，则验证通过，添加。
		int partnerId = this.daoObj.findUserId(partnerUserName, partnerPassWord);
		int partnerAccountId = this.daoObj.findAccountId(partnerAccountNumber);

		boolean exist = this.daoObj.checkRecordInJunction(partnerId, partnerAccountId);
		if (!exist) {
			System.out.println("Joint account add failure: Information mismatches with system record.");
			return;
		}

		int currUserId = this.daoObj.findUserId(AppEntry.currUserName, AppEntry.currPassWord);
		this.daoObj.insertJunction(currUserId, partnerAccountId);
		System.out.println("Joint account add success: You now have a joint account with " + partnerUserName);
		AppEntry.log.info("Customer " + AppEntry.currUserName + " has added partner " + partnerUserName + "'s account "
				+ partnerAccountNumber + " as a joint bank account.");
		return;
	}

	public void seeOwnedAccount() {
		int user_id = this.daoObj.findUserId(AppEntry.currUserName, AppEntry.currPassWord);
		List<Account> ownedAccounts = this.daoObj.getAllAccountsWithUserId(user_id);
		System.out.println(AppEntry.currUserName + " owns the following account(s):");
		for (int i = 0; i < ownedAccounts.size(); i++) {
			System.out.println(ownedAccounts.get(i).toString());
		}
	}

	public void seeAllCustomers() {
		List<User> userList = this.daoObj.getAllCustomers();
		for (int i = 0; i < userList.size(); i++) {
			User currUser = userList.get(i);
			System.out.println(currUser.toStringWithoutPw());
		}
	}

	public void seeAllAccounts() {
		List<Account> accountList = this.daoObj.getAllAccounts();
		for (int i = 0; i < accountList.size(); i++) {
			Account currAccount = accountList.get(i);
			System.out.println(currAccount.toString());
		}
	}

	public void seeAllAccountsWithPending() {
		List<Account> accountList = this.daoObj.getAllAccountsWithPending();
		for (int i = 0; i < accountList.size(); i++) {
			Account currAccount = accountList.get(i);
			System.out.println(currAccount.toString());
		}
	}

	public void seeAllAccountsWithFrozen() {
		List<Account> accountList = this.daoObj.getAllAccountsWithFrozen();
		for (int i = 0; i < accountList.size(); i++) {
			Account currAccount = accountList.get(i);
			System.out.println(currAccount.toString());
		}
	}

	public void approveOneAccount(long number) {
		int accountId = daoObj.findAccountId(number);
		if (accountId == -1) {
			System.out.println("No such account number exists.");
			return;
		}
		this.daoObj.approveAccount(number);
		System.out.println("Account " + number + " has been set to active.");
		AppEntry.log.info(AppEntry.currUserName + " has approved account " + number);
	}

	public void approveManyAccount() {
		List<Account> accountList = this.daoObj.getAllAccountsWithPending();
		for (int i = 0; i < accountList.size(); i++) {
			long currNum = accountList.get(i).getNumber();
			this.daoObj.approveAccount(currNum);
			System.out.println("Account " + currNum + " has been set to active.");
		}
		System.out.println("All pending accounts are approved.");
		AppEntry.log.info(AppEntry.currUserName + " has approved all accounts with a pending state.");
	}

	public void denyOneAccount(long number) {
		int accountId = daoObj.findAccountId(number);
		if (accountId == -1) {
			System.out.println("No such account number exists.");
			return;
		}
		this.daoObj.denyAccount(number);
		System.out.println("Account " + number + " has been set to frozen.");
		AppEntry.log.info(AppEntry.currUserName + " has denied account " + number);
	}

	public void denyManyAccount() {
		List<Account> accountList = this.daoObj.getAllAccountsWithPending();
		for (int i = 0; i < accountList.size(); i++) {
			long currNum = accountList.get(i).getNumber();
			this.daoObj.denyAccount(currNum);
			System.out.println("Account " + currNum + " has been set to frozen.");
		}
		System.out.println("All pending accounts are denied.");
		AppEntry.log.info(AppEntry.currUserName + " has denied all accounts with a pending state.");
	}

	public void superSee() {
		List<InfoList> list = this.daoObj.getComprehensiveListWithoutPw();
		for (int i = 0; i < list.size(); i++) {
			InfoList info = list.get(i);
			System.out.println(info.toBriefString());
		}
		AppEntry.log.warn("Employee " + AppEntry.currUserName + " used the employee's super see mode.");
	}

	public void superSeeAdmin() {
		List<InfoList> list = this.daoObj.getComprehensiveList();
		for (int i = 0; i < list.size(); i++) {
			InfoList info = list.get(i);
			System.out.println(info.toString());
		}
		AppEntry.log.warn("Admin " + AppEntry.currUserName + " used the admin's super see mode.");
	}

	public void seeBankStaff() {
		List<User> list = this.daoObj.getAllEmployeeAndAdmin();
		for (int i = 0; i < list.size(); i++) {
			User info = list.get(i);
			System.out.println(info.toString());
		}
	}

	public boolean retrieveMoneyAdmin(long number, String moneyAmount) {
		String regex = "(0\\.[0-9]{1,2})|([1-9][0-9]*\\.[0-9]{1,2})|([1-9][0-9]*)";
		if (!moneyAmount.matches(regex)) {
			System.out.println(
					"Input format is not allowed. Only positive integer or positive decimal with at least 1 decimal digit and at most 2 decimal digit is allowed.");
			return false;
		}

		if (daoObj.findAccountId(number) == -1) {
			System.out.println("Withdrawl failure: Such account does not exist.");
			return false;
		}

		String accountState = daoObj.getAccountState(number);
		if (accountState.equals("frozen") || accountState.equals("pending") || accountState.equals("")) {
			System.out.println("Withdrawl failure: Account exists but it does not have an active state.");
			return false;
		}

		double balance = daoObj.getAccountBalance(number);
		if (balance < Double.parseDouble(moneyAmount)) {
			System.out.println("Withdrawl failure: Overdrafting is not allowed.");
			return false;
		}
		daoObj.decreaseAccountBalance(number, Double.parseDouble(moneyAmount));
		System.out.println("Withdrawl success: $" + moneyAmount + " has been withdrawn from account: " + number);
		AppEntry.log.info(AppEntry.currUserName + " retrieved " + moneyAmount + " from account " + number);
		return true;

	}

	public void transferMoneyBetweenAccountsAdmin(long srcNumber, long desNumber, String moneyAmount) {
		int srcAccId = this.daoObj.findAccountId(srcNumber);
		int desAccId = this.daoObj.findAccountId(desNumber);
		if (srcAccId == -1 || desAccId == -1) {
			System.out.println("Transfer failed: Source or destination account is non-existent. Did you made a typo?");
			return;
		}

		if (srcNumber == desNumber) {
			System.out.println(
					"You're trying to transfer money to the same account number, which is redundant. Operation cancelled.");
			return;
		}

		String stateSrc = this.daoObj.getAccountState(srcNumber);
		String stateDes = this.daoObj.getAccountState(desNumber);
		if (!stateSrc.equals("active") || !stateDes.equals("active")) {
			System.out.println("Transfer failure: Pending and frozen accounts are not allowed in transfer.");
			return;
		}
		if (this.retrieveMoneyAdmin(srcNumber, moneyAmount)) {
			this.depositMoney(desNumber, moneyAmount);
			System.out.println("Transfer success: $" + moneyAmount + " has been transfered.");
			AppEntry.log.info(AppEntry.currUserName + " transfered " + moneyAmount + " from account " + srcNumber
					+ " to " + desNumber);
		} else {
			System.out.println("Transfer failed due to retrieval failure with source account.");
		}
	}

	public void deleteAccountAdmin(long number) {
		if (this.daoObj.findAccountId(number) == -1) {
			System.out.println("No such account number exists in system records. Nothing is deleted.");
			return;
		}
		double balance = this.daoObj.getAccountBalance(number);
		if (balance > 0) {
			System.out.println("Account deletion failed: Accounts with balance > 0 cannot be deleted.");
			return;
		}
		this.daoObj.deleteAccount(number);
		System.out.println("Account deletion success: Account " + number + " has been deleted.");
		AppEntry.log.warn(AppEntry.currUserName + " has deleted the bank account: " + number);
	}

}
