package app;
import java.util.Scanner;
import operations.UpdateMail;
import manager.Manage;
import operations.ShowEmployee;
import operations.UpdateAddress;
import operations.LoginAndPassword;

public class EmployeeCruds {

    public static void start(Manage ops, Scanner sc) throws Exception {

        EmployeeChoices choice=null;

        do {
            System.out.println("\n--- EMPLOYEE MENU ---");
            for (EmployeeChoices c : EmployeeChoices.values()) {
                System.out.println(c);
            }
            System.out.println("Your choice: ");
            
            try {
                choice = EmployeeChoices.valueOf(sc.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid choice. Please select from the menu.");
                continue;
            }
            
            try {
            switch (choice) {

            	case UPDATE:
            		System.out.println("Enter 1 to Update your mail ID");
            		System.out.println("Enter 2 to update your Address");
            		System.out.println("Your choice: ");
            		int ch=sc.nextInt();
            		sc.nextLine();
            		switch(ch) {
            		case 1:
            			UpdateMail.updateMail(ops,sc, null);
            			break;
            		case 2:
            			UpdateAddress.updateAddress(ops, sc, null);
            			break;
            		default:
            			break;
            		}
            		
            	
                case SHOW_SELF:
                    ShowEmployee.showOne(ops);
                    break;

                case CHANGE_PASSWORD:
                    LoginAndPassword.updatePassword(ops, sc);
                    break;

                case EXIT:
                    System.out.println("Employee logged out.");
                    break;
            }
            }
            catch(Exception e) {
            	System.out.println("Operation failed: " + e.getMessage());	
         }

        } while (choice != EmployeeChoices.EXIT);
    }
}