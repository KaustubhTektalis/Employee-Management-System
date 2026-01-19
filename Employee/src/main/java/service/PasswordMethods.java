package service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import customExceptions.InvalidDataException;
import dao.CrudImplementation;
import util.ValidateId;

public class PasswordMethods {

	private static String loggedInID = null;
	private static List<String> loggedInRoles = new ArrayList<>();

	private PasswordMethods() {
	}

	public static void setLoginContext(String empID, List<String> roles) {
		loggedInID = empID;
		loggedInRoles.clear();
		if (roles != null) {
			loggedInRoles.addAll(roles);
		}
	}

	public static String getLoggedInId() {
		return loggedInID;
	}

	public static List<String> getLoggedInRoles() {
		return Collections.unmodifiableList(loggedInRoles);
	}

	public static boolean hasRole(String role) {
		return loggedInRoles.stream().anyMatch(r -> r.equalsIgnoreCase(role));
	}

	public static void clearLoginContext() {
		loggedInID = null;
		loggedInRoles.clear();
	}

	public static String hash(String plainPassword) {
		if (plainPassword == null) {
			throw new IllegalArgumentException("Password cannot be null");
		}
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
	}

	public static void updatePassword(CrudImplementation ops, Scanner sc)
			throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {

		ensureLoggedIn();

		while (true) {
			System.out.print("Enter new password: ");
			String p1 = sc.nextLine().trim();

			System.out.print("Re-enter new password: ");
			String p2 = sc.nextLine().trim();

			if (!p1.equals(p2)) {
				System.out.println("Passwords do not match. Try again.");
			} else {
				ops.updatePassword(loggedInID, p1);
				System.out.println("Password updated successfully!");
				break;
			}
		}
	}

	public static void resetPassword(CrudImplementation ops, Scanner sc)
			throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {

		ensureLoggedIn();

		System.out.println("Enter 1 to Reset Your Password");
		System.out.println("Enter 2 to Reset someone else's password");
		System.out.print("Your choice: ");

		int choice = sc.nextInt();
		sc.nextLine();

		switch (choice) {
		case 1 -> {
			String newPass = randomPasswordGenerator();
			ops.updatePassword(loggedInID, newPass);
			System.out.println("Your new password is: " + newPass);
		}
		case 2 -> {
			System.out.print("ID of the employee: ");
			String selectedID = sc.nextLine();
			ValidateId.validateId(selectedID);

			String newPass = randomPasswordGenerator();
			ops.updatePassword(selectedID, newPass);
			System.out.println("The new password is: " + newPass);
		}
		default -> System.out.println("Invalid choice");
		}
	}

	public static void updatePasswordDB(CrudImplementation ops, Scanner sc) {

		ensureLoggedIn();

		while (true) {
			System.out.print("Enter new password: ");
			String p1 = sc.nextLine().trim();

			System.out.print("Re-enter new password: ");
			String p2 = sc.nextLine().trim();

			if (!p1.equals(p2)) {
				System.out.println("Passwords do not match. Try again.");
			} else {
				ops.updatePasswordDB(loggedInID, p1);
				System.out.println("Password updated successfully!");
				break;
			}
		}
	}

	public static void resetPasswordDB(CrudImplementation ops, Scanner sc)
			throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {

		ensureLoggedIn();

		System.out.println("Enter 1 to Reset Your Password");
		System.out.println("Enter 2 to Reset someone else's password");
		System.out.print("Your choice: ");

		int choice = sc.nextInt();
		sc.nextLine();

		switch (choice) {
		case 1 -> {
			String newPass = randomPasswordGenerator();
			ops.updatePasswordDB(loggedInID, newPass);
			System.out.println("Your new password is: " + newPass);
		}
		case 2 -> {
			System.out.print("ID of the employee: ");
			String selectedID = sc.nextLine();
			ValidateId.validateId(selectedID);

			String newPass = randomPasswordGenerator();
			ops.updatePasswordDB(selectedID, newPass);
			System.out.println("The new password is: " + newPass);
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
		int length = 12;
		StringBuilder password = new StringBuilder(length);

		password.append(UPPER.charAt(random.nextInt(UPPER.length())));
		password.append(LOWER.charAt(random.nextInt(LOWER.length())));
		password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
		password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

		for (int i = 4; i < length; i++) {
			password.append(ALL.charAt(random.nextInt(ALL.length())));
		}

		char[] chars = password.toString().toCharArray();
		for (int i = chars.length - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			char temp = chars[i];
			chars[i] = chars[j];
			chars[j] = temp;
		}

		return new String(chars);
	}

	private static void ensureLoggedIn() {
		if (loggedInID == null) {
			throw new IllegalStateException("No user is logged in");
		}
	}
}