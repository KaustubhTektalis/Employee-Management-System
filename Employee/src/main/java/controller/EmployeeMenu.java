package controller;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

import dao.CrudDBImplementation;
import dao.CrudFileImplementation;
import enums.EmployeeChoices;
import service.LoginAndAccess;
import service.PasswordMethods;
import service.Read;
import service.Update;
import util.SaveEmployeesToFile;

public class EmployeeMenu {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeMenu.class);

	private EmployeeMenu() {
	}

	public static void showMenu(CrudFileImplementation ops, Scanner sc, ObjectMapper mapper, File file) {

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
					Update.handleUpdateMenuForEmployee(ops, sc, mapper, file);
					break;

				case MY_DETAILS:
					System.out.println(Read.handleReadOne(ops, LoginAndAccess.getLoggedInId()));
					break;

				case CHANGE_PASSWORD:
					PasswordMethods.updatePassword(ops, sc);
					break;

				case EXIT:
					SaveEmployeesToFile.saveToJson(mapper, file);
					System.out.println("Employee logged out.");
					logger.info("Employee {}  logged out.", LoginAndAccess.getLoggedInId());
					LoginAndAccess.clearLoginContext();
					break;

				default:
					break;
				}

			} catch (Exception e) {
				System.out.println("Operation failed: " + e.getMessage());
			}

		} while (choice != EmployeeChoices.EXIT);
	}

	public static void showDBMenu(CrudDBImplementation dbops, Scanner sc) {

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
					Update.handleUpdateMenuForEmployeeDB(dbops, sc);
					break;

				case MY_DETAILS:
					System.out.println(Read.handleReadOneDB(dbops, LoginAndAccess.getLoggedInId()));
					break;

				case CHANGE_PASSWORD:
					PasswordMethods.updatePasswordDB(dbops, sc);
					break;

				case EXIT:
					System.out.println("Employee logged out.");
					logger.info("Employee {} logged out.", LoginAndAccess.getLoggedInId());
					LoginAndAccess.clearLoginContext();
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
