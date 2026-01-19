package controller;

import java.io.File;
import java.sql.Connection;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.CrudImplementation;
import dao.SaveEmployeesToFile;
import enums.ManagerChoices;
import service.PasswordMethods;
import service.Update;

public class ManagerMenu {
	private ManagerMenu() {}
	public static void showMenu(CrudImplementation ops, Scanner sc, ObjectMapper mapper, File file) {

		ManagerChoices choice = null;

		do {
			System.out.println("\n--- MANAGER MENU ---");
			for (ManagerChoices c : ManagerChoices.values()) {
				System.out.println(c);
			}
			System.out.print("Your choice: ");

			try {
				choice = ManagerChoices.valueOf(sc.nextLine().trim().toUpperCase());
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid choice.");
				continue;
			}

			try {
				switch (choice) {

				case UPDATE:
					Update.handleUpdate(ops, sc, mapper, file);
					break;

				case SHOW_ALL:
					ops.showAll().forEach(System.out::println);
					break;

				case SELF_DETAILS:
//					System.out.println(ops.showOne(PasswordMethods.getLoggedInId()));
					System.out.println(ops.showSelf(PasswordMethods.getLoggedInId()));
					break;

				case CHANGE_PASSWORD:
					PasswordMethods.updatePassword(ops, sc);
					SaveEmployeesToFile.saveToJson( mapper, file);
					break;

				case EXIT:
					SaveEmployeesToFile.saveToJson( mapper, file);
					System.out.println("Manager logged out.");
					break;

				default:
					break;
				}

			} catch (Exception e) {
				System.out.println("Operation failed: " + e.getMessage());
			}

		} while (choice != ManagerChoices.EXIT);
	}
	
	
//	------------------------------------------------------------------------------------------------
	
	
	public static void showDBMenu(CrudImplementation ops, Scanner sc, Connection conn) {

		ManagerChoices choice = null;

		do {
			System.out.println("\n--- MANAGER MENU ---");
			for (ManagerChoices c : ManagerChoices.values()) {
				System.out.println(c);
			}
			System.out.print("Your choice: ");

			try {
				choice = ManagerChoices.valueOf(sc.nextLine().trim().toUpperCase());
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid choice.");
				continue;
			}

			try {
				switch (choice) {

				case UPDATE:
//					String id = PasswordMethods.getLoggedInId();
					Update.handleUpdateDB(ops, sc, conn);
					break;

				case SHOW_ALL:
					ops.showAll().forEach(System.out::println);
					break;

				case SELF_DETAILS:
//					System.out.println(ops.showOne(PasswordMethods.getLoggedInId()));
					System.out.println(ops.showSelf(PasswordMethods.getLoggedInId()));
					break;

				case CHANGE_PASSWORD:
					PasswordMethods.updatePassword(ops, sc);
					break;

				case EXIT:
					System.out.println("Manager logged out.");
					break;

				default:
					break;
				}

			} catch (Exception e) {
				System.out.println("Operation failed: " + e.getMessage());
			}

		} while (choice != ManagerChoices.EXIT);
	}
}
