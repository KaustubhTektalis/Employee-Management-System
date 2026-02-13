package testings;

import model.Employee;
import service.PasswordMethods;
import util.SetNextID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestServices {
	

	public static void saveToMock( ObjectMapper objectMapper, File file, List<Employee>list) {
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
			System.out.println("Saved successfully.");
		} catch (IOException e) {
			System.out.println("Error saving employees to JSON: " + e.getMessage());
		}
	}
	
	
	
    ObjectMapper mapper = new ObjectMapper();
    File file;

    @BeforeEach
    void setup() throws Exception {
        file = new File("mockdata.json");
    }

    private List<Employee> loadEmployeesFromFile() throws Exception {
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        return mapper.readValue(file, new TypeReference<List<Employee>>() {});
    }

    @Test
    void testAddingEmployeeToMockFile() throws Exception {
        List<Employee> mockList = loadEmployeesFromFile();
        Employee newEmp = new Employee("TT26001", "Charlie", "charlie@gmail.com", "SF", "HR",
                new ArrayList<>(List.of("Admin")), PasswordMethods.randomPasswordGenerator());
        mockList.add(newEmp);
        saveToMock(mapper, file,mockList);

        List<Employee> reloaded= loadEmployeesFromFile();
        assertEquals("Charlie", reloaded.get(0).getName());
        assertEquals("TT26001", reloaded.get(0).getId());
        assertEquals("charlie@gmail.com", reloaded.get(0).getMail());
        assertEquals("SF", reloaded.get(0).getAddress());
        assertEquals("HR", reloaded.get(0).getDepartment());
    }

    @Test
    void testGenerateNextIdAfterAddingEmployee() throws Exception {
        List<Employee> mockList = loadEmployeesFromFile();

        String nextId = SetNextID.generateNextId(mockList);
        assertEquals("TT26003", nextId);
    }
    
    @Test
    void testAddingNewEmployeeWithAutoId() throws Exception{
    	List<Employee> mockList = loadEmployeesFromFile();
    	Employee newEmp=new Employee(SetNextID.generateNextId(mockList),
    			"Rineesha","rin@gmail.com","hyd","Admin",new ArrayList<>
    	(List.of("Admin","Manager")),PasswordMethods.randomPasswordGenerator());
    	 mockList.add(newEmp);
         saveToMock(mapper, file,mockList);
    	
         List<Employee> reloaded= loadEmployeesFromFile();
         assertEquals("TT26001", reloaded.get(0).getId());
    } 
    
    @Test
    void testUpdateName() throws Exception{
    	List<Employee> mockList=loadEmployeesFromFile();
    	 String targetId = "TT26002";
    	 for (Employee emp : mockList) {
    	        if (emp.getId().equals(targetId)) {
    	            emp.setName("Tharun");
    	            break;
    	        }
    	    }
    	 saveToMock(mapper, file, mockList);
    	 List<Employee> reloaded = loadEmployeesFromFile();
    	 assertEquals("Tharun", reloaded.get(1).getName());
    }
}
