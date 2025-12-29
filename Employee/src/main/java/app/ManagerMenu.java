package app;
import java.io.File;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import manager.Manage;
import operations.*;

public class ManagerCruds {

    public static void start(Manage ops, Scanner sc,
                             ObjectMapper mapper, File file) throws Exception {

        ManagerChoices choice = null;

        
        do {
            System.out.println("\n--- MANAGER MENU ---");
            for (ManagerChoices c : ManagerChoices.values()) {
                System.out.println(c);
            }
            System.out.println("Your choice: ");
         
            try {
                choice = ManagerChoices.valueOf(sc.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid choice. Please select from the menu.");
                continue;
            }

            try {
            switch (choice) {

                case UPDATE:
                    UpdateEmployee.update(ops, sc);
                    SaveEmployeesToFile.saveToJson(ops, mapper, file);
                    break;

                case SHOW:
                    ShowEmployee.showAll(ops);
                    break;

                case CHANGE_PASSWORD:
                    LoginAndPassword.updatePassword(ops, sc);
                    SaveEmployeesToFile.saveToJson(ops, mapper, file);
                    break;

                case EXIT:
                    SaveEmployeesToFile.saveToJson(ops, mapper, file);
                    System.out.println("Manager logged out.");
                    break;
            	}
            }
            catch(Exception e) {
            	System.out.println("Operation failed: " + e.getMessage());	
         }

        }
        while (choice != ManagerChoices.EXIT);
    }
}