package service;

import java.util.ArrayList;
import java.util.List;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import customExceptions.InvalidDataException;
import dao.EmployeeRepository;
import model.Employee;
import util.PasswordHasher;

public class Manage {

    private final EmployeeRepository repository;

    public Manage(EmployeeRepository repository) {
        this.repository = repository;
    }

    public void setEmployees(List<Employee> list) {
        repository.setEmployees(list);
    }

    public List<Employee> getEmployees() {
        return repository.findAll();
    }
    private void validateId(String id) throws IdFormatWrongException {
        if (id == null || !id.matches("^TT\\d{5}$")) {
            throw new IdFormatWrongException("Invalid employee ID format");
        }
    }

    public void add(String name, String mail, String address,
                    String department, ArrayList<String> role,
                    String password) throws InvalidDataException {

        if (name == null || name.trim().isEmpty())
            throw new InvalidDataException("Name cannot be empty");

        if (mail == null || !mail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            throw new InvalidDataException("Invalid email format");

        if (department == null || department.trim().isEmpty())
            throw new InvalidDataException("Department cannot be empty");

        String id = SetNextID.generateNextId(repository.findAll());
        String hashedPassword = PasswordHasher.hash(password);

        Employee emp =
                new Employee(id, name, mail, address,
                        department, role, hashedPassword);

        repository.save(emp);
    }

    public void delete(String id) throws EmployeeNotFoundException , IdFormatWrongException{
    	 validateId(id);

        Employee emp = repository.findById(id);
        if (emp == null)
            throw new EmployeeNotFoundException("Employee not found");
        

        repository.delete(emp);
    }

    public List<Employee> showAll() throws EmployeeNotFoundException {

        if (repository.isEmpty())
            throw new EmployeeNotFoundException("No employees to display");

        return repository.findAll();
    }

    public Employee showOne(String id)
            throws EmployeeNotFoundException {

        Employee emp = repository.findById(id);
        if (emp == null)
            throw new EmployeeNotFoundException("Employee not found");

        return emp;
    }

    public void updateName(String id, String name)
            throws EmployeeNotFoundException , IdFormatWrongException{

    	 validateId(id);
        getExisting(id).setName(name);
    }

    public void updateMail(String id, String mail)
            throws EmployeeNotFoundException , IdFormatWrongException{
    	 validateId(id);

        getExisting(id).setMail(mail);
    }

    public void updateAddress(String id, String address)
            throws EmployeeNotFoundException, IdFormatWrongException {
    	 validateId(id);

        getExisting(id).setAddress(address);
    }

    public void updateDepartment(String id, String department)
            throws EmployeeNotFoundException, IdFormatWrongException {
    	 validateId(id);

        getExisting(id).setDepartment(department);
    }


    public void addRole(String id, String role)
            throws EmployeeNotFoundException , IdFormatWrongException{
    	 validateId(id);

        Employee emp = getExisting(id);

        if (!emp.getRole().contains(role)) {
            emp.getRole().add(role);
        }
    }

    public void revokeRole(String id, String role)
            throws EmployeeNotFoundException, IdFormatWrongException {
    	 validateId(id);
    	
        getExisting(id).getRole().remove(role);
    }
    
    public void updatePassword(String id, String newPassword)
            throws EmployeeNotFoundException {

        getExisting(id).setPassword(PasswordHasher.hash(newPassword));
    }

    public static String defaultPassword() {
        return "Default123";
    }

    private Employee getExisting(String id)
            throws EmployeeNotFoundException {

        Employee emp = repository.findById(id);
        if (emp == null)
            throw new EmployeeNotFoundException("Employee not found");

        return emp;
    }

    public boolean employeeExists(String id) throws IdFormatWrongException {
    	 validateId(id);
        return repository.findById(id) != null;
    }

    public boolean isEmpty() {
        return repository.isEmpty();
    }
}
