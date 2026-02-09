package dao;

import java.sql.SQLException;
//import manager.Manage;
import java.util.ArrayList;
import java.util.List;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import customExceptions.InvalidDataException;
import enums.RoleChoice;
import model.Employee;

public interface CrudInterface {
	
	Employee add(String name, String mail, String address, String department, ArrayList<String> role, String password)
			throws InvalidDataException;

	void delete(String id) throws EmployeeNotFoundException, IdFormatWrongException;

	void updateName(String id, String name) throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException;

	void updateMail(String id, String mail) throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException;

	void updateAddress(String id, String address) throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException;

	void updateDepartment(String id, String department) throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException;

	void addRole(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException;

	void revokeRole(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException;

	void updatePassword(String loggedInid, String password)
			throws InvalidDataException, IdFormatWrongException, EmployeeNotFoundException ;

	List<Employee> showAll() throws EmployeeNotFoundException, IdFormatWrongException;

	Employee showOne(String id) throws EmployeeNotFoundException, IdFormatWrongException;
	
	Employee showSelf(String id) throws EmployeeNotFoundException;
	

	String addDB(String name,String mail,String address,String department, RoleChoice role) throws SQLException;
	void updateNameDB(String id, String name);
	void updateMailDB(String id, String mail);
	void updateDepartmentDB(String id, String department);
	void updateAddressDB(String id, String address);
	void addRoleDB(String id, String role);
	void deleteDB(String id);
	void updatePasswordDB(String id, String password);
	void revokeRoleDB(String id, String role);
	
}