package service;

import java.io.File;

import java.sql.Connection;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import dao.CrudImplementation;
import dao.EmployeeListOps;
import dao.SaveEmployeesToFile;
import enums.RoleChoice;

public final class Update {

	private Update() {
	}

	public static void handleUpdate(CrudImplementation ops, Scanner sc, ObjectMapper mapper, File file)
			throws EmployeeNotFoundException, IdFormatWrongException {

		if (EmployeeListOps.isEmpty()) {
			System.out.println("Add data before updation");
			return;
		}

		String targetId = null;
		System.out.println("Update Details of:");
		System.out.println("Enter 1 for self.");
		System.out.println("Enter 2 to update Someone else's details.");
		System.out.println("Your choice: ");

		int who = sc.nextInt();
		sc.nextLine();
		if (who == 1) {
			targetId = PasswordMethods.getLoggedInId();
		} else if (who == 2) {
			System.out.println("Enter employee ID: ");
			targetId = sc.nextLine();

			if (!ops.employeeExists(targetId)) {
				System.out.println("Employee not found!");
				return;
			}
		}

		while (true) {
			System.out.println("""
					Enter 1 to Update all
					Enter 2 to Update name
					Enter 3 to Update mail
					Enter 4 to Update address
					Enter 5 to Update department
					Enter 6 to Manage roles

					Your choice :
					""");

			int ch;
			try {
				ch = sc.nextInt();
				sc.nextLine();
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("Please enter a number only.");
				continue;
			}

			switch (ch) {
			case 1 -> updateAll(ops, sc, targetId);
			case 2 -> updateName(ops, sc, targetId);
			case 3 -> updateMail(ops, sc, targetId);
			case 4 -> updateAddress(ops, sc, targetId);
			case 5 -> updateDepartment(ops, sc, targetId);
			case 6 -> updateRole(ops, sc, targetId);
			default -> System.out.println("Invalid choice");
			}

			SaveEmployeesToFile.saveToJson(ops, mapper, file);
			ops.showAll().forEach(System.out::println);
			break;
		}
	}

	private static void updateAll(CrudImplementation ops, Scanner sc, String id)
			throws EmployeeNotFoundException, IdFormatWrongException {
		updateName(ops, sc, id);
		updateMail(ops, sc, id);
		updateAddress(ops, sc, id);
		updateDepartment(ops, sc, id);
		updateRole(ops, sc, id);
	}

	private static void updateName(CrudImplementation ops, Scanner sc, String id)
			throws EmployeeNotFoundException, IdFormatWrongException {
		System.out.print("New name: ");
		ops.updateName(id, sc.nextLine());
		return;
	}

	private static void updateMail(CrudImplementation ops, Scanner sc, String id)
			throws EmployeeNotFoundException, IdFormatWrongException {
		System.out.print("New mail: ");
		ops.updateMail(id, sc.nextLine());
		return;
	}

	private static void updateAddress(CrudImplementation ops, Scanner sc, String id)
			throws EmployeeNotFoundException, IdFormatWrongException {
		System.out.print("New address: ");
		ops.updateAddress(id, sc.nextLine());
		return;
	}

	private static void updateDepartment(CrudImplementation ops, Scanner sc, String id)
			throws EmployeeNotFoundException, IdFormatWrongException {
		System.out.print("New department: ");
		ops.updateDepartment(id, sc.nextLine());
		return;
	}

	private static void updateRole(CrudImplementation ops, Scanner sc, String id)
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
		return;
	}

	public final static void handleUpdateForEmployee(CrudImplementation ops, Scanner sc) {

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
		return;
	}

