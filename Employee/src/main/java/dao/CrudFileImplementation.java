package dao;

import java.util.ArrayList;

import java.util.List;
import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import customExceptions.InvalidDataException;
import model.Employee;
import service.PasswordMethods;
import util.SetNextID;
import util.ValidateId;

public class CrudFileImplementation implements EmployeeDaoFile {

	public boolean employeeExists(String id) throws IdFormatWrongException {
		ValidateId.validateId(id);
		return EmployeeListOps.findById(id) != null;
	}

	public Employee add(String name, String mail, String address, String department, ArrayList<String> role,
			String password) throws InvalidDataException {

		String id = SetNextID.generateNextId(EmployeeListOps.findAll());
		String hashedPassword = PasswordMethods.hash(password);

		Employee emp = new Employee(id, name, mail, address, department, role, hashedPassword);
		EmployeeListOps.add(emp);
		return emp;
	}

	public void delete(String id) throws EmployeeNotFoundException, IdFormatWrongException {
		ValidateId.validateId(id);

		Employee emp = EmployeeListOps.findById(id);
		if (emp == null)
			throw new EmployeeNotFoundException("Employee not found");

		EmployeeListOps.delete(emp);
	}

	public List<Employee> readAll() throws EmployeeNotFoundException {
		return EmployeeListOps.findAll();
	}

	public Employee readOne(String id) throws EmployeeNotFoundException {
		return EmployeeListOps.findById(id);
	}

	public void updateName(String id, String name)
			throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
		EmployeeListOps.findById(id).setName(name);

	}

	public void updateMail(String id, String mail)
			throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
		EmployeeListOps.findById(id).setMail(mail);

	}

	public void updateAddress(String id, String address)
			throws EmployeeNotFoundException, IdFormatWrongException, InvalidDataException {
		EmployeeListOps.findById(id).setAddress(address);

	}

	public void updateDepartment(String id, String department)
			throws EmployeeNotFoundException, IdFormatWrongException {
		EmployeeListOps.findById(id).setDepartment(department);

	}

	public void updatePassword(String id, String newPassword)
			throws InvalidDataException, IdFormatWrongException, EmployeeNotFoundException {

		EmployeeListOps.findById(id).setPassword(PasswordMethods.hash(newPassword));
	}

	public void addRole(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException {
		EmployeeListOps.findById(id).addRole(role);

	}

	public void revokeRole(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException {
		EmployeeListOps.findById(id).removeRole(role);

	}
}
