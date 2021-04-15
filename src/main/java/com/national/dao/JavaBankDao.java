package com.national.dao;

import java.util.List;
import com.national.model.Account;
import com.national.model.InfoList;
import com.national.model.User;

public interface JavaBankDao {

	public abstract boolean checkUserExists(String name);
	
	public abstract boolean findUser(String name, String password);

	public abstract String findUserType(String name, String password);

	public abstract int findUserId(String name, String password);

	public abstract void insertUser(String name, String password, String type);

	public abstract void insertAccount(long number, double balance, String state);

	public abstract void deleteAccount(long number);

	public abstract int findAccountId(long number);

	public abstract void insertJunction(int user_id, int account_id);

	public abstract void increaseAccountBalance(long number, double increment);

	public abstract void decreaseAccountBalance(long number, double decrement);

	public abstract void approveAccount(long number);

	public abstract void denyAccount(long number);

	public abstract List<User> getAllCustomers();

	public abstract List<Account> getAllAccounts();

	public abstract List<InfoList> getComprehensiveList();

	public abstract List<Account> getAllAccountsWithPending();

	public abstract List<Account> getAllAccountsWithFrozen();

	public abstract List<Account> getAllAccountsWithUserId(int user_id);

	public abstract double getAccountBalance(long number);

	public abstract boolean checkRecordInJunction(int user_id, int account_id);

	public abstract String getAccountState(long number);

	public abstract List<InfoList> getComprehensiveListWithoutPw();
	
	public abstract List<User> getAllEmployeeAndAdmin();
}
