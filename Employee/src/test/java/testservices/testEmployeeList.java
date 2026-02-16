package testservices;

//import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
//import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.io.File;
//import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import customExceptions.InvalidDataException;
import dao.CrudFileImplementation;
import service.Create;

@ExtendWith(MockitoExtension.class)
class testEmployeeList{

	@Mock
	private CrudFileImplementation impl;

	@Mock
	private ObjectMapper mapper;

	@Mock
	private File file;

//	@SuppressWarnings("unchecked")
	@Test
	void testAdd() throws InvalidDataException {

		String input = String.join("\n","Rohith", "rohith@gmail.com", "Hyderabad", "dev", "ADMIN");

		Scanner scanner = new Scanner(input);

		Create.handleAdd(impl, scanner, mapper, file);
		
//		ArrayList<String>al=new ArrayList<>();
//		al.add("ADMIN");
		
		verify(impl).add(eq("Rohith"), eq("rohith@gmail.com"), eq("Hyderabad"), eq("dev"), argThat(list -> list.size() == 1 &&
                list.get(0).equalsIgnoreCase("ADMIN")), anyString());
	}
}
