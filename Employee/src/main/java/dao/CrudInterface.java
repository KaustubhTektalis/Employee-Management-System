package dao;

//import manager.Manage;
import java.util.ArrayList;
import java.util.List;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import customExceptions.InvalidDataException;
import model.Employee;

public interface CrudInterface {
	Employee add(String name, String mail, String address, String department, ArrayList<String> role, String password)
			throws InvalidDataException;

	void delete(String id) throws EmployeeNotFoundException, IdFormatWrongException;

	void updateName(String id, String name) throws EmployeeNotFoundException, IdFormatWrongException;

	void updateMail(String id, String mail) throws EmployeeNotFoundException, IdFormatWrongException;

	void updateAddress(String id, String address) throws EmployeeNotFoundException, IdFormatWrongException;

	void updateDepartment(String id, String department) throws EmployeeNotFoundException, IdFormatWrongException;

	void addRole(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException;

	void revokeRole(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException;

	void updatePassword(String loggedInid, String password)
			throws InvalidDataException,IdFormatWrongException, EmployeeNotFoundException ;

	List<Employee> showAll() throws EmployeeNotFoundException, IdFormatWrongException;

	Employee showOne(String id) throws EmployeeNotFoundException, IdFormatWrongException;
}