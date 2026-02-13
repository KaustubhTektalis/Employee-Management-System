package controller;

import java.io.File;
import java.sql.Connection;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.CrudDBImplementation;
import dao.CrudFileImplementation;
import enums.ManagerChoices;
import service.LoginAndAccess;
import service.PasswordMethods;
import service.Read;
import service.Update;
import util.SaveEmployeesToFile;

public class ManagerMenu {
	private static final Logger logger = LoggerFactory.getLogger(ManagerMenu.class);
	private ManagerMenu() {}
	public static void showMenu(CrudFileImplementation ops, Scanner sc, ObjectMapper mapper, File file) {

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
					Update.handleUpdateMenu(ops, sc, mapper, file);
					break;

				case SHOW_ALL:
					Read.handleReadAll(ops).forEach(System.out::println);
					break;

				case MY_DETAILS:
					System.out.println(Read.handleReadOne(ops,LoginAndAccess.getLoggedInId()));
					break;

				case CHANGE_PASSWORD:
					PasswordMethods.updatePassword(ops, sc);
					SaveEmployeesToFile.saveToJson( mapper, file);
					break;

				case EXIT:
					SaveEmployeesToFile.saveToJson( mapper, file);
					System.out.println("Manager logged out.");
					logger.info("Manager {} logged out", LoginAndAccess.getLoggedInId());
					LoginAndAccess.clearLoginContext();
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
	
	
	public static void showDBMenu(CrudDBImplementation dbops, Scanner sc, Connection conn) {

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
					Update.handleUpdateMenuDB(dbops, sc, conn);
					break;

				case SHOW_ALL:
					Read.handleReadAllDB(dbops, conn).forEach(System.out::println);
					break;

				case MY_DETAILS:
					System.out.println(Read.handleReadOneDB(dbops, conn, LoginAndAccess.getLoggedInId()));
					break;

				case CHANGE_PASSWORD:
					PasswordMethods.updatePasswordDB(dbops, sc);
					break;

				case EXIT:
					conn=null;
					System.out.println("Manager logged out.");
					logger.info("Manager {} logged out", LoginAndAccess.getLoggedInId());
					LoginAndAccess.clearLoginContext();
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
