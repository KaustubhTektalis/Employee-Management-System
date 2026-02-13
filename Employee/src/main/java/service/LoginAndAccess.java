package service;

import java.io.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dao.CrudFileImplementation;
import dao.EmployeeListOps;
import model.Employee;

public class LoginAndAccess {
	private static final Logger logger = LoggerFactory.getLogger(LoginAndAccess.class);
	private static String loggedInID;
	private static List<String> loggedInRoles = new ArrayList<>();

	private LoginAndAccess() {
	}
	//------------------------------------------------------------------------------------------------------- 
	
	public static void setLoginContext(String empID, List<String> roles) {
		loggedInID = empID;
		loggedInRoles.clear();
		if (roles != null) {
			loggedInRoles.addAll(roles);
		}
	}

	public static String getLoggedInId() {
		return loggedInID;
	}

	public static List<String> getLoggedInRoles() {
		return Collections.unmodifiableList(loggedInRoles);
	}

	public static boolean hasRole(String role) {
		return loggedInRoles.stream().anyMatch(r -> r.equalsIgnoreCase(role));
	}
	

	public static void clearLoginContext() {
		loggedInID=null;
		loggedInRoles.clear();
	}
	//----------------------------------------------------------------------------------------------------
	
	public static void authenticateInFile(CrudFileImplementation ops, Scanner sc) {

		Console console = System.console();
		int attempts=0;
		final int max_attempts=3;

		while (attempts<max_attempts) {
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
				setLoginContext(emp.getId(), emp.getRole());
				System.out.println("Login successful. Employee ID: "+emp.getId()+", Role: "+emp.getRole());
				logger.info("Login successful. Employee ID: {}, Role: {}", emp.getId(), emp.getRole());
				return;
			}
			attempts++;
		}
	    System.out.println("Maximum login attempts exceeded. Exiting program.");
	    logger.error("Exiting program. Max login attempts reached.");
	    System.exit(0);
	}

	
	private static Employee accessCheckFile(CrudFileImplementation ops, String empID, String pass) {
		for (Employee e : EmployeeListOps.findAll()) {
			if (e.getId().equals(empID)) {
				if(BCrypt.checkpw(pass, e.getPassword())) {
				return e;
				}
				else {
					System.out.println("Invalid password. Please try again.");
					logger.warn("Invalid password. Please try again.");
		                return null;
				}
			}
		}
		logger.warn("Invalid Employee ID. Please try again.");
		System.out.println("Inavlid Employee ID. Please try again");
		return null;
	}
	
	
	//------------------------------------------------------------------------------------------------------

	public static boolean authenticateInDB(Connection conn, Scanner sc) {
		
		int attempts = 0;
	    final int max_attempts = 3;
	    
		while (attempts<max_attempts) {
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
					attempts++;
					logger.warn("Invalid Employee ID. Please try again.");
					System.out.println("Inavlid Employee ID. Please try again");
					continue;
				}

				String storedHash = rs.getString("empPassword");

				if (!BCrypt.checkpw(password, storedHash)) {
					attempts++;
					System.out.println("Invalid password. Please try again.");
					logger.warn("Invalid password. Please try again.");
					continue;
				}
				List<String> roles = fetchRoles(conn, empId);
				setLoginContext(empId, roles);

				logger.info("Login successful. Employee ID: {}, Roles: {}", empId, String.join(", ", roles));
				return true;

			} catch (Exception e) {
				attempts++;
				System.out.println("Login failed. Try again");
				logger.warn("Login Failed. " + e.getMessage());
			}
		}
		System.out.println("Maximum login attempts exceeded. Exiting program.");
		logger.error("Maximum login attempts exceeded. Exiting program.");
		System.exit(0);
		return false;
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
