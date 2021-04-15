package com.national.menu;

import java.util.Scanner;

import com.national.service.Service;

public class AdminMenu {
	public static void render() {
		System.out.println("\n---------------> Admin Menu <---------------");
		Scanner scan = new Scanner(System.in);
		Service service = new Service();

		System.out.println("\nPlease enter one of the following commands:");
		System.out.println(
				"\n* Customer List --> list -c\n* Account List --> list -a\n* Pending Account List --> list -a -p | list -p -a\n* Frozen Account List --> list -a -f | list -f -a\n* Bank Staff List --> list -staff\n* Super See --> list -super\n* Approve One Pending Account --> approve\n* Approve All Pending Accounts --> approve -all\n* Deny One Pending Account --> deny\n* Deny All Pending Accounts --> deny -all\n* Deposit --> deposit\n* Withdraw --> withdraw\n* Transfer --> transfer\n* Delete Account --> delete\n* Log Out And Return To Main Menu --> return");

		String str = scan.nextLine().trim().toLowerCase();

		if ("list -c".equals(str)) {
			service.seeAllCustomers();
			render();
		} else if ("list -a".equals(str)) {
			service.seeAllAccounts();
			render();
		} else if ("list -a -p".equals(str) || "list -p -a".equals(str)) {
			service.seeAllAccountsWithPending();
			render();
		} else if ("list -a -f".equals(str) || "list -f -a".equals(str)) {
			service.seeAllAccountsWithFrozen();
			render();
		} else if ("list -staff".equals(str)) {
			service.seeBankStaff();
			render();
		} else if ("list -super".equals(str)) {
			service.superSeeAdmin();
			render();
		} else if ("approve".equals(str)) {
			System.out.println("Please enter the account number to set active:");
			String input = scan.nextLine();
			boolean flag = input.matches("[0-9]{13}");
			if (!flag) {
				System.out.println("Account number must be a pure 13-digits number.");
				render();
			} else {
				service.approveOneAccount(Long.parseLong(input));
				render();
			}
		} else if ("approve -all".equals(str)) {
			service.approveManyAccount();
			render();
		} else if ("deny".equals(str)) {
			System.out.println("Please enter the account number to set frozen:");
			String input = scan.nextLine();
			boolean flag = input.matches("[0-9]{13}");
			if (!flag) {
				System.out.println("Account number must be a pure 13-digits number.");
				render();
			} else {
				service.denyOneAccount(Long.parseLong(input));
				render();
			}
		} else if ("deny -all".equals(str)) {
			service.denyManyAccount();
			render();
		} else if ("deposit".equals(str)) {
			System.out.println("\nPlease enter the account number to deposit:");
			String inputNum = scan.nextLine().trim();
			boolean flag = inputNum.matches("[0-9]{13}");
			if (!flag) {
				System.out.println("Account number must be a pure 13-digits number.");
				render();
			} else {
				System.out.println("\nPlease enter the amount to deposit:");
				String inputAmount = scan.nextLine().trim();
				service.depositMoney(Long.parseLong(inputNum), inputAmount);
				render();
			}
		} else if ("withdraw".equals(str)) {
			System.out.println("\nPlease enter the account number to withdraw from:");
			String inputNum = scan.nextLine().trim();
			boolean flag = inputNum.matches("[0-9]{13}");
			if (!flag) {
				System.out.println("Account number must be a pure 13-digits number.");
				render();
			} else {
				System.out.println("\nPlease enter the amount to withdraw:");
				String inputAmount = scan.nextLine().trim();
				service.retrieveMoneyAdmin(Long.parseLong(inputNum), inputAmount);
				render();
			}
		} else if ("transfer".equals(str)) {
			System.out.println("\nPlease enter account number to transfer from:");
			String inputNumSrc = scan.nextLine().trim();
			boolean flag = inputNumSrc.matches("[0-9]{13}");
			if (!flag) {
				System.out.println("Account number must be a pure 13-digits number.");
				render();
			} else {
				System.out.println("\nPlease enter account number to transfer to:");
				String inputNumDes = scan.nextLine().trim();
				boolean mark = inputNumDes.matches("[0-9]{13}");
				if (!mark) {
					System.out.println("Account number must be a pure 13-digits number.");
					render();
				} else {
					System.out.println("\nPlease enter the amount to transfer:");
					String inputAmount = scan.nextLine().trim();
					long inputNumSrcParsed = Long.parseLong(inputNumSrc);
					long inputNumDesParsed = Long.parseLong(inputNumDes);
					service.transferMoneyBetweenAccountsAdmin(inputNumSrcParsed, inputNumDesParsed, inputAmount);
					render();
				}
			}
		} else if ("delete".equals(str)) {
			System.out.println("\nPlease enter account number to delete:");
			String inputNum = scan.nextLine().trim();
			boolean flag = inputNum.matches("[0-9]{13}");
			if (!flag) {
				System.out.println("Account number must be a pure 13-digits number.");
				render();
			} else {
				service.deleteAccountAdmin(Long.parseLong(inputNum));
				render();
			}
		} else if ("return".equals(str)) {
			LandingMenu.render(); // from here it jumps to the main menu
		} else {
			System.out.println("\nUnrecognized command, please try again.");
			render();
		}
		scan.close();

	}
}
