package service;

import java.io.File;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import dao.CrudOps;
import dao.EmployeeListOps;
import dao.SaveEmployeesToFile;
import enums.RoleChoice;

public final class Update {
	
	private Update() {}
	
	public static void handleUpdate(CrudOps ops, Scanner sc, ObjectMapper mapper, File file)
			throws EmployeeNotFoundException, IdFormatWrongException {

		if (EmployeeListOps.isEmpty()) {
			System.out.println("Add data before updation");
			return;
		}

		System.out.print("Enter employee ID: ");
		String id = sc.nextLine();

		if (!ops.employeeExists(id)) {
			System.out.println("Employee not found!");
			return;
		}

		System.out.println("""
				Enter 1 to Update all
				Enter 2 to Update name
				Enter 3 to Update mail
				Enter 4 to Update address
				Enter 5 to Update department
				Enter 6 to Manage roles
				
				Your choice : 
				""");

		int ch = sc.nextInt();
		sc.nextLine();

		switch (ch) {
		case 1 -> updateAll(ops, sc, id);
		case 2 -> updateName(ops, sc, id);
		case 3 -> updateMail(ops, sc, id);
		case 4 -> updateAddress(ops, sc, id);
		case 5 -> updateDepartment(ops, sc, id);
		case 6 -> updateRole(ops, sc, id);
		default -> System.out.println("Invalid choice");
		}

		SaveEmployeesToFile.saveToJson(ops, mapper, file);
		ops.showAll().forEach(System.out::println);
	}

	private static void updateAll(CrudOps ops, Scanner sc, String id)
			throws EmployeeNotFoundException, IdFormatWrongException {
		updateName(ops, sc, id);
		updateMail(ops, sc, id);
		updateAddress(ops, sc, id);
		updateDepartment(ops, sc, id);
		updateRole(ops, sc, id);
	}

	private static void updateName(CrudOps ops, Scanner sc, String id)
			throws EmployeeNotFoundException, IdFormatWrongException {
		System.out.print("New name: ");
		ops.updateName(id, sc.nextLine());
	}

	private static void updateMail(CrudOps ops, Scanner sc, String id)
			throws EmployeeNotFoundException, IdFormatWrongException {
		System.out.print("New mail: ");
		ops.updateMail(id, sc.nextLine());
	}

	private static void updateAddress(CrudOps ops, Scanner sc, String id)
			throws EmployeeNotFoundException, IdFormatWrongException {
		System.out.print("New address: ");
		ops.updateAddress(id, sc.nextLine());
	}

	private static void updateDepartment(CrudOps ops, Scanner sc, String id)
			throws EmployeeNotFoundException, IdFormatWrongException {
		System.out.print("New department: ");
		ops.updateDepartment(id, sc.nextLine());
	}

	private static void updateRole(CrudOps ops, Scanner sc, String id)
			throws EmployeeNotFoundException, IdFormatWrongException {

		System.out.println("1. Add role");
		System.out.println("2. Revoke role");
		int ch = sc.nextInt();
		sc.nextLine();

		for (RoleChoice r : RoleChoice.values()) {
			System.out.println(r);
		}

		RoleChoice role = RoleChoice.valueOf(sc.nextLine().toUpperCase());

		if (ch == 1) {
			ops.addRole(id, role.name());
		} else if (ch == 2) {
			ops.revokeRole(id, role.name());
		} else {
			System.out.println("Invalid choice");
		}
	}
	
	
	
	public final static void handleUpdateForEmployee(CrudOps ops, Scanner sc) {

		System.out.println("Enter 1 to update your Mail ID");
		System.out.println("Enter 2 to update your Address");
		System.out.print("Your choice: ");

		int ch = sc.nextInt();
		sc.nextLine();

		String loggedInID = PasswordMethods.getLoggedInId();

		try {
			switch (ch) {
			case 1:
				Update.updateMail(ops, sc, loggedInID);
				break;

			case 2:
				Update.updateAddress(ops, sc, loggedInID);
				break;

			default:
				System.out.println("Invalid choice");
				break;
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

}
