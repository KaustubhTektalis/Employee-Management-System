package controller;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.CrudImplementation;
import dao.EmployeeListOps;
import enums.ChooseBackend;
import model.Employee;
import service.PasswordMethods;
import service.LoginAndAccess;

public class MainMenu {
	private static final String FILE_PATH = "employees_data.json";
	static Scanner sc = new Scanner(System.in);

	public static void Menu() {
		ChooseBackend ch = null;
		for (ChooseBackend c : ChooseBackend.values()) {
			System.out.println(c);
		}
		System.out.println("Your choice: ");
		try {
			ch = ChooseBackend.valueOf(sc.nextLine().trim().toUpperCase());

		} catch (IllegalArgumentException e) {
			System.out.println("Invalid choice.");
		}
		try {
			switch (ch) {
			case FILE:
				FileMenu();
				break;

			case DB:
				DBMenu();
				break;

			default:
				System.out.println("Select correct option");
				break;

			}
		} catch (Exception e) {
			System.out.println("Select correct option"+e.getMessage());

		}
	}

	public static void FileMenu() {

		CrudImplementation ops = new CrudImplementation(ChooseBackend.FILE, null);

		ObjectMapper mapper = new ObjectMapper();
		File file = new File(FILE_PATH);

		try {

			if (file.exists() && file.length() > 0) {
				List<Employee> list = mapper.readValue(file, new TypeReference<List<Employee>>() {
				});
				EmployeeListOps.setEmployees(list);
			}

			LoginAndAccess.authenticateInFile(ops, sc);

			if (PasswordMethods.hasRole("Admin")) {
				AdminMenu.showMenu(ops, sc, mapper, file);

			} else if (PasswordMethods.hasRole("Manager")) {
				ManagerMenu.showMenu(ops, sc, mapper, file);

			} else {
				EmployeeMenu.showMenu(ops, sc);
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public static void DBMenu() {

		MakeConnection db = new MakeConnection();
		Connection conn = db.connect_to_db("crudoperations", "postgres", "pass");

		if (conn == null) {
			System.out.println("Database connection failed.");
			return;
		}
		CrudImplementation ops = new CrudImplementation(ChooseBackend.DB, conn);

		try {

			LoginAndAccess.authenticateInDB(conn, sc);

			if (PasswordMethods.hasRole("Admin")) {
				AdminMenu.showDBMenu(ops, sc, conn);
			} else if (PasswordMethods.hasRole("Manager")) {
				ManagerMenu.showDBMenu(ops, sc, conn);
			} else {
				EmployeeMenu.showDBMenu(ops, sc);
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
