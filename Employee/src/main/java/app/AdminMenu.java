package app;

import java.io.File;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import manager.Manage;
import operations.*;

public class AdminCruds {

    public static void showMenu(Manage ops, Scanner sc,
                             ObjectMapper mapper, File file) throws Exception {

        AdminChoices choice = null;

        do {
            System.out.println("\n--- ADMIN MENU ---");
            for (AdminChoices c : AdminChoices.values()) {
                System.out.println(c);
            }
            System.out.println("Your choice: ");
            
            try {
                choice = AdminChoices.valueOf(sc.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid choice. Please select from the menu.");
                continue;
            }
            
            try {
            switch (choice) {

                case ADD:
                    AddEmployee.add(ops, sc);
                    SaveEmployeesToFile.saveToJson(ops, mapper, file);
                    break;

                case UPDATE:
                    UpdateEmployee.update(ops, sc);
                    SaveEmployeesToFile.saveToJson(ops, mapper, file);
                    break;

                case DELETE:
                    DeleteEmployee.delete(ops, sc);
                    SaveEmployeesToFile.saveToJson(ops, mapper, file);
                    break;

                case SHOW:
                	System.out.println("Enter 1 for your details");
                	System.out.println("Enter 2 for all employee Details");
                	System.out.println("Your choice: ");
                	int ch=sc.nextInt();
                	sc.nextLine();
                	switch(ch) {
                	case 1:
                		ShowEmployee.showOne(ops);
                        break;
                	case 2:
                		ShowEmployee.showAll(ops);
                        break;
                    default:
                    	break;
                	}
                	break;

                case CHANGE_PASSWORD:
                    LoginAndPassword.updatePassword(ops, sc);
                    SaveEmployeesToFile.saveToJson(ops, mapper, file);
                    break;

                case RESET_PASSWORD:
                    LoginAndPassword.resetPassword(ops, sc);
                    SaveEmployeesToFile.saveToJson(ops, mapper, file);
                    break;

                case EXIT:
                    SaveEmployeesToFile.saveToJson(ops, mapper, file);
                    System.out.println("Admin logged out.");
                    break;
			default:
				break;
            }
            }
            catch(Exception e) {
            	System.out.println("Operation failed: " + e.getMessage());	
         }
            

        } while (choice != AdminChoices.EXIT);
    }
}