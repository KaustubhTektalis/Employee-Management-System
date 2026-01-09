package controller;

import java.io.File;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CrudOps;
import dao.SaveEmployeesToFile;
import enums.AdminChoices;
import service.Create;
import service.Delete;
import service.PasswordMethods;
import service.Update;

public class AdminMenu {
	private AdminMenu() {}

	public static void showMenu(CrudOps ops, Scanner sc, ObjectMapper mapper, File file) {

		AdminChoices choice = null;

		do {
			System.out.println("\n--- ADMIN MENU ---");
			for (AdminChoices c : AdminChoices.values()) {
				System.out.println(c);
			}
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
					Update.handleUpdate(ops, sc, mapper, file);
					break;

				case DELETE:
					Delete.handleDelete(ops, sc, mapper, file);
					break;

				case SHOW_ALL:
					ops.showAll().forEach(System.out::println);
					break;

				case SELF_DETAILS:
					System.out.println(ops.showOne(PasswordMethods.getLoggedInId()));
					break;

				case CHANGE_PASSWORD:
					PasswordMethods.updatePassword(ops, sc);
					SaveEmployeesToFile.saveToJson(ops, mapper, file);
					break;

				case RESET_PASSWORD:
					PasswordMethods.resetPassword(ops, sc);
					SaveEmployeesToFile.saveToJson(ops, mapper, file);
					break;

				case EXIT:
					SaveEmployeesToFile.saveToJson(ops, mapper, file);
					System.out.println("Admin logged out.");
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