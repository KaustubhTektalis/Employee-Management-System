package dao;

import java.util.ArrayList;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import customExceptions.InvalidDataException;
import enums.ChooseBackend;
import enums.RoleChoice;
import model.Employee;
import service.RoleTableDB;
import service.PasswordMethods;
import service.Read;
import service.SetNextID;
import util.ValidPassword;
import util.ValidateId;

public class CrudImplementation implements CrudInterface {

	private Connection conn;
	private final ChooseBackend backend;

	public CrudImplementation(ChooseBackend backend, Connection conn) {
		this.backend = backend;
		this.conn = conn;
	}

	private Employee getExistingEmployee(String id) throws IdFormatWrongException, EmployeeNotFoundException {
		ValidateId.validateId(id);

		Employee emp = EmployeeListOps.findById(id);

		if (emp == null)
			throw new EmployeeNotFoundException("Employee not found");

		return emp;
	}

	public boolean employeeExists(String id) throws IdFormatWrongException {
		ValidateId.validateId(id);
		return EmployeeListOps.findById(id) != null;
	}


	public Employee add(String name, String mail, String address, String department, ArrayList<String> role,
			String password) throws InvalidDataException {

		String id = SetNextID.generateNextId(EmployeeListOps.findAll());
		String hashedPassword = PasswordMethods.hash(password);

		Employee emp = new Employee(id, name, mail, address, department, role, hashedPassword);

		EmployeeListOps.save(emp);
		return emp;
	}

	public void delete(String id) throws EmployeeNotFoundException, IdFormatWrongException {
		ValidateId.validateId(id);

		Employee emp = EmployeeListOps.findById(id);
		if (emp == null)
			throw new EmployeeNotFoundException("Employee not found");

		EmployeeListOps.delete(emp);
	}

	public List<Employee> showAll() throws EmployeeNotFoundException {

	    if (backend == ChooseBackend.FILE) {
	        if (EmployeeListOps.isEmpty())
	            throw new EmployeeNotFoundException("No employees");
	        return EmployeeListOps.findAll();
	    }

	    List<Employee> list = Read.readAll(conn);
	    if (list.isEmpty())
	        throw new EmployeeNotFoundException("No employees");

	    return list;
	}

	public Employee showOne(String id) throws EmployeeNotFoundException {

		if (backend == ChooseBackend.FILE) {
            Employee emp = EmployeeListOps.findById(id);
            if (emp == null)
                throw new EmployeeNotFoundException("Employee not found");
            return emp;
        }
        return Read.readOne(conn, id);
    }
	
	public Employee showSelf(String id) throws EmployeeNotFoundException {
		if (backend == ChooseBackend.FILE) {
            Employee emp = EmployeeListOps.findById(id);
            if (emp == null)
                throw new EmployeeNotFoundException("Employee not found");
            return emp;
        }
        return Read.readSelf(conn, id);
	}
	
