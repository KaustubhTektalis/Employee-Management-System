package service;

import java.io.File;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import dao.CrudImplementation;
import dao.SaveEmployeesToFile;
import util.ValidateId;

public class Delete {
	private static final Logger logger = LogManager.getLogger(Delete.class);

	public static void handleDelete(CrudImplementation ops, Scanner sc, ObjectMapper mapper, File file)
			throws EmployeeNotFoundException, IdFormatWrongException {

		try {
			System.out.print("Enter employee ID to delete: ");
			String id = sc.nextLine();
			ValidateId.validateId(id);

			if (id.equals(PasswordMethods.getLoggedInId())) {
				logger.warn("You Cannot delete your own records.");
				return;
			}

			System.out.print("Are you sure you want to delete Employee " + id + "? (yes/no): ");
			String confirm = sc.nextLine().trim().toLowerCase();

			if (!confirm.equals("yes")) {
				logger.info("Deletion cancelled.");
				return;
			}
			ops.delete(id);
			SaveEmployeesToFile.saveToJson(mapper, file);
			logger.info("Employee {} has been deleted successfully.", id);
		} catch (Exception e) {
			logger.warn("Error deleting employee: {}", e.getMessage(), e);
		}
	}

	public static void handleDeleteDB(CrudImplementation ops, Scanner sc, Connection conn) {
		try {
			System.out.print("Enter the Employee ID to delete: ");
			String id = sc.nextLine();

			if (id == PasswordMethods.getLoggedInId()) {
				logger.warn("You Cannot delete your own records.");
				return;
			}

			System.out.print("Are you sure you want to delete Employee " + id + "? (yes/no): ");
			String confirm = sc.nextLine().trim().toLowerCase();

			if (!confirm.equals("yes")) {
				System.out.println("Deletion cancelled.");
				return;
			}
			ops.deleteDB(id);
			logger.info("Employee {} has been deleted successfully.", id);

		} catch (Exception e) {
			logger.warn("Error deleting employee: {}", e.getMessage(), e);
		}
	}
}
