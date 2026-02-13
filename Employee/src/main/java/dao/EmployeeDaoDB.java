package dao;

import java.sql.SQLException;
import java.util.List;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import enums.RoleChoice;
import model.Employee;

public interface EmployeeDaoDB {
	String addDB(String name, String mail, String address, String department, RoleChoice role) throws SQLException;

	void updateNameDB(String id, String name) throws EmployeeNotFoundException, IdFormatWrongException;

	void updateMailDB(String id, String mail) throws EmployeeNotFoundException, IdFormatWrongException;

	void updateDepartmentDB(String id, String department) throws EmployeeNotFoundException, IdFormatWrongException;

	void updateAddressDB(String id, String address) throws EmployeeNotFoundException, IdFormatWrongException;

	void addRoleDB(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException;

	void deleteDB(String id) throws EmployeeNotFoundException, IdFormatWrongException;

	void updatePasswordDB(String id, String password) throws EmployeeNotFoundException, IdFormatWrongException;

	void revokeRoleDB(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException;

	List<Employee> readAllDB() throws EmployeeNotFoundException, IdFormatWrongException, SQLException;

	Employee readOneDB(String id) throws EmployeeNotFoundException, IdFormatWrongException, SQLException;

}
