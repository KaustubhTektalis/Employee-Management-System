package service;

import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import dao.CrudDBImplementation;
import dao.CrudFileImplementation;

import java.util.List;

import model.Employee;
import util.ValidateId;

public class Read {


	private static final Logger logger = LoggerFactory.getLogger(Read.class);

	public static List<Employee> handleReadAll(CrudFileImplementation ops) throws EmployeeNotFoundException {
		List<Employee> employees = ops.readAll();
		if (employees == null || employees.isEmpty()) {
			logger.warn("No employees found in the system");
			throw new EmployeeNotFoundException("No employees found");
		}
		System.out.println("Employees data fetched");
		logger.info("Employees data fetched");
		return employees;
	}

	public static Employee handleReadOne(CrudFileImplementation ops, String id)
			throws EmployeeNotFoundException, IdFormatWrongException {
		ValidateId.validateId(id);
		Employee emp = ops.readOne(id);
		if (emp == null)
			throw new EmployeeNotFoundException("Employee not found");
		logger.info("Error fetching all employees: {}");
		return emp;
	}

//--------------------------------------------------------------------------------------------------------------------------

	public static List<Employee> handleReadAllDB(CrudDBImplementation dbops, Connection conn)
			throws EmployeeNotFoundException, IdFormatWrongException, SQLException {
		List<Employee> list = dbops.readAllDB();
		if (list == null || list.isEmpty()) {
			throw new EmployeeNotFoundException("No active employees found in DB");
		}
		return list;
	}

	public static Employee handleReadOneDB(CrudDBImplementation dbops, Connection conn, String id)
			throws EmployeeNotFoundException, IdFormatWrongException, SQLException {
		Employee emp = dbops.readOneDB(id);
		if (emp == null) {
			throw new EmployeeNotFoundException("Employee not found in DB");
		}
		return emp;
	}

	public static List<Employee> handleReadInactiveDB(CrudDBImplementation dbops, Connection conn)
			throws EmployeeNotFoundException, IdFormatWrongException, SQLException {
		List<Employee> list = dbops.readAllInactiveDB();
		if (list == null || list.isEmpty()) {
			throw new EmployeeNotFoundException("No active employees found in DB");
		}
		return list;
	}

}