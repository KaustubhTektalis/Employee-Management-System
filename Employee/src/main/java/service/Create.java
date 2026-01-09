package service;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import customExceptions.InvalidDataException;
import dao.CrudOps;
import dao.SaveEmployeesToFile;
import enums.RoleChoice;
import model.Employee;

public class Create {
	public static void handleAdd(CrudOps ops, Scanner sc, ObjectMapper mapper, File file)
			throws EmployeeNotFoundException, IdFormatWrongException {

		try {
			System.out.print("Enter name: ");
			String name = sc.nextLine();

			System.out.print("Enter mail: ");
			String mail = sc.nextLine();

			System.out.print("Enter address: ");
			String address = sc.nextLine();

			System.out.print("Enter department: ");
			String department = sc.nextLine();

			System.out.println("Choose role:");
			for (RoleChoice r : RoleChoice.values()) {
				System.out.println(r);
			}

			RoleChoice choice = RoleChoice.valueOf(sc.nextLine().toUpperCase());

			ArrayList<String> role = new ArrayList<>();
			role.add(choice.name().charAt(0) + choice.name().substring(1).toLowerCase());

			String randomPasswordForNew=PasswordMethods.randomPasswordGenerator();
			Employee newEmployee = ops.add(name, mail, address, department, role, randomPasswordForNew);
			SaveEmployeesToFile.saveToJson(ops, mapper, file);
			System.out.println("New user added!");
			System.out.println("The password for new user "+ newEmployee.getId()  +"to login is: "+ randomPasswordForNew); 

//			ops.showAll().forEach(System.out::println);

		} catch (InvalidDataException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
}
