package service;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PasswordTableDB {
	public static void insertPassword(Connection conn, String empId, String plainPassword) {
		try {
		String query="INSERT INTO passwords(empId,empPassword) VALUES (?,?)";
		PreparedStatement ps=conn.prepareStatement(query);

		ps.setString(1, empId);
        ps.setString(2, PasswordMethods.hash(plainPassword));
		ps.executeUpdate();
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to insert password", e);
		}
	}
}