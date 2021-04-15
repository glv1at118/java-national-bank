package com.national.menu;

import java.util.Scanner;
import com.national.entry.AppEntry;
import com.national.service.Service;

public class LandingMenu {

	public static void render() {

		Service service = new Service();
		Scanner scan = new Scanner(System.in);

		System.out.println("\n---------------> Main Menu <---------------");
		System.out.println(
				"\n* Login In As An Existing User --> login\n* Register As A New User --> register\n* Exit The Application --> exit");

		String input = scan.nextLine().trim().toLowerCase();

		if (input.equals("login")) {
			System.out.println("Please enter your user name (5-15 alphabet characters):");
			String enteredName = scan.nextLine().trim().toLowerCase();
			System.out.println("Please enter your password (5-15 alphabet & number characters):");
			String enteredPassWord = scan.nextLine().trim().toLowerCase();

			boolean flag = service.loginUser(enteredName, enteredPassWord);
			if (!flag) {
				System.out.println("User name and password does not match system record.");
				render();
			} else {
				String type = service.acquireUserType(enteredName, enteredPassWord);
				AppEntry.setCredential(enteredName, enteredPassWord, type);
				AppEntry.log.info("User " + enteredName + " has logged in as " + AppEntry.currUserType);

				if (AppEntry.currUserType.equals("customer")) {
					CustomerMenu.render(); // from here it jumps to the customer menu
				} else if (AppEntry.currUserType.equals("employee")) {
					EmployeeMenu.render(); // from here it jumps to the employee menu
				} else if (AppEntry.currUserType.equals("admin")) {
					AdminMenu.render(); // from here it jumps to the admin menu
				}
			}
		} else if (input.equals("register")) {
			System.out.println("Please enter a new user name (5-15 alphabet characters):");
			String newName = scan.nextLine().trim().toLowerCase();
			System.out.println("Please enter a new password (5-15 alphabet & number characters):");
			String newPassWord = scan.nextLine().trim().toLowerCase();
			System.out.println("Please enter the user type (customer/employee/admin):");
			String newUserType = scan.nextLine().trim().toLowerCase();

			boolean flag = service.createUser(newName, newPassWord, newUserType);

			if (!flag) {
				System.out.println("User creation failed, please try again.");
				render();
			} else {
				System.out.println("User creation success, please login.");
				render();
			}

		} else if (input.equals("exit")) {
			scan.close(); // 只可以在方法结束前close scanner，跳转其他Menu不可以close scanner!!!
			return;
		} else {
			System.out.println("Unknown command detected, please try again.");
			render();
		}
		scan.close(); // 只可以在方法结束前close scanner，跳转其他Menu不可以close scanner!!!

	}
}
