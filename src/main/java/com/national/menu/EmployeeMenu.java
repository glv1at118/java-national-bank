package com.national.menu;

import java.util.Scanner;
import com.national.service.Service;

public class EmployeeMenu {
	public static void render() {
		System.out.println("\n---------------> Employee Menu <---------------");
		Service service = new Service();

		System.out.println("\nPlease enter one of the following commands:");
		System.out.println(
				"\n* Customer List --> list -c\n* Account List --> list -a\n* Pending Account List --> list -a -p | list -p -a\n* Frozen Account List --> list -a -f | list -f -a\n* Super See --> list -super\n* Approve One Pending Account --> approve\n* Approve All Pending Accounts --> approve -all\n* Deny One Pending Account --> deny\n* Deny All Pending Accounts --> deny -all\n* Log Out And Return To Main Menu --> return");

		Scanner scan = new Scanner(System.in);
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
		} else if ("list -super".equals(str)) {
			service.superSee();
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
		} else if ("return".equals(str)) {
			LandingMenu.render(); // from here it jumps to the main menu
		} else {
			System.out.println("\nUnrecognized command, please try again.");
			render();
		}
		scan.close();
	}
}
