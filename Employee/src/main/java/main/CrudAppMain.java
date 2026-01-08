package main;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import controller.Login;
import dao.EmployeeRepository;
import model.Employee;
import service.AccessAndPasswordMethods;
import service.Manage;

public class CrudAppMain {

    private static final String FILE_PATH = "employees_data.json";

    public static void main(String[] args) {
    	
//    	if(args.length==0) {
//    		return;
//    	}

        Scanner sc = new Scanner(System.in);

        EmployeeRepository repository = new EmployeeRepository();
        Manage ops = new Manage(repository);

        ObjectMapper mapper = new ObjectMapper();
        File file = new File(FILE_PATH);

        try {

            if (file.exists() && file.length() > 0) {
                List<Employee> list =
                        mapper.readValue(file,
                                new TypeReference<List<Employee>>() {});
                ops.setEmployees(list);
            }

            Login.authenticate(ops, sc);

            String loggedInId =
                    AccessAndPasswordMethods.getLoggedInAdminId();

            Employee loggedInEmployee =
                    ops.showOne(loggedInId);

            if (loggedInEmployee.getRole().contains("Admin")) {
                AdminMenu.showMenu(ops, sc, mapper, file);

            } else if (loggedInEmployee.getRole().contains("Manager")) {
                ManagerMenu.showMenu(ops, sc, mapper, file);

            } else {
                EmployeeMenu.showMenu(ops, sc);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        } finally {
            sc.close();
        }
    }
}
