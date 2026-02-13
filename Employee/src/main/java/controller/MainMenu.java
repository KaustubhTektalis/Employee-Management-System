package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.CrudDBImplementation;
import dao.CrudFileImplementation;
import dao.EmployeeListOps;
import enums.ChooseBackend;
import model.Employee;
import util.MakeConnection;
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
			System.out.println("Select correct option" + e.getMessage());

		}
	}

	public static void FileMenu() {
		
		CrudFileImplementation fops= new CrudFileImplementation();

		ObjectMapper mapper = new ObjectMapper();
		File file = new File(FILE_PATH);

		try {

			if (file.exists() && file.length() > 0) {
				List<Employee> list = mapper.readValue(file, new TypeReference<List<Employee>>() {
				});
				EmployeeListOps.setEmployees(list);
			}

			LoginAndAccess.authenticateInFile(fops, sc);

			
			if (LoginAndAccess.hasRole("Admin")) {
				AdminMenu.showMenu(fops, sc, mapper, file);

			} else if (LoginAndAccess.hasRole("Manager")) {
				ManagerMenu.showMenu(fops, sc, mapper, file);

			} else {
				EmployeeMenu.showMenu(fops, sc, mapper, file);
			}
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public static void DBMenu() {

		/*
		 * POSTGRESQL: (LOCAL) host: "localhost" dbname: "crudoperations" user:
		 * "postgres" password: "pass"
		 * 
		 * 
		 * Supabase: (MAKE SURE TO OCNNECT TO A IPV6 NETWORK) host:
		 * "db.gngwzkdvmixpgvkxknpf.supabase.co" dbname: "postgres?sslmode=require"
		 * user: "postgres" password: "tektalisPASS123$"
		 * 
		 */
		MakeConnection db = new MakeConnection();
		Connection conn = null;

		CrudDBImplementation dbops=new CrudDBImplementation(conn);
		
		try {
			conn = db.connect_to_db("localhost", "crudoperations", "postgres", "pass");
		} catch (SQLException e) {
			System.out.println("Database connection failed: " + e.getMessage());
			return;
		}

		if (conn == null) {
			System.out.println("Database connection failed.");
			return;
		}

		try {

			LoginAndAccess.authenticateInDB(conn, sc);

			if (LoginAndAccess.hasRole("Admin")) {
				AdminMenu.showDBMenu(dbops, sc, conn);
			} else if (LoginAndAccess.hasRole("Manager")) {
				ManagerMenu.showDBMenu(dbops, sc, conn);
			} else {
				EmployeeMenu.showDBMenu(dbops, sc,conn);
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