	public void updateName(String id, String name) throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
		getExistingEmployee(id).setName(name);
	}

	public void updateMail(String id, String mail) throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
		getExistingEmployee(id).setMail(mail);
	}

	public void updateAddress(String id, String address) throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
		getExistingEmployee(id).setAddress(address);
	}

	public void updateDepartment(String id, String department)throws EmployeeNotFoundException, IdFormatWrongException {
		getExistingEmployee(id).setDepartment(department);
	}

	public void addRole(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException {
		Employee emp = getExistingEmployee(id);
		emp.addRole(role);
	}

	public void revokeRole(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException {
		ValidateId.validateId(id);
		Employee emp = getExistingEmployee(id);
		emp.removeRole(role);
	}

	public void updatePassword(String id, String newPassword)
			throws InvalidDataException, IdFormatWrongException, EmployeeNotFoundException {

		if (!ValidPassword.isValidPassword(newPassword))
			throw new InvalidDataException(
					"Password must contain at least one uppercase letter, one number, and one special character.");

		getExistingEmployee(id).setPassword(PasswordMethods.hash(newPassword));
	}

//	---------------------------------------------------------------------------------------------------
	
	
	
	public boolean employeeExistsDB(String id) {
		String query="SELECT 1 FROM employees WHERE empid=? AND active IS TRUE";
		try{
			PreparedStatement ps=conn.prepareStatement(query);
			ps.setString(1, id);
			ResultSet rs=ps.executeQuery();
			return rs.next();
		}
		catch(SQLException e) {
			return false;
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

	public void deleteDB(String id) {
		try {
//			String deletePassword = "DELETE FROM passwords WHERE empId = ?";
//			PreparedStatement psPass = conn.prepareStatement(deletePassword);
//			psPass.setString(1, id);
//
//			String deleteRoles = "DELETE FROM roles WHERE empId = ?";
//			PreparedStatement psRoles = conn.prepareStatement(deleteRoles);
//			psRoles.setString(1, id);

			String deleteEmployee = "UPDATE employees SET active = FALSE WHERE empid = ?";
			PreparedStatement psEmp = conn.prepareStatement(deleteEmployee);
			psEmp.setString(1, id);

//			psPass.executeUpdate();
//			psRoles.executeUpdate();
			
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

	public void updateNameDB(String id, String name) {
		try {
			String query = "UPDATE employees SET empName = ? WHERE empId = ? AND active IS TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, id);

			if (ps.executeUpdate() > 0) {
				System.out.println("Name of Employee " + id + " updated!");
			}

			Read.readOne(conn, id);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void updateMailDB(String id, String mail) {
		try {
			String query = "UPDATE employees SET empMail = ? WHERE empId = ? AND active IS TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, mail);
			ps.setString(2, id);

			if (ps.executeUpdate() > 0) {
				System.out.println("Mail of Employee " + id + " updated!");
			}

			Read.readOne(conn, id);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void updateAddressDB(String id, String address) {
		try {
			String query = "UPDATE employees SET empAddress = ? WHERE empId = ? AND active IS TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, address);
			ps.setString(2, id);

			if (ps.executeUpdate() > 0) {
				System.out.println("Address of Employee " + id + " updated!");
			}

			Read.readOne(conn, id);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void updateDepartmentDB(String id, String department) {
		try {
			String query = "UPDATE employees SET empDepartment = ? WHERE empId = ? AND active IS TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, department);
			ps.setString(2, id);

			if (ps.executeUpdate() > 0) {
				System.out.println("Department of Employee " + id + " updated!");
			}

			Read.readOne(conn, id);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void addRoleDB(String id, String role) {
		try {
			String query = "INSERT INTO roles (empid, role, active) SELECT e.empid, ?, "
					+ "TRUE FROM employees e WHERE e.empid = ? AND e.active = TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, id);
			ps.setString(2, role);

			if (ps.executeUpdate() > 0) {
				System.out.println("Role "+role +" added to the Employee " + id );
			}

			Read.readOne(conn, id);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void revokeRoleDB(String id, String role) {
		try {
			String query = "UPDATE roles r INNER JOIN employees e ON r.empid = e.empid "
					+ "SET r.active = FALSE WHERE r.empid = ? AND r.role = ? AND e.active = TRUE AND r.active = TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, id);
	        ps.setString(2, role);

			if (ps.executeUpdate() > 0) {
				System.out.println("Role revoked for Employee " + id);
			} else {
				System.out.println("Employee not found with id: " + id);
			}

			Read.readOne(conn, id);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void updatePasswordDB(String id, String password) {
		try {
			String hashedPassword = PasswordMethods.hash(password);

			String query = "UPDATE passwords p INNER JOIN employees e ON p.empid = e.empid "
					+ "SET p.empPassword = ? WHERE p.empid = ? AND e.active = TRUE";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, hashedPassword);
			ps.setString(2, id);

			int rowsUpdated = ps.executeUpdate();

			if (rowsUpdated > 0) {
				System.out.println("Password updated for employee " + id);
			} else {
				System.out.println("Employee not found: " + id);
			}

		} catch (SQLException e) {
			System.out.println("Failed to update password: " + e.getMessage());
		}
	}
}
