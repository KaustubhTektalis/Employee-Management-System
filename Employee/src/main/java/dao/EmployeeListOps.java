package dao;

//Methods use the Employees arrayList

import java.util.ArrayList;
import java.util.List;

import model.Employee;

public class EmployeeListOps {

	private final static List<Employee> employees = new ArrayList<>();

	public static void setEmployees(List<Employee> list) {
		employees.clear();
		employees.addAll(list);
	}

	public static List<Employee> findAll() {
		return new ArrayList<>(employees);
	}

	public static Employee findById(String id) {
		for (Employee e : employees) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}

	public static void save(Employee employee) {
		employees.add(employee);
	}

	public static void delete(Employee employee) {
		employees.remove(employee);
	}

	public static boolean isEmpty() {
		return employees.isEmpty();
	}
}
