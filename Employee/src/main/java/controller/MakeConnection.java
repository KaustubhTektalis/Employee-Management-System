package controller;

import java.sql.Connection;
//import java.sql.DriverManager;

public class MakeConnection {
	public Connection connect_to_db(String dbname, String user, String pass) {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");

			conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);

			System.out.println("Connection successful");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
