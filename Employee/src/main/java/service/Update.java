package service;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import customExceptions.InvalidDataException;
import dao.CrudDBImplementation;
import dao.CrudFileImplementation;
import enums.RoleChoice;
import util.SaveEmployeesToFile;
import util.ValidateAddress;
import util.ValidateDepartment;
import util.ValidateMail;
import util.ValidateName;

public final class Update {
	private static final Logger logger = LoggerFactory.getLogger(Update.class);

	private Update() {
	}

	public static void handleUpdateMenu(CrudFileImplementation ops, Scanner sc, ObjectMapper mapper, File file)
			throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {

		if (ops.checkEmpty()) {
			logger.warn("There are no employees at the moment, add data first.");
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
			targetId = LoginAndAccess.getLoggedInId();
		} else if (who == 2) {
			System.out.println("Enter employee ID: ");
			targetId = sc.nextLine();

			if (!ops.employeeExists(targetId)) {
				logger.warn("Employee not found!");
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
				logger.warn("Invalid input, need to enter a number.");
				System.out.println("Invalid input, enter a number.");
				continue;
			}

			switch (ch) {
			case 1 -> handleUpdateAll(ops, sc, targetId, mapper, file);
			case 2 -> handleUpdateName(ops, sc, targetId, mapper, file);
			case 3 -> handleUpdateMail(ops, sc, targetId, mapper, file);
			case 4 -> handleUpdateAddress(ops, sc, targetId, mapper, file);
			case 5 -> handleUpdateDepartment(ops, sc, targetId, mapper, file);
			case 6 -> handleUpdateRole(ops, sc, targetId, mapper, file);
			default -> System.out.println("Invalid choice selected for update: " + ch);
			}
			break;
		}
	}

	private static void handleUpdateAll(CrudFileImplementation ops, Scanner sc, String id, ObjectMapper mapper,
			File file) throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
		handleUpdateName(ops, sc, id, mapper, file);
		handleUpdateMail(ops, sc, id, mapper, file);
		handleUpdateAddress(ops, sc, id, mapper, file);
		handleUpdateDepartment(ops, sc, id, mapper, file);
		handleUpdateRole(ops, sc, id, mapper, file);
	}

	private static void handleUpdateName(CrudFileImplementation ops, Scanner sc, String id, ObjectMapper mapper,
			File file) throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
		while (true) {
			try {
				System.out.print("New name: ");
				String name = sc.nextLine();
				ValidateName.validateName(name);
				ops.updateName(id, name);
				SaveEmployeesToFile.saveToJson(mapper, file);
				System.out.println("Updated name for employee ID " + id + " : " + name);
				logger.info("Updated name for employee ID {}: {}", id, name);
				break;
			} catch (InvalidDataException e) {
				System.out.println(e.getMessage());
			}
		}
		return;
	}

	private static void handleUpdateMail(CrudFileImplementation ops, Scanner sc, String id, ObjectMapper mapper,
			File file) throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
		while (true) {
			try {
				System.out.print("New mail: ");
				String mail = sc.nextLine();
				ValidateMail.validateMail(mail);
				ops.updateMail(id, mail);
				System.out.println("Updated mail for employee ID " + id + " : " + mail);
				SaveEmployeesToFile.saveToJson(mapper, file);
				logger.info("Updated mail for employee ID {}: {}", id, mail);
				break;
			} catch (InvalidDataException e) {
				System.out.println(e.getMessage());
			}
		}
		return;
	}

	private static void handleUpdateAddress(CrudFileImplementation ops, Scanner sc, String id, ObjectMapper mapper,
			File file) throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
		while (true) {
			try {
				System.out.print("New address: ");
				String address = sc.nextLine();
				ValidateAddress.validateAddress(address);
				ops.updateAddress(id, address);
				SaveEmployeesToFile.saveToJson(mapper, file);
				System.out.println("Updated address for employee ID " + id + " : " + address);
				logger.info("Updated address for employee ID {}: {}", id, address);
				break;
			} catch (InvalidDataException e) {
				System.out.println(e.getMessage());
			}
		}
		return;
	}

	private static void handleUpdateDepartment(CrudFileImplementation ops, Scanner sc, String id, ObjectMapper mapper,
			File file) throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
		while (true) {
			try {
				System.out.print("New department: ");
				String department = sc.nextLine();
				ValidateDepartment.validateDepartment(department);
				ops.updateDepartment(id, department);
				SaveEmployeesToFile.saveToJson(mapper, file);
				System.out.println("Updated department for employee ID " + id + " : " + department);
				logger.info("Updated department for employee ID {}: {}", id, department);
				break;
			} catch (InvalidDataException e) {
				System.out.println(e.getMessage());
			}
		}
		return;
	}

	private static void handleUpdateRole(CrudFileImplementation ops, Scanner sc, String id, ObjectMapper mapper,
			File file) throws EmployeeNotFoundException, IdFormatWrongException {
		while (true) {

			System.out.println("1. Add role");
			System.out.println("2. Revoke role");
			System.out.print("Your choice: ");

			int ch;
			try {
				ch = sc.nextInt();
				sc.nextLine();
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("Invalid input. Enter 1 or 2.");
				continue;
			}

			if (ch == 1) {

				while (true) {
					for (RoleChoice r : RoleChoice.values()) {
						System.out.println(r);
					}

					System.out.println("Enter role to add: ");

					String roleString = sc.nextLine().toUpperCase();

					try {
						RoleChoice role = RoleChoice.valueOf(roleString);

						if (ops.readOne(id).getRole().contains(role.name())) {
							System.out.println("Role already exists");
							continue;
						}
						ops.addRole(id, role.name());
						System.out.println("Added role " + role + " to employee ID " + id);
						SaveEmployeesToFile.saveToJson(mapper, file);
						logger.info("Added role {} to employee ID {}", role, id);
						return;
					} catch (IllegalArgumentException e) {
						System.out.println("Invalid role. Try again.");
					}
				}

			} else if (ch == 2) {

				while (true) {
					System.out.println("Existing roles: ");
					for (String s : ops.readOne(id).getRole()) {
						System.out.println(s);
					}
					System.out.println("Select a role to revoke: ");
					String roleString = sc.nextLine().toUpperCase();

					try {
						RoleChoice role = RoleChoice.valueOf(roleString);

//						ops.revokeRole(id, role.name());
						if (!ops.readOne(id).getRole().contains(role.name())) {
						    System.out.println("Role not assigned to employee.");
						    continue;
						}
						
						System.out.println("Revoked role " + role + " to employee ID " + id);
						SaveEmployeesToFile.saveToJson(mapper, file);
						logger.info("Revoked role {} from employee ID {}", role, id);
						return;
					} catch (IllegalArgumentException e) {
						System.out.println("Invalid role. Try again.");
					}
				}
			} else {
				System.out.println("Invalid role operation choice: " + ch + " Select 1 or 2.");
				logger.warn("Invalid role operation choice: {}", ch);
				return;
			}
		}
	}

	public final static void handleUpdateMenuForEmployee(CrudFileImplementation ops, Scanner sc, ObjectMapper mapper,
			File file) {

		System.out.println("Enter 1 to update your Mail ID");
		System.out.println("Enter 2 to update your Address");
		System.out.print("Your choice: ");

		int ch = sc.nextInt();
		sc.nextLine();

		String loggedInID = LoginAndAccess.getLoggedInId();

		try {
			switch (ch) {
			case 1:
				Update.handleUpdateMail(ops, sc, loggedInID, mapper, file);
				break;

			case 2:
				Update.handleUpdateAddress(ops, sc, loggedInID, mapper, file);
				break;

			default:
				System.out.println("Invalid choice");
				break;
			}
		} catch (Exception e) {
			System.out.println("Invalid choice.");
			logger.warn("Error: " + e.getMessage());
		}
		return;
	}

// -----------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------

	public static void handleUpdateMenuDB(CrudDBImplementation dbops, Scanner sc) throws SQLException{
		try {

			String targetId = null;
			System.out.println("Update Details of:");
			System.out.println("Enter 1 for self.");
			System.out.println("Enter 2 to update Someone else's details.");
			System.out.println("Your choice: ");

			int who = sc.nextInt();
			sc.nextLine();

			if (who != 1 && who != 2) {
				System.out.println("Invalid selection. Select 1 or 2.");
				logger.warn("Invalid selection");
				return;
			}

			if (who == 1) {
				targetId = LoginAndAccess.getLoggedInId();
			} else if (who == 2) {
				System.out.println("Enter employee ID: ");
				targetId = sc.nextLine();

				if (!dbops.employeeExistsDB(targetId)) {
					System.out.println("Employee not found!");
					logger.warn("Employee not found!");
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
					System.out.println("Invalid input, enter a number 1-6.");
					logger.warn("Invalid input, enter a number.");
					continue;
				}

				switch (ch) {
				case 1 -> handleUpdateAllDB(dbops, sc, targetId);
				case 2 -> handleUpdateNameDB(dbops, sc, targetId);
				case 3 -> handleUpdateMailDB(dbops, sc, targetId);
				case 4 -> handleUpdateAddressDB(dbops, sc, targetId);
				case 5 -> handleUpdateDepartmentDB(dbops, sc, targetId);
				case 6 -> handleUpdateRoleDB(dbops, sc, targetId);
				default -> System.out.println("Invalid choice selected for update: " + ch);
				}
				break;
			}
		} catch (Exception e) {
			System.out.println("Error updating data.");
			e.printStackTrace();
			logger.error("Error updating data.");
		}
	}

	public static void handleUpdateAllDB(CrudDBImplementation dbops, Scanner sc, String id)
			throws InvalidDataException, EmployeeNotFoundException, IdFormatWrongException, SQLException {
		handleUpdateNameDB(dbops, sc, id);
		handleUpdateMailDB(dbops, sc, id);
		handleUpdateAddressDB(dbops, sc, id);
		handleUpdateDepartmentDB(dbops, sc, id);
		handleUpdateRoleDB(dbops, sc, id);
	}

	public static void handleUpdateNameDB(CrudDBImplementation dbops, Scanner sc, String id)
			throws InvalidDataException, EmployeeNotFoundException, IdFormatWrongException, SQLException {
		System.out.print("New name: ");
		String name = sc.nextLine();
		ValidateName.validateName(name);
		boolean updated = dbops.updateNameDB(id, name);
		if (updated) {
			Read.handleReadOneDB(dbops, id);
			logger.info("Updated name for employee ID {}: {}", id, name);
			System.out.println("Updated name for employee ID " + id + " : " + name);
		} else {
			logger.info("Could not update name for employee ID {}", id);
			System.out.println("Could not update name for employee ID " + id);
		}
		return;
	}

	public static void handleUpdateMailDB(CrudDBImplementation dbops, Scanner sc, String id)
			throws InvalidDataException, EmployeeNotFoundException, IdFormatWrongException, SQLException {
		System.out.print("New mail: ");
		String mail = sc.nextLine();
		ValidateMail.validateMail(mail);
		boolean updated = dbops.updateMailDB(id, mail);
		if (updated) {
			Read.handleReadOneDB(dbops, id);
			logger.info("Updated mail for employee ID {}: {}", id, mail);
			System.out.println("Updated mail for employee ID " + id + " : " + mail);
		} else {
			logger.info("Could not update mail for employee ID {}", id);
			System.out.println("Could not update mail for employee ID " + id);
		}
		return;
	}

	public static void handleUpdateAddressDB(CrudDBImplementation dbops, Scanner sc, String id)
			throws InvalidDataException, EmployeeNotFoundException, IdFormatWrongException, SQLException {
		System.out.print("New address: ");
		String address = sc.nextLine();
		ValidateAddress.validateAddress(address);
		boolean updated = dbops.updateAddressDB(id, address);
		if (updated) {
			Read.handleReadOneDB(dbops, id);
			logger.info("Updated address for employee ID {}: {}", id, address);
			System.out.println("Updated address for employee ID " + id + " : " + address);
		} else {
			logger.info("Could not update address for employee ID {}", id);
			System.out.println("Could not update address for employee ID " + id);
		}
		return;
	}

	public static void handleUpdateDepartmentDB(CrudDBImplementation dbops, Scanner sc, String id)
			throws InvalidDataException, EmployeeNotFoundException, IdFormatWrongException, SQLException {
		System.out.print("New department: ");
		String department = sc.nextLine();
		ValidateDepartment.validateDepartment(department);
		boolean updated = dbops.updateDepartmentDB(id, department);
		if (updated) {
			Read.handleReadOneDB(dbops, id);
			logger.info("Updated department for employee ID {}: {}", id, department);
			System.out.println("Updated department for employee ID " + id + " : " + department);
		} else {
			logger.info("Could not update department for employee ID {}", id);
			System.out.println("Could not update department for employee ID " + id);
		}
		return;
	}

	public static void handleUpdateRoleDB(CrudDBImplementation dbops, Scanner sc, String id)
			throws SQLException, EmployeeNotFoundException, IdFormatWrongException {

		System.out.println("Enter 1 to Add role");
		System.out.println("Enter 2 to Revoke role");
		System.out.print("Your choice: ");

		int ch;
		try {
			ch = sc.nextInt();
			sc.nextLine();
		} catch (Exception e) {
			sc.nextLine();
			System.out.println("Invalid input.");
			return;
		}

		if (ch == 1) {

			for (RoleChoice r : RoleChoice.values()) {
				System.out.println(r);
			}

			System.out.print("Your choice: ");
			String roleInput = sc.nextLine().toUpperCase();

			try {
				RoleChoice role = RoleChoice.valueOf(roleInput);

				boolean added = dbops.addRoleDB(id, role.name());

				if (added) {
					Read.handleReadOneDB(dbops, id);
					System.out.println("Added role " + role + " successfully.");
				} else {
					System.out.println("Cannot add role (inactive employee or duplicate role).");
				}

			} catch (IllegalArgumentException e) {
				System.out.println("Invalid role.");
			}
		}

		else if (ch == 2) {

			List<String> existingRoles = LoginAndAccess.fetchRoles(id);

			if (existingRoles.isEmpty()) {
				System.out.println("Employee has no roles to revoke.");
				return;
			}

			System.out.println("Existing roles: " + existingRoles);
			System.out.print("Enter role to revoke: ");
			String roleInput = sc.nextLine().toUpperCase();

			try {
				RoleChoice role = RoleChoice.valueOf(roleInput);

				boolean revoked = dbops.revokeRoleDB(id, role.name());

				if (revoked) {
					Read.handleReadOneDB(dbops, id);
					System.out.println("Revoked role " + role + " successfully.");
				} else {
					System.out.println("Role not active or cannot revoke.");
				}

			} catch (IllegalArgumentException e) {
				System.out.println("Invalid role.");
			}
			catch(SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}

		else {
			System.out.println("Invalid choice.");
		}
	}

	public final static void handleUpdateMenuForEmployeeDB(CrudDBImplementation dbops, Scanner sc)
			throws InvalidDataException {

		System.out.println("Enter 1 to update your Mail ID");
		System.out.println("Enter 2 to update your Address");
		System.out.print("Your choice: ");

		int ch = sc.nextInt();
		sc.nextLine();

		String loggedInID = LoginAndAccess.getLoggedInId();

		try {
			switch (ch) {
			case 1:
				Update.handleUpdateMailDB(dbops, sc, loggedInID);
				break;

			case 2:
				Update.handleUpdateAddressDB(dbops, sc, loggedInID);
				break;

			default:
				System.out.println("Invalid choice");
				logger.warn("Invalid choice");
				break;
			}
		} catch (InvalidDataException e) {
			System.out.println("Invalid data." + e.getMessage());
			logger.warn("Invalid data: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error updating data." + e.getMessage());
			logger.warn("Error updating data: " + e.getMessage());
		}
		return;
	}
}
