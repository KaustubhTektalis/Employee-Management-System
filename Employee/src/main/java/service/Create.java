package service;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

//import javax.management.relation.Role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
//import customExceptions.IdFormatWrongException;
import customExceptions.InvalidDataException;
import dao.CrudDBImplementation;
import dao.CrudFileImplementation;
import enums.RoleChoice;
import model.Employee;
import util.SaveEmployeesToFile;
import util.ValidateAddress;
import util.ValidateDepartment;
import util.ValidateMail;
import util.ValidateName;

public class Create {
	private static final Logger logger = LoggerFactory.getLogger(Create.class);

	public static void handleAdd(CrudFileImplementation ops, Scanner sc, ObjectMapper mapper, File file)
			throws InvalidDataException {

		String name;
		String mail;
		String address;
		String department;
		RoleChoice choice;

		while (true) {
			try {
				System.out.print("Enter name: ");
				name = sc.nextLine();
				ValidateName.validateName(name);
				break;
			} catch (InvalidDataException e) {
				System.out.println(e.getMessage());
			}
		}

		while (true) {
			try {
				System.out.print("Enter mail: ");
				mail = sc.nextLine();
				ValidateMail.validateMail(mail);
				break;
			} catch (InvalidDataException e) {
				System.out.println(e.getMessage());
			}
		}

		while (true) {
			try {
				System.out.print("Enter address: ");
				address = sc.nextLine();
				ValidateAddress.validateAddress(address);
				break;
			} catch (InvalidDataException e) {
				System.out.println(e.getMessage());
			}
		}

		while (true) {
			try {
				System.out.print("Enter department: ");
				department = sc.nextLine();
				ValidateDepartment.validateDepartment(department);
				break;
			} catch (InvalidDataException e) {
				System.out.println(e.getMessage());
			}
		}

		while (true) {
			try {
				System.out.println("Choose a role:");
				for (RoleChoice r : RoleChoice.values()) {
					System.out.println(r);
				}
				choice = RoleChoice.valueOf(sc.nextLine().toUpperCase());
				break;
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid Role. Please try again.");
			}
		}

		try {
			ArrayList<String> role = new ArrayList<>();
			role.add(choice.name().charAt(0) + choice.name().substring(1).toLowerCase());

			String randomPasswordForNew = PasswordMethods.randomPasswordGenerator();
			Employee newEmployee = ops.add(name, mail, address, department, role, randomPasswordForNew);

			SaveEmployeesToFile.saveToJson(mapper, file);

			logger.info("New employee added: id={}, name={}, department={}, role={}", newEmployee.getId(),
					newEmployee.getName(), newEmployee.getDepartment(), role);
			System.out.println("New employee added!");
			logger.info("Employee {} added", newEmployee.getId());
			System.out.println(
					"Generated password for new employee " + newEmployee.getId() + " is: " + randomPasswordForNew);
			logger.info("Password generated for new employee {}", newEmployee.getId());

		} catch (Exception e) {
			logger.error("Error while adding employee", e);
		}
	}

//---------------------------------------------------------------------------------------------------------------------------	

	public static void handleAddDB(CrudDBImplementation dbops, Scanner sc)
			throws SQLException, EmployeeNotFoundException, IdFormatWrongException {

		String name;
		String mail;
		String address;
		String department;
		RoleChoice choice;

		while (true) {
			try {
				System.out.print("Enter name: ");
				name = sc.nextLine();
				ValidateName.validateName(name);
				break;
			} catch (InvalidDataException e) {
				System.out.println(e.getMessage());
			}
		}

		while (true) {
			try {
				System.out.print("Enter mail: ");
				mail = sc.nextLine();
				ValidateMail.validateMail(mail);
				break;
			} catch (InvalidDataException e) {
				System.out.println(e.getMessage());
			}
		}

		while (true) {
			try {
				System.out.print("Enter address: ");
				address = sc.nextLine();
				ValidateAddress.validateAddress(address);
				break;
			} catch (InvalidDataException e) {
				System.out.println(e.getMessage());
			}
		}

		while (true) {
			try {
				System.out.print("Enter department: ");
				department = sc.nextLine();
				ValidateDepartment.validateDepartment(department);
				break;
			} catch (InvalidDataException e) {
				System.out.println(e.getMessage());
			}
		}

		while (true) {
			System.out.println("Choose a role:");
			for (RoleChoice r : RoleChoice.values()) {
				System.out.println(r);
			}
			choice = RoleChoice.valueOf(sc.nextLine().toUpperCase());

			break;
		}

		try {
			String password = PasswordMethods.randomPasswordGenerator();
			String empId = dbops.addDB(name, mail, address, department, choice);
			PasswordTableDB.insertPassword(empId, password);

			System.out.println("New employee added!");
			logger.info("New employee added: {}", Read.handleReadOneDB(dbops,empId));
			System.out.println("Generated password for new employee " + empId + " is: " + password);
			logger.info("Password generated for new employee {}", empId);

		} catch (SQLException e) {
			logger.error("Database error while adding employee", e);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid input: {}", e.getMessage());
			System.out.println("Failed to add employee. Please try again.");
		}
	}
}
