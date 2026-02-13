package util;

import java.time.Year;
//import java.util.ArrayList;
import java.util.List;

import model.Employee;

public class SetNextID {
	public static String generateNextId(List<Employee> list) {
		int currentYear = Year.now().getValue() % 100;
		int max = 000;
		for (Employee e : list) {
			String id = e.getId();
			int num = Integer.parseInt(id.substring(4));
			int idYear = Integer.parseInt(id.substring(2, 4));
			if (idYear == currentYear) {
	            max = Math.max(max, num);
	        }
			else {
				max=000;
			}
		}
		 int next = max + 1;
		 return "TT" + currentYear + String.format("%03d", next);
	}
}
