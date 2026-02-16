package controller;

import java.io.File;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.CrudDBImplementation;
import dao.CrudFileImplementation;
import enums.AdminChoices;
import service.Create;
import service.Delete;
import service.LoginAndAccess;
import service.PasswordMethods;
import service.Read;
import service.Update;
import util.SaveEmployeesToFile;

public class AdminMenu {

	private static final Logger logger = LoggerFactory.getLogger(AdminMenu.class);

	private AdminMenu() {
	}

	public static void showMenu(CrudFileImplementation ops, Scanner sc, ObjectMapper mapper, File file) {

		AdminChoices choice = null;

		do {
			System.out.println("\n--- ADMIN MENU ---");
			System.out.println("ADD");
			System.out.println("UPDATE");
			System.out.println("DELETE");
			System.out.println("SHOW_ALL");
			System.out.println("MY_DETAILS");
			System.out.println("CHANGE_PASSWORD");
			System.out.println("RESET_PASSWORD");
			System.out.println("EXIT");

			System.out.print("Your choice: ");

			try {
				choice = AdminChoices.valueOf(sc.nextLine().trim().toUpperCase());
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid choice.");
				continue;
			}

			try {
				switch (choice) {

				case ADD:
					Create.handleAdd(ops, sc, mapper, file);
					break;

				case UPDATE:
					Update.handleUpdateMenu(ops, sc, mapper, file);
					break;

				case DELETE:
					Delete.handleDelete(ops, sc, mapper, file);
					break;

				case SHOW_ALL:
					Read.handleReadAll(ops).forEach(System.out::println);
					break;

				case MY_DETAILS:
					System.out.println(Read.handleReadOne(ops, LoginAndAccess.getLoggedInId()));
					break;

				case CHANGE_PASSWORD:
					PasswordMethods.updatePassword(ops, sc);
					SaveEmployeesToFile.saveToJson(mapper, file);
					break;

				case RESET_PASSWORD:
					PasswordMethods.resetPassword(ops, sc);
					SaveEmployeesToFile.saveToJson(mapper, file);
					break;

				case EXIT:
					SaveEmployeesToFile.saveToJson(mapper, file);
					System.out.println("Admin logged out.");
					logger.info("Admin {} logged out.", LoginAndAccess.getLoggedInId());
					LoginAndAccess.clearLoginContext();
					break;

				default:
					break;
				}

			} catch (Exception e) {
				System.out.println("Operation failed: " + e.getMessage());
			}

		} while (choice != AdminChoices.EXIT);
	}

//	---------------------------------------------------------------------------------------------------------

	public static void showDBMenu(CrudDBImplementation dbops, Scanner sc) {

		AdminChoices choice = null;

		do {
			System.out.println("\n--- ADMIN MENU ---");
			System.out.println("ADD");
			System.out.println("UPDATE");
			System.out.println("DELETE");
			System.out.println("SHOW_ALL");
			System.out.println("MY_DETAILS");
			System.out.println("INACTIVE_USERS");
			System.out.println("CHANGE_PASSWORD");
			System.out.println("RESET_PASSWORD");
			System.out.println("EXIT");
			System.out.print("Your choice: ");

			try {
				choice = AdminChoices.valueOf(sc.nextLine().trim().toUpperCase());
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid choice.");
				continue;
			}

			try {
				switch (choice) {

				case ADD:
					Create.handleAddDB(dbops, sc);
					break;

				case UPDATE:
					Update.handleUpdateMenuDB(dbops, sc);
					break;

				case DELETE:
					Delete.handleDeleteDB(dbops, sc);
					break;

				case SHOW_ALL:
//					ops.showAll().forEach(System.out::println);
					Read.handleReadAllDB(dbops).forEach(System.out::println);
					break;

				case MY_DETAILS:
//					System.out.println(ops.showSelf(PasswordMethods.getLoggedInId()));
					System.out.println(Read.handleReadOneDB(dbops, LoginAndAccess.getLoggedInId()));
					break;

				case INACTIVE_USERS:
					Read.handleReadInactiveDB(dbops).forEach(System.out::println);
					;
					break;

				case CHANGE_PASSWORD:
					PasswordMethods.updatePasswordDB(dbops, sc);
					break;

				case RESET_PASSWORD:
					PasswordMethods.resetPasswordDB(dbops, sc);
					break;

				case EXIT:
					System.out.println("Admin logged out.");
					logger.info("Admin {} logged out.", LoginAndAccess.getLoggedInId());
					LoginAndAccess.clearLoginContext();
					break;

				default:
					break;
				}

			} catch (Exception e) {
				System.out.println("Operation failed: " + e.getMessage());
			}

		} while (choice != AdminChoices.EXIT);
	}

}