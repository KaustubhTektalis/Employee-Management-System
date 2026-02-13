package service;

import java.security.SecureRandom;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.mindrot.jbcrypt.BCrypt;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import customExceptions.InvalidDataException;
import dao.CrudDBImplementation;
import dao.CrudFileImplementation;
import util.ValidPassword;
import util.ValidateId;

public class PasswordMethods {
	private static final Logger logger = LoggerFactory.getLogger(PasswordMethods.class);

	public static String hash(String plainPassword) {
		if (plainPassword == null) {
			throw new IllegalArgumentException("Password cannot be null");
		}
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
	}

	public static void updatePassword(CrudFileImplementation ops, Scanner sc)
			throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
		while (true) {
			System.out.print("Enter new password: ");
			String p1 = sc.nextLine().trim();

			if (!ValidPassword.isValidPassword(p1)) {
				System.out.println(
						"Password must contain atleast one uppercase letter, one number and one special character");
				continue;
			}

			System.out.print("Re-enter new password: ");
			String p2 = sc.nextLine().trim();

			if (!p1.equals(p2)) {
				System.out.println("Passwords do not match. Try again.");
				logger.warn("Passwords do not match. Try again.");
			} else {
				ops.updatePassword(LoginAndAccess.getLoggedInId(), p1);
				System.out.println("Password updated successfully!");
				logger.info("Password updated successfully!");
				break;
			}
		}
	}

	public static void resetPassword(CrudFileImplementation ops, Scanner sc)
			throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
//		ensureLoggedIn();

		System.out.println("Enter 1 to Reset Your Password");
		System.out.println("Enter 2 to Reset someone else's password");
		System.out.print("Your choice: ");

		int choice = sc.nextInt();
		sc.nextLine();

		switch (choice) {
		case 1 -> {
			String newPass = randomPasswordGenerator();
			ops.updatePassword(LoginAndAccess.getLoggedInId(), newPass);
			System.out.println("Your new password is: " + newPass);
			logger.info("Password of employee {} updated", LoginAndAccess.getLoggedInId());
		}
		case 2 -> {
			System.out.print("ID of the employee to reset password: ");
			String selectedID = sc.nextLine();
			ValidateId.validateId(selectedID);
			String newPass = randomPasswordGenerator();
			ops.updatePassword(selectedID, newPass);
			System.out.println("The new password for employee " + selectedID + "is: " + newPass);
			logger.info("Password of employee {} updated", selectedID);
		}
		default -> logger.warn("Invalid choice");
		}
	}

	public static void updatePasswordDB(CrudDBImplementation dbops, Scanner sc) {
//		ensureLoggedIn();

		while (true) {
			System.out.print("Enter new password: ");
			String p1 = sc.nextLine().trim();

			if (!ValidPassword.isValidPassword(p1)) {
				System.out.println(
						"Password must contain atleast one uppercase letter, one number and one special character");
				continue;
//				throw new InvalidDataException("Password must contain atleast one uppercase letter, one number and one special character");	
			}
			System.out.print("Re-enter new password: ");
			String p2 = sc.nextLine().trim();
			if (!p1.equals(p2)) {
				System.out.println("Passwords do not match. Try again.");
				logger.warn("Passwords do not match. Try again.");
			} else {
				dbops.updatePasswordDB(LoginAndAccess.getLoggedInId(), p1);
				System.out.println("Password updated successfully!");
				logger.info("Password updated successfully!");
				break;
			}
		}
	}

	public static void resetPasswordDB(CrudDBImplementation dbops, Scanner sc)
			throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
//		ensureLoggedIn();

		System.out.println("Enter 1 to Reset Your Password");
		System.out.println("Enter 2 to Reset someone else's password");
		System.out.print("Your choice: ");

		int choice = sc.nextInt();
		sc.nextLine();

		switch (choice) {
		case 1 -> {
			String newPass = randomPasswordGenerator();
			dbops.updatePasswordDB(LoginAndAccess.getLoggedInId(), newPass);
			System.out.println("Your new password is: " + newPass);
			logger.info("Password of employee {} updated", LoginAndAccess.getLoggedInId());
		}
		case 2 -> {
			System.out.print("ID of the employee: ");
			String selectedID = sc.nextLine();
			ValidateId.validateId(selectedID);
			String newPass = randomPasswordGenerator();
			dbops.updatePasswordDB(selectedID, newPass);
			System.out.println("The new password for employee " + selectedID + "is: " + newPass);
			logger.info("Password of employee {} updated", selectedID);
		}
		default -> logger.warn("Invalid choice");
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
}