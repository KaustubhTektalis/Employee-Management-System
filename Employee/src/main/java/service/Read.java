package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import model.Employee;

public class Read {

	// currently only for db, file reading operations in employeelistOps.java

	private static final Logger logger = LogManager.getLogger(Read.class);

	public static List<Employee> readAll(Connection conn) {
		List<Employee> list = new ArrayList<>();

		try {
			String query = """
					    SELECT empid, empname, empmail, empaddress, empdepartment
					    FROM employees WHERE active IS TRUE
					""";

			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				list.add(new Employee(rs.getString("empid"), rs.getString("empname"), rs.getString("empmail"),
						rs.getString("empaddress"), rs.getString("empdepartment"), new ArrayList<>(), null));
			}
			logger.info("Employees data fetched");
		} catch (Exception e) {
			logger.error("Error fetching all employees: {}", e.getMessage(), e);
		}

		return list;
	}

	public static Employee readOne(Connection conn, String id) {
		try {
			String query = """
					    SELECT empid, empname, empmail, empaddress, empdepartment
					    FROM employees
					    WHERE empid = ? AND active IS TRUE
					""";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, id);

			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				logger.warn("Employee with ID {} not found", id);
				return null;
			}
			logger.info("Fetched employee with ID {}", id);
			return new Employee(rs.getString("empid"), rs.getString("empname"), rs.getString("empmail"),
					rs.getString("empaddress"), rs.getString("empdepartment"), new ArrayList<>(), null);

		} catch (Exception e) {
			logger.error("Error fetching self details for employee {}", id, e);
			return null;
		}
	}

	public static Employee readSelf(Connection conn, String id) {
		try {

			ArrayList<String> roles = new ArrayList<>();
			String roleQuery = "SELECT role FROM roles WHERE empid = ? AND active IS TRUE";
			try (PreparedStatement ps = conn.prepareStatement(roleQuery)) {
				ps.setString(1, id);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					roles.add(rs.getString("role"));
				}
			}

			String empQuery = """
					    SELECT empid, empname, empmail, empaddress, empdepartment
					    FROM employees
					    WHERE empid = ? AND active IS TRUE
					""";

			PreparedStatement ps = conn.prepareStatement(empQuery);
			ps.setString(1, id);

			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				return null;
			}
			logger.info("Fetched self details for employee ID {}", id);

			return new Employee(rs.getString("empid"), rs.getString("empname"), rs.getString("empmail"),
					rs.getString("empaddress"), rs.getString("empdepartment"), roles, null);

		} catch (Exception e) {
			logger.error("Error fetching self details for employee {}", id, e);
			return null;
		}
	}

}