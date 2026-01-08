package service;

import java.util.Scanner;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import model.Employee;
import util.PasswordHasher;

public class AccessAndPasswordMethods {

	private static String loggedInID = null;

	public static boolean acc(Manage ops, String empID, String pass) {

		String hashedPass = PasswordHasher.hash(pass);

		for (Employee e : ops.getEmployees()) {
			if (e.getId().equals(empID) && e.getPassword().equals(hashedPass)) {

				loggedInID = empID;
				return true;
			}
		}
		return false;
	}

	private static void validateId(String id) throws IdFormatWrongException {
		if (id == null || !id.matches("^TT\\d{5}$")) {
			throw new IdFormatWrongException("Invalid employee ID format");
		}
	}

	public static void updatePassword(Manage ops, Scanner sc) throws EmployeeNotFoundException {

		System.out.print("Enter new password: ");
		String p1 = sc.nextLine().trim();

		System.out.print("Re-enter new password: ");
		String p2 = sc.nextLine().trim();

		if (!p1.equals(p2)) {
			System.out.println("Passwords do not match.");
			return;
		}

		ops.updatePassword(loggedInID, p1);
	}

	public static void resetPassword(Manage ops, Scanner sc) throws EmployeeNotFoundException, IdFormatWrongException {

		String defaultPass = Manage.defaultPassword();

		System.out.println("Enter 1 to Reset Your Password");
		System.out.println("Enter 2 to Reset someone else's password");
		System.out.print("Your choice: ");

		int choice = sc.nextInt();
		sc.nextLine();

		switch (choice) {
		case 1 -> {
			ops.updatePassword(loggedInID, defaultPass);
			System.out.println("The Default password is 'Default123'");
		}
		case 2 -> {
			System.out.print("ID of the employee: ");
			String selectedID = sc.nextLine();
			validateId(selectedID);

			ops.updatePassword(selectedID, defaultPass);
			System.out.println("The Default password is 'Default123'");
		}
		default -> System.out.println("Invalid choice");
		}
	}

	public static String getLoggedInAdminId() {
		return loggedInID;
	}
}
