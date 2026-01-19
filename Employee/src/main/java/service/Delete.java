package service;

import java.io.File;

import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import dao.CrudImplementation;
import dao.SaveEmployeesToFile;

public class Delete {
	public static void handleDelete(CrudImplementation ops, Scanner sc, ObjectMapper mapper, File file)
			throws EmployeeNotFoundException, IdFormatWrongException {

		System.out.print("Enter employee ID to delete: ");
		String id = sc.nextLine();

		System.out.print("Are you sure you want to delete Employee " + id + "? (yes/no): ");
		String confirm = sc.nextLine().trim().toLowerCase();

		if (!confirm.equals("yes")) {
			System.out.println("Deletion cancelled.");
			return;
		}
		ops.delete(id);
		SaveEmployeesToFile.saveToJson(mapper, file);
		System.out.println("Employee " + id + " has been deleted successfully.");
	}

	public static void handleDeleteDB(CrudImplementation ops, Scanner sc, Connection conn) {
		try {
			System.out.print("Enter the Employee ID to delete: ");
			String id = sc.nextLine();

			System.out.print("Are you sure you want to delete Employee " + id + "? (yes/no): ");
			String confirm = sc.nextLine().trim().toLowerCase();

			if (!confirm.equals("yes")) {
				System.out.println("Deletion cancelled.");
				return;
			}
			ops.deleteDB(id);
			System.out.println("Employee " + id + " has been deleted successfully.");

		} catch (Exception e) {
			System.out.println("Error deleting employee: " + e.getMessage());
		}
	}
}