// -----------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------

	public static void handleUpdateDB(CrudImplementation ops, Scanner sc, Connection conn) {
		try {

			String targetId = null;
			System.out.println("Update Details of:");
			System.out.println("Enter 1 for self.");
			System.out.println("Enter 2 to update Someone else's details.");
			System.out.println("Your choice: ");

			int who = sc.nextInt();
			sc.nextLine();
			if (who == 1) {
				targetId = PasswordMethods.getLoggedInId();
			} else if (who == 2) {
				System.out.println("Enter employee ID: ");
				targetId = sc.nextLine();

				if (!ops.employeeExistsDB(targetId)) {
					System.out.println("Employee not found!");
					return;
				}
			}

			while (true) {
				System.out.println("""
						Enter 1 to Update all
						Enter 2 to Update name
						Enter 3 to Update mail
						Enter 4 to Update address
						Enter 5 to Update department
						Enter 6 to Manage roles

						Your choice :
						""");
				int ch;
				try {
					ch = sc.nextInt();
					sc.nextLine();
				} catch (Exception e) {
					sc.nextLine();
					System.out.println("Please enter a number only.");
					continue;
				}

				switch (ch) {
				case 1 -> updateAllDB(ops, sc, targetId);
				case 2 -> updateNameDB(ops, sc, targetId);
				case 3 -> updateMailDB(ops, sc, targetId);
				case 4 -> updateAddressDB(ops, sc, targetId);
				case 5 -> updateDepartmentDB(ops, sc, targetId);
				case 6 -> updateRoleDB(ops, sc, targetId);
				default -> System.out.println("Invalid choice");
				}
//				ops.showAll().forEach(System.out::println);
				break;
			}
		} catch (Exception e) {
			System.out.println("Please enter a number only.");
		}
	}

	public static void updateAllDB(CrudImplementation ops, Scanner sc, String id) {
		updateNameDB(ops, sc, id);
		updateMailDB(ops, sc, id);
		updateAddressDB(ops, sc, id);
		updateDepartmentDB(ops, sc, id);
		updateRoleDB(ops, sc, id);
	}

	public static void updateNameDB(CrudImplementation ops, Scanner sc, String id) {
		System.out.print("New name: ");
		ops.updateNameDB(id, sc.nextLine());
		return;
	}

	public static void updateMailDB(CrudImplementation ops, Scanner sc, String id) {
		System.out.print("New mail: ");
		ops.updateMailDB(id, sc.nextLine());
		return;
	}

	public static void updateAddressDB(CrudImplementation ops, Scanner sc, String id) {
		System.out.print("New address: ");
		ops.updateAddressDB(id, sc.nextLine());
return;
	}

	public static void updateDepartmentDB(CrudImplementation ops, Scanner sc, String id) {
		System.out.print("New department: ");
		ops.updateDepartmentDB(id, sc.nextLine());
		return;
	}

	public static void updateRoleDB(CrudImplementation ops, Scanner sc, String id) {
		System.out.println("Enter 1 to Add role");
		System.out.println("Enter 2 to Revoke role");
		System.out.println("Your choice: ");
		int ch = sc.nextInt();
		sc.nextLine();

		for (RoleChoice r : RoleChoice.values()) {
			System.out.println(r);
		}

		RoleChoice role = RoleChoice.valueOf(sc.nextLine().toUpperCase());

		if (ch == 1) {
			ops.addRoleDB(id, role.name());
		} else if (ch == 2) {
			ops.revokeRoleDB(id, role.name());
		} else {
			System.out.println("Invalid choice");
		}
		return;
	}

	public final static void handleUpdateForEmployeeDB(CrudImplementation ops, Scanner sc) {

		System.out.println("Enter 1 to update your Mail ID");
		System.out.println("Enter 2 to update your Address");
		System.out.print("Your choice: ");

		int ch = sc.nextInt();
		sc.nextLine();

		String loggedInID = PasswordMethods.getLoggedInId();

		try {
			switch (ch) {
			case 1:
				Update.updateMailDB(ops, sc, loggedInID);
				break;

			case 2:
				Update.updateAddressDB(ops, sc, loggedInID);
				break;

			default:
				System.out.println("Invalid choice");
				break;
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return;
	}
}
