package service;

import java.util.Scanner;

import dao.CrudOps;
import dao.EmployeeListOps;
import model.Employee;

import java.io.Console;
import java.util.Arrays;

public class LoginAndAccess {
	public static void authenticate(CrudOps ops, Scanner sc) {
		Console console = System.console();
		while (true) {
			System.out.print("ID: ");
			String id = sc.nextLine();

			char[] passChars;
			if (console != null)
				passChars = console.readPassword("Password: ");
			else {
				System.out.print("Password: ");
				passChars = sc.nextLine().toCharArray();
			}

			String pass = new String(passChars);
			Arrays.fill(passChars, '\0');

			if (accessCheck(ops, id, pass)) {
				PasswordMethods.setLoggedInID(id);
				System.out.println("Login successful\n");
				return;
			}
		}
	}

	public static boolean accessCheck(CrudOps ops, String empID, String pass) {

		String hashedPass = PasswordHasher.hash(pass);

		for (Employee e : EmployeeListOps.findAll()) {
			if (e.getId().equals(empID) && e.getPassword().equals(hashedPass)) {

				return true;
			}
		}
		return false;
	}

}