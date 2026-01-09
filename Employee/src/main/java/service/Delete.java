package service;

import java.io.File;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import customExceptions.EmployeeNotFoundException;
import customExceptions.IdFormatWrongException;
import dao.CrudOps;
import dao.SaveEmployeesToFile;

public class Delete {
	public static void handleDelete(CrudOps ops, Scanner sc, ObjectMapper mapper, File file)
			throws EmployeeNotFoundException, IdFormatWrongException {

		System.out.print("Enter employee ID to delete: ");
		String id = sc.nextLine();

		ops.delete(id);
		SaveEmployeesToFile.saveToJson(ops, mapper, file);
		ops.showAll().forEach(System.out::println);
	}

}
