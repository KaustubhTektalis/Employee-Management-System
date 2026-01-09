package dao;

import java.util.ArrayList;

import java.util.List;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import customExceptions.InvalidDataException;
import model.Employee;
import service.PasswordHasher;
import service.SetNextID;
import util.ValidPassword;
import util.ValidateId;

public class CrudOps implements CrudInterface {

	private final EmployeeListOps empList;

	public CrudOps(EmployeeListOps empList) {
		this.empList = empList;
	}
	private Employee getExistingEmployee(String id) throws IdFormatWrongException, EmployeeNotFoundException {
		ValidateId.validateId(id);

		Employee emp = empList.findById(id);
		if (emp == null)
			throw new EmployeeNotFoundException("Employee not found");

		return emp;
	}

	public boolean employeeExists(String id) throws IdFormatWrongException {
		ValidateId.validateId(id);
		return empList.findById(id) != null;
	}

	// implementing cruds

	public Employee add(String name, String mail, String address, String department, ArrayList<String> role,
			String password) throws InvalidDataException {

		if (name == null || name.trim().isEmpty())
			throw new InvalidDataException("Name cannot be empty");

		if (mail == null || !mail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
			throw new InvalidDataException("Invalid email format");

		if (department == null || department.trim().isEmpty())
			throw new InvalidDataException("Department cannot be empty");

		String id = SetNextID.generateNextId(EmployeeListOps.findAll());
		String hashedPassword = PasswordHasher.hash(password);

		Employee emp = new Employee(id, name, mail, address, department, role, hashedPassword);

		empList.save(emp);
		return emp;
	}

	public void delete(String id) throws EmployeeNotFoundException, IdFormatWrongException {
		ValidateId.validateId(id);

		Employee emp = empList.findById(id);
		if (emp == null)
			throw new EmployeeNotFoundException("Employee not found");

		empList.delete(emp);
	}

	public List<Employee> showAll() throws EmployeeNotFoundException {

		if (EmployeeListOps.isEmpty())
			throw new EmployeeNotFoundException("No employees to display");

		return EmployeeListOps.findAll();
	}

	public Employee showOne(String id) throws EmployeeNotFoundException {

		Employee emp = empList.findById(id);
		if (emp == null)
			throw new EmployeeNotFoundException("Employee not found");

		return emp;
	}

	public void updateName(String id, String name) throws EmployeeNotFoundException, IdFormatWrongException {

		getExistingEmployee(id).setName(name);
	}

	public void updateMail(String id, String mail) throws EmployeeNotFoundException, IdFormatWrongException {

		getExistingEmployee(id).setMail(mail);
	}

	public void updateAddress(String id, String address) throws EmployeeNotFoundException, IdFormatWrongException {

		getExistingEmployee(id).setAddress(address);
	}

	public void updateDepartment(String id, String department)
			throws EmployeeNotFoundException, IdFormatWrongException {

		getExistingEmployee(id).setDepartment(department);
	}

	public void addRole(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException {

		Employee emp = getExistingEmployee(id);

		if (!emp.getRole().contains(role)) {
			emp.getRole().add(role);
		}
	}

	public void revokeRole(String id, String role) throws EmployeeNotFoundException, IdFormatWrongException {
		ValidateId.validateId(id);

		getExistingEmployee(id).getRole().remove(role);
	}

	public void updatePassword(String id, String newPassword)
			throws InvalidDataException, IdFormatWrongException, EmployeeNotFoundException {

		if (!ValidPassword.isValidPassword(newPassword))
			throw new InvalidDataException(
					"Password must contain at least one uppercase letter, one number, and one special character.");

		getExistingEmployee(id).setPassword(PasswordHasher.hash(newPassword));
	}
}
