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
		String query = "SELECT 1 FROM employees WHERE empid = ? AND active IS TRUE";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			throw new SQLException("Failed to check employee", e);
		}
	}

	public String addDB(String name, String mail, String address, String department, RoleChoice role)
			throws SQLException {
		String generatedEmpId = null;
		conn.setAutoCommit(false);

		String query = "INSERT INTO employees (empName, empMail, empAddress, empDepartment) " + "VALUES (?, ?, ?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(query, new String[] { "empid" })) {
			ps.setString(1, name);
			ps.setString(2, mail);
			ps.setString(3, address);
			ps.setString(4, department);
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					generatedEmpId = rs.getString("empid");
				}
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
		} catch (SQLException e) {
			throw new SQLException("Failed to fetch employees from database", e);
		}
		if (list.isEmpty()) {
			throw new EmployeeNotFoundException("No active employees found");
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

				Employee emp = new Employee(rs.getString("empid"), rs.getString("empname"), rs.getString("empmail"),
						rs.getString("empaddress"), rs.getString("empdepartment"), new ArrayList<>(), null);

				return emp;
			}
		}
	}
	
//	public List<String> readRolesDB(String id) throws SQLException {
//		List<String> roles = new ArrayList<>();
//
//		String roleQuery = "SELECT role FROM roles WHERE empid = ? AND active IS TRUE";
//
//		try (PreparedStatement rolePs = conn.prepareStatement(roleQuery)) {
//			rolePs.setString(1, id);
//
//			try (ResultSet roleRs = rolePs.executeQuery()) {
//				while (roleRs.next()) {
//					roles.add(roleRs.getString("role"));
//				}
//			}
//		}
//		return roles;
//	}

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
		} catch (SQLException e) {
			throw new SQLException("Failed to fetch employees from database", e);
		}
		if (list.isEmpty()) {
			throw new EmployeeNotFoundException("No inactive employees found");
		}
		return list;
	}

	public boolean deleteDB(String id) throws SQLException {
		String deleteEmployee = "UPDATE employees SET active = FALSE WHERE empid = ? AND active= TRUE";
		try (PreparedStatement psEmp = conn.prepareStatement(deleteEmployee)) {
			psEmp.setString(1, id);

			int rowsDeleted = psEmp.executeUpdate();
			return rowsDeleted > 0;
		} catch (SQLException e) {
			throw new SQLException("Failed to delete employee with id: " + id, e);
		}
	}

	public boolean updateNameDB(String id, String name) throws SQLException {
		String query = "UPDATE employees SET empName = ? WHERE empId = ? AND active IS TRUE";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, name);
			ps.setString(2, id);

			int rowsUpdated = ps.executeUpdate();
			return rowsUpdated > 0;

		} catch (SQLException e) {
			throw new SQLException("Failed to update name for employee: " + id, e);
		}
	}

	public boolean updateMailDB(String id, String mail) throws SQLException {
		String query = "UPDATE employees SET empMail = ? WHERE empId = ? AND active IS TRUE";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, mail);
			ps.setString(2, id);

			int rowsUpdated = ps.executeUpdate();
			return rowsUpdated > 0;

		} catch (SQLException e) {
			throw new SQLException("Failed to update mail for employee: " + id, e);
		}

	}

	public boolean updateAddressDB(String id, String address) throws SQLException {
		String query = "UPDATE employees SET empAddress = ? WHERE empId = ? AND active IS TRUE";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, address);
			ps.setString(2, id);

			int rowsUpdated = ps.executeUpdate();
			return rowsUpdated > 0;

		} catch (SQLException e) {
			throw new SQLException("Failed to update address for employee: " + id, e);
		}

	}

	public boolean updateDepartmentDB(String id, String department) throws SQLException {
		String query = "UPDATE employees SET empDepartment = ? WHERE empId = ? AND active IS TRUE";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, department);
			ps.setString(2, id);
			int rowsUpdated = ps.executeUpdate();
			return rowsUpdated > 0;

		} catch (SQLException e) {
			throw new SQLException("Failed to update department for employee: " + id, e);
		}
	}

	public boolean updatePasswordDB(String id, String password) throws SQLException {
		String hashedPassword = PasswordMethods.hash(password);

		String query = "UPDATE passwords p SET empPassword = ? FROM employees e "
				+ "WHERE p.empid = e.empid AND p.empid =? AND e.active = TRUE";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, hashedPassword);
			ps.setString(2, id);

			int rowsUpdated = ps.executeUpdate();
			return rowsUpdated > 0;

		} catch (SQLException e) {
			throw new SQLException("Failed to update password for employee: " + id, e);
		}
	}

	public boolean addRoleDB(String id, String role) throws SQLException {
		String empCheck = "SELECT active FROM employees WHERE empid = ?";
		try (PreparedStatement ps = conn.prepareStatement(empCheck)) {
			ps.setString(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next() || !rs.getBoolean("active")) {
					return false;
				}
			}
		}

//		String roleCheck = "SELECT 1 FROM roles WHERE empid = ? AND role = ? AND active = ?";
//		try (PreparedStatement ps = conn.prepareStatement(roleCheck)) {
//			ps.setString(1, id);
//			ps.setString(2, role);
//			ps.setBoolean(3, true);
//
//			try (ResultSet rs = ps.executeQuery()) {
//				if (rs.next()) {
//					return false;
//				}
//			}
//		}

		String insert = "INSERT INTO roles (empid, role, active) " +
			    "VALUES (?, ?, ?) " +
			    "ON CONFLICT (empid, role) " +
			    "DO UPDATE SET active = TRUE " +
			    "WHERE roles.active = FALSE;";
		try (PreparedStatement ps = conn.prepareStatement(insert)) {
			ps.setString(1, id);
			ps.setString(2, role);
			ps.setBoolean(3, true);

			return ps.executeUpdate() > 0;
		}
	}

	public boolean revokeRoleDB(String id, String role) throws SQLException {

		String update = "UPDATE roles SET active = ? " + "WHERE empid = ? AND role = ? AND active = ?";

		try (PreparedStatement ps = conn.prepareStatement(update)) {
			ps.setBoolean(1, false);
			ps.setString(2, id);
			ps.setString(3, role);
			ps.setBoolean(4, true);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
