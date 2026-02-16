package service;

import java.io.File;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import dao.CrudDBImplementation;
import dao.CrudFileImplementation;
import util.SaveEmployeesToFile;
import util.ValidateId;

public class Delete {
	private static final Logger logger = LoggerFactory.getLogger(Delete.class);

	public static void handleDelete(CrudFileImplementation ops, Scanner sc, ObjectMapper mapper, File file)
			throws EmployeeNotFoundException, IdFormatWrongException {

		try {
			System.out.print("Enter employee ID to delete: ");
			String id = sc.nextLine();
			ValidateId.validateId(id);

			if (id.equals(LoginAndAccess.getLoggedInId())) {
				System.out.println("You Cannot delete your own records.");
				return;
			}

			System.out.print("Are you sure you want to delete Employee " + id + "? (yes/no): ");
			String confirm = sc.nextLine().trim().toLowerCase();

			if (!confirm.equals("yes")) {
				System.out.println("Deletion cancelled.");
				return;
			}
			ops.delete(id);
			SaveEmployeesToFile.saveToJson(mapper, file);
			System.out.println("Employee " + id + " has been deleted successfully.");
			logger.info("Employee {} has been deleted successfully.", id);
		} catch (Exception e) {
			logger.warn("Error deleting employee: {}", e.getMessage(), e);
		}
	}

	// ----------------------------------------------------------------------------------------------------

	public static void handleDeleteDB(CrudDBImplementation dbops, Scanner sc) {
		try {
			System.out.print("Enter the Employee ID to delete: ");
			String id = sc.nextLine();

			if (id.equals(LoginAndAccess.getLoggedInId())) {
				System.out.println("You Cannot delete your own records.");
//				logger.warn("You Cannot delete your own records.");
				return;
			}

			System.out.print("Are you sure you want to delete Employee " + id + "? (yes/no): ");
			String confirm = sc.nextLine().trim().toLowerCase();

			if (!confirm.equals("yes")) {
				System.out.println("Deletion cancelled.");
				return;
			}
			boolean deleted = dbops.deleteDB(id);

			if (deleted) {
				logger.info("Employee {} has been deleted successfully.", id);
				System.out.println("Employee " + id + " deleted successfully.");
			} else {
				System.out.println("No employee found with ID: " + id);
			}
//			System.out.println("Employee "+ id +" has been deleted successfully.");
//			logger.info("Employee {} has been deleted successfully.", id);

		} catch (Exception e) {
			logger.warn("Error deleting employee: {}", e.getMessage(), e);
			 System.out.println("Error deleting employee: " + e.getMessage());
		}
	}
}
