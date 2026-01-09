package controller;

import java.util.Scanner;


import dao.CrudOps;
import enums.EmployeeChoices;
import service.PasswordMethods;
import service.Update;

public class EmployeeMenu {
	private EmployeeMenu() {}
	public static void showMenu(CrudOps ops, Scanner sc) {

		EmployeeChoices choice = null;

		do {
			System.out.println("\n--- EMPLOYEE MENU ---");
			for (EmployeeChoices c : EmployeeChoices.values()) {
				System.out.println(c);
			}
			System.out.print("Your choice: ");

			try {
				choice = EmployeeChoices.valueOf(sc.nextLine().trim().toUpperCase());
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid choice.");
				continue;
			}

			try {
				switch (choice) {

				case UPDATE:
					Update.handleUpdateForEmployee(ops, sc);
					break;

				case SHOW_SELF:
					System.out.println(ops.showOne(PasswordMethods.getLoggedInId()));
					break;

				case CHANGE_PASSWORD:
					PasswordMethods.updatePassword(ops, sc);
					break;

				case EXIT:
					System.out.println("Employee logged out.");
					break;

				default:
					break;
				}

			} catch (Exception e) {
				System.out.println("Operation failed: " + e.getMessage());
			}

		} while (choice != EmployeeChoices.EXIT);
	}
}
