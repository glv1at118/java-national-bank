package com.national.menu;

import java.util.Scanner;
import com.national.entry.AppEntry;
import com.national.service.Service;

public class CustomerMenu {
	public static void render() {

		Service service = new Service();
		Scanner scan = new Scanner(System.in);
		System.out.println("\n---------------> Customer Menu <---------------");
		System.out.println("\nHello " + AppEntry.currUserName + ", welcome to Java National Bank online.");

		System.out.println("\nPlease enter one of the following commands:");
		System.out.println(
				"\n* List Owned Accounts --> list\n* Create New Account --> create\n* Withdraw From An Account --> withdraw\n* Deposit To An Account --> deposit\n* Transfer Between Accounts --> transfer\n* Add Partner's Account As Joint --> joint\n* Logout and return To Main Menu --> return");

		String input = scan.nextLine().trim().toLowerCase();
		if ("list".equals(input)) {
			service.seeOwnedAccount();
			render();
		} else if ("create".equals(input)) {
			service.createAccount();
			render();
		} else if ("withdraw".equals(input)) {
			System.out.println("\nPlease enter your account number:");
			String inputNum = scan.nextLine().trim();
			boolean flag = inputNum.matches("[0-9]{13}");
			if (!flag) {
				System.out.println("Account number must be a pure 13-digits number.");
				render();
			} else {
				System.out.println("\nPlease enter the amount you want to withdraw:");
				String inputAmount = scan.nextLine().trim();
				service.retrieveMoney(Long.parseLong(inputNum), inputAmount);
				render();
			}
		} else if ("deposit".equals(input)) {
			System.out.println("\nPlease enter your account number:");
			String inputNum = scan.nextLine().trim();
			boolean flag = inputNum.matches("[0-9]{13}");
			if (!flag) {
				System.out.println("Account number must be a pure 13-digits number.");
				render();
			} else {
				System.out.println("\nPlease enter the amount you want to deposit:");
				String inputAmount = scan.nextLine().trim();
				service.depositMoney(Long.parseLong(inputNum), inputAmount);
				render();
			}
		} else if ("transfer".equals(input)) {
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
					System.out.println("\nPlease enter the amount you want to transfer:");
					String inputAmount = scan.nextLine().trim();
					long inputNumSrcParsed = Long.parseLong(inputNumSrc);
					long inputNumDesParsed = Long.parseLong(inputNumDes);
					service.transferMoneyBetweenAccounts(inputNumSrcParsed, inputNumDesParsed, inputAmount);
					render();
				}
			}
		} else if ("joint".equals(input)) {
			System.out.println("\nPlease enter partner's account number:");
			String numberEntered = scan.nextLine().trim();
			boolean flag = numberEntered.matches("[0-9]{13}");
			if (!flag) {
				System.out.println("Account number must be a pure 13-digits number.");
				render();
			} else {
				System.out.println("\nPlease enter partner's user name:");
				String nameEntered = scan.nextLine().trim();
				System.out.println("\nPlease enter partner's password:");
				String pwEntered = scan.nextLine().trim();
				service.addExistingAccount(nameEntered, pwEntered, Long.parseLong(numberEntered));
				render();
			}
		} else if ("return".equals(input)) {
			LandingMenu.render(); // from here it jumps to the main menu
		} else {
			System.out.println("\nUnrecognized command, please try again.");
			render();
		}

		scan.close();
	}
}
