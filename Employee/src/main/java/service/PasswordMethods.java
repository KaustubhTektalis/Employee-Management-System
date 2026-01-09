package service;

import java.util.Scanner;
import java.security.SecureRandom;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import customExceptions.InvalidDataException;
import dao.CrudOps;
import util.ValidateId;

public class PasswordMethods {

	private static String loggedInID = null;

	static void setLoggedInID(String empID) {
		loggedInID = empID;
	}

	public static String getLoggedInId() {
		return loggedInID;
	}

	public static void updatePassword(CrudOps ops, Scanner sc)
			throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {

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

	public static void resetPassword(CrudOps ops, Scanner sc)
			throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {

		String defaultPass = randomPasswordGenerator();

		System.out.println("Enter 1 to Reset Your Password");
		System.out.println("Enter 2 to Reset someone else's password");
		System.out.print("Your choice: ");

		int choice = sc.nextInt();
		sc.nextLine();

		switch (choice) {
		case 1 -> {
			ops.updatePassword(loggedInID, defaultPass);
			System.out.println("The new password is: " + randomPasswordGenerator());
		}
		case 2 -> {
			System.out.print("ID of the employee: ");
			String selectedID = sc.nextLine();
			ValidateId.validateId(selectedID);

			ops.updatePassword(selectedID, defaultPass);
			System.out.println("The new password is: " + randomPasswordGenerator());
		}
		default -> System.out.println("Invalid choice");
		}
	}


	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String DIGITS = "0123456789";
	private static final String SPECIAL = "!@#$%^&*()-_=+";
	private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;
	private static final SecureRandom random = new SecureRandom();

	public static String randomPasswordGenerator() {
		int maxPassLength=12;
		
		StringBuilder password = new StringBuilder(maxPassLength);
		password.append(UPPER.charAt(random.nextInt(UPPER.length())));
		password.append(LOWER.charAt(random.nextInt(LOWER.length())));
		password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
		password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
		for (int i = 4; i < maxPassLength; i++) {
			password.append(ALL.charAt(random.nextInt(ALL.length())));
		}
		char[] pwArray = password.toString().toCharArray();
		for (int i = pwArray.length - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			char temp = pwArray[i];
			pwArray[i] = pwArray[j];
			pwArray[j] = temp;
		}

		return new String(pwArray);
	}
}
