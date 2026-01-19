package service;

import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

import dao.CrudImplementation;
import dao.EmployeeListOps;
import model.Employee;

public class LoginAndAccess {
	public static void authenticateInFile(CrudImplementation ops, Scanner sc) {

		Console console = System.console();

		while (true) {
			System.out.print("ID: ");
			String id = sc.nextLine();

			char[] passChars;
			if (console != null) {
				passChars = console.readPassword("Password: ");
			} else {
				System.out.print("Password: ");
				passChars = sc.nextLine().toCharArray();
			}

			String pass = new String(passChars);
			Arrays.fill(passChars, '\0');

			Employee emp = accessCheckFile(ops, id, pass);

			if (emp != null) {
				PasswordMethods.setLoginContext(emp.getId(), emp.getRole());
				System.out.println("Login successful\n");
				return;
			}

			System.out.println("Check details and try again.");
		}
	}

	private static Employee accessCheckFile(CrudImplementation ops, String empID, String pass) {

//		String hashedPass = PasswordMethods.hash(pass);

		for (Employee e : EmployeeListOps.findAll()) {
			if (e.getId().equals(empID) && BCrypt.checkpw(pass, e.getPassword())) {
				return e;
			}
		}
		return null;
	}

	public static boolean authenticateInDB(Connection conn, Scanner sc) {

		while (true) {
			System.out.print("Enter Your ID: ");
			String empId = sc.nextLine();

			System.out.print("Enter Password: ");
			String password = sc.nextLine();

			try {
				String passQuery = "SELECT empPassword FROM passwords WHERE empId = ?";
				PreparedStatement ps = conn.prepareStatement(passQuery);
				ps.setString(1, empId);

				ResultSet rs = ps.executeQuery();

				if (!rs.next()) {
					System.out.println("Invalid Employee ID. Please try again.");
					continue;
				}

				String storedHash = rs.getString("empPassword");

				if (!BCrypt.checkpw(password, storedHash)) {
					System.out.println("Invalid password. Please try again.");
					continue;
				}
				List<String> roles = fetchRoles(conn, empId);
				PasswordMethods.setLoginContext(empId, roles);

				System.out.println("Login successful!");
				return true;

			} catch (Exception e) {
				System.out.println("Validation error: " + e.getMessage());
			}
		}
	}

	private static List<String> fetchRoles(Connection conn, String empId) throws Exception {

		List<String> roles = new ArrayList<>();

		String roleQuery = "SELECT role FROM roles WHERE empId = ?";
		PreparedStatement ps = conn.prepareStatement(roleQuery);
		ps.setString(1, empId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			roles.add(rs.getString("role"));
		}

		return roles;
	}
}
