package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import java.util.List;

import model.Employee;

public class Read {
	public static List<Employee> readAll(Connection conn) {
		List<Employee> list = new ArrayList<>();

		try {
			String query = """
					    SELECT empid, empname, empmail, empaddress, empdepartment
					    FROM employees
					""";

			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				list.add(new Employee(rs.getString("empid"), rs.getString("empname"), rs.getString("empmail"),
						rs.getString("empaddress"), rs.getString("empdepartment"), new ArrayList<>(), // roles empty
						null));
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return list;
	}

	
	
	public static Employee readOne(Connection conn, String id) {
		try {
			String query = """
					    SELECT empid, empname, empmail, empaddress, empdepartment
					    FROM employees
					    WHERE empid = ?
					""";

			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, id);

			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				return null;
			}

			return new Employee(rs.getString("empid"), rs.getString("empname"), rs.getString("empmail"),
					rs.getString("empaddress"), rs.getString("empdepartment"), new ArrayList<>(),
					null);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static Employee readSelf(Connection conn, String id) {
		try {
			
			ArrayList<String> roles = new ArrayList<>();
			String roleQuery = "SELECT role FROM roles WHERE empid = ?";
			try (PreparedStatement ps = conn.prepareStatement(roleQuery)) {
			    ps.setString(1, id);
			    ResultSet rs = ps.executeQuery();
			    while (rs.next()) {
			        roles.add(rs.getString("role"));
			    }
			}
			
			String empQuery = """
		            SELECT empid, empname, empmail, empaddress, empdepartment
		            FROM employees
		            WHERE empid = ?
		        """;

			PreparedStatement ps = conn.prepareStatement(empQuery);
			ps.setString(1, id);

			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				return null;
			}

			return new Employee(rs.getString("empid"), rs.getString("empname"), rs.getString("empmail"),
					rs.getString("empaddress"), rs.getString("empdepartment"), roles,
					null);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}