package controller;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.CrudOps;
import dao.EmployeeListOps;
import model.Employee;
import service.PasswordMethods;
import service.LoginAndAccess;

public class MainMenu {
	private static final String FILE_PATH = "employees_data.json";

	public static void Menu() {

		Scanner sc = new Scanner(System.in);

		EmployeeListOps repository = new EmployeeListOps();
		CrudOps ops = new CrudOps(repository);

		ObjectMapper mapper = new ObjectMapper();
		File file = new File(FILE_PATH);

		try {

			if (file.exists() && file.length() > 0) {
				List<Employee> list = mapper.readValue(file, new TypeReference<List<Employee>>() {
				});
				EmployeeListOps.setEmployees(list);
			}

			LoginAndAccess.authenticate(ops, sc);

			String loggedInId = PasswordMethods.getLoggedInId();

			Employee loggedInEmployee = ops.showOne(loggedInId);

			if (loggedInEmployee.getRole().contains("Admin")) {
				AdminMenu.showMenu(ops, sc, mapper, file);

			} else if (loggedInEmployee.getRole().contains("Manager")) {
				ManagerMenu.showMenu(ops, sc, mapper, file);

			} else {
				EmployeeMenu.showMenu(ops, sc);
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());

		} finally {
			sc.close();
		}
	}
}
