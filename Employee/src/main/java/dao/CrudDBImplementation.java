package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import enums.RoleChoice;
import model.Employee;
import service.PasswordMethods;
import service.RoleTableDB;

public class CrudDBImplementation implements EmployeeDaoDB {

	private Connection conn;

	public CrudDBImplementation(Connection conn) {
		this.conn = conn;
	}

	public boolean employeeExistsDB(String id) throws SQLException {
		String query = "SELECT 1 FROM employees WHERE empid=? AND active IS TRUE";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			throw new SQLException("Failed to check Employee");
		}
	}

	public String addDB(String name, String mail, String address, String department, RoleChoice role)
			throws SQLException {
		String generatedEmpId = null;
		try {
			conn.setAutoCommit(false);

			String query = "INSERT INTO employees (empName, empMail, empAddress, empDepartment) "
					+ "VALUES (?, ?, ?, ?)";

			PreparedStatement ps = conn.prepareStatement(query, new String[] { "empid" });
			ps.setString(1, name);
			ps.setString(2, mail);
			ps.setString(3, address);
			ps.setString(4, department);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				generatedEmpId = rs.getString("empid");
			}

			RoleTableDB.insertRole(conn, generatedEmpId, role);
			conn.commit();
			return generatedEmpId;
		} catch (SQLException e) {
			conn.rollback();
			throw e;
		} finally {
			conn.setAutoCommit(true);
		}

	}

	public List<Employee> readAllDB() throws EmployeeNotFoundException, IdFormatWrongException, SQLException {
		List<Employee> list = new ArrayList<>();
		String query = """
				    SELECT empid, empname, empmail, empaddress, empdepartment
				    FROM employees WHERE active IS TRUE
				""";

		try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				list.add(new Employee(rs.getString("empid"), rs.getString("empname"), rs.getString("empmail"),
						rs.getString("empaddress"), rs.getString("empdepartment"), new ArrayList<>(), null));
			}
		}
		return list;
	}

	public Employee readOneDB(String id) throws EmployeeNotFoundException, IdFormatWrongException, SQLException {
		String query = """
				    SELECT empid, empname, empmail, empaddress, empdepartment
				    FROM employees
				    WHERE empid = ? AND active IS TRUE
				""";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					return null;
				}

				return new Employee(rs.getString("empid"), rs.getString("empname"), rs.getString("empmail"),
						rs.getString("empaddress"), rs.getString("empdepartment"), new ArrayList<>(), null);
			}
		}
	}

	public List<Employee> readAllInactiveDB() throws EmployeeNotFoundException, IdFormatWrongException, SQLException {
		List<Employee> list = new ArrayList<>();
		String query = """
				    SELECT empid, empname, empmail, empaddress, empdepartment
				    FROM employees WHERE active IS FALSE;
				""";

		try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				list.add(new Employee(rs.getString("empid"), rs.getString("empname"), rs.getString("empmail"),
						rs.getString("empaddress"), rs.getString("empdepartment"), new ArrayList<>(), null));
			}
		}
		return list;
	}

	public void deleteDB(String id) {
		try {

			String deleteEmployee = "UPDATE employees SET active = FALSE WHERE empid = ? AND active= TRUE";
			PreparedStatement psEmp = conn.prepareStatement(deleteEmployee);
			psEmp.setString(1, id);

			int rowsDeleted = psEmp.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Employee " + id + " deleted from database.");
			} else {
				System.out.println("No employee found with ID: " + id);
			}

		} catch (SQLException e) {
			System.out.println("Error deleting employee: " + e.getMessage());
		}
	}

	public void updateNameDB(String id, String name) throws EmployeeNotFoundException, IdFormatWrongException {
		try {
			String query = "UPDATE employees SET empName = ? WHERE empId = ? AND active IS TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, id);

			if (ps.executeUpdate() > 0) {
				System.out.println("Name of Employee " + id + " updated!");
			}
//			Read.handleReadOneDB(ops, conn, id);

		} catch (SQLException e) {
			System.out.println("Error updating Name");
		}

	}

	public void updateMailDB(String id, String mail) throws EmployeeNotFoundException, IdFormatWrongException {
		try {
			String query = "UPDATE employees SET empMail = ? WHERE empId = ? AND active IS TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, mail);
			ps.setString(2, id);

			if (ps.executeUpdate() > 0) {
				System.out.println("Mail of Employee " + id + " updated!");
			}

//			Read.handleReadOneDB(ops, conn, id);

		} catch (SQLException e) {
			System.out.println("Error updating Mail");
		}

	}

	public void updateAddressDB(String id, String address) throws EmployeeNotFoundException, IdFormatWrongException {
		try {
			String query = "UPDATE employees SET empAddress = ? WHERE empId = ? AND active IS TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, address);
			ps.setString(2, id);

			if (ps.executeUpdate() > 0) {
				System.out.println("Address of Employee " + id + " updated!");
			}

//			Read.handleReadOneDB(ops, conn, id);

		} catch (SQLException e) {
			System.out.println("Error updating Address");
		}

	}

	public void updateDepartmentDB(String id, String department)
			throws EmployeeNotFoundException, IdFormatWrongException {
		try {
			String query = "UPDATE employees SET empDepartment = ? WHERE empId = ? AND active IS TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, department);
			ps.setString(2, id);

			if (ps.executeUpdate() > 0) {
				System.out.println("Department of Employee " + id + " updated!");
			}

//			Read.handleReadOneDB(ops, conn, id);

		} catch (SQLException e) {
			System.out.println("Error updating Department");
		}
	}

	public void updatePasswordDB(String id, String password) {
		try {
			String hashedPassword = PasswordMethods.hash(password);

			String query = "UPDATE passwords p SET empPassword = ? FROM employees e "
					+ "WHERE p.empid = e.empid AND p.empid =? AND e.active = TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, hashedPassword);
			ps.setString(2, id);

			if (ps.executeUpdate() > 0) {
				System.out.println("Password updated for employee " + id);
			} else {
				System.out.println("Employee not found: " + id);
			}

		} catch (SQLException e) {
			System.out.println("Failed to update password: " + e.getMessage());
		}
	}

	public void addRoleDB(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException {
		try {
			String query = "INSERT INTO roles (empid, role, active) SELECT e.empid, ?, "
					+ "TRUE FROM employees e WHERE e.empid = ? AND e.active = TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, role);
			ps.setString(2, id);

			if (ps.executeUpdate() > 0) {
				System.out.println("Role " + role + " added to the Employee " + id);
			}
//			Read.handleReadOneDB(ops, conn, id);
		} catch (SQLException e) {
			System.out.println("Role already exists for the employee");
		}
	}

	public void revokeRoleDB(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException {
		try {
			String query = "UPDATE roles r INNER JOIN employees e ON r.empid = e.empid "
					+ "SET r.active = FALSE WHERE r.empid = ? AND r.role = ? AND e.active = TRUE AND r.active = TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, id);
			ps.setString(2, role);

			if (ps.executeUpdate() > 0) {
				System.out.println("Role " + role + " revoked from Employee " + id);
			}
//			Read.handleReadOneDB(ops, conn, id);

		} catch (SQLException e) {
			System.out.println("Selected role doesn't previously exist for the employee");
		}

	}

}
