package service;

import java.sql.Connection;
import java.sql.PreparedStatement;

import util.MakeConnection;

public class PasswordTableDB {
	public static void insertPassword(String empId, String plainPassword) {
			String query = "INSERT INTO passwords(empId,empPassword) VALUES (?,?)";
			try(Connection conn= MakeConnection.getConnection();
					PreparedStatement ps = conn.prepareStatement(query)){

			ps.setString(1, empId);
			ps.setString(2, PasswordMethods.hash(plainPassword));
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("Failed to insert password", e);
		}
	}
}