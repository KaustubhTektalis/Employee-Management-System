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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dao.CrudImplementation;
import dao.EmployeeListOps;
import model.Employee;

public class LoginAndAccess {
	private static final Logger logger = LogManager.getLogger(LoginAndAccess.class);
	
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
				logger.info("Login successful\n");
				return;
			}

			logger.warn("Check details and try again.");
		}
	}

	private static Employee accessCheckFile(CrudImplementation ops, String empID, String pass) {
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
				String passQuery = "SELECT p.empPassword FROM passwords p JOIN employees e ON p.empid=e.empid WHERE p.empId = ? AND e.active IS TRUE";
				PreparedStatement ps = conn.prepareStatement(passQuery);
				ps.setString(1, empId);

				ResultSet rs = ps.executeQuery();

				if (!rs.next()) {
					logger.warn("Invalid Employee ID. Please try again.");
					continue;
				}

				String storedHash = rs.getString("empPassword");

				if (!BCrypt.checkpw(password, storedHash)) {
					logger.warn("Invalid password. Please try again.");
					continue;
				}
				List<String> roles = fetchRoles(conn, empId);
				PasswordMethods.setLoginContext(empId, roles);

				logger.info("Login successful!");
				return true;

			} catch (Exception e) {
				logger.warn("Login Failed. " + e.getMessage());
			}
		}
	}

	private static List<String> fetchRoles(Connection conn, String empId) throws Exception {

		List<String> roles = new ArrayList<>();

		String roleQuery = "SELECT r.role FROM roles r INNER JOIN employees e on r.empid=e.empid WHERE r.empid = ? AND e.active IS TRUE";
		PreparedStatement ps = conn.prepareStatement(roleQuery);
		ps.setString(1, empId);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			roles.add(rs.getString("role"));
		}
		return roles;
	}
}
