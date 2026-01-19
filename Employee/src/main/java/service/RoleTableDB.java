package service;

import java.sql.Connection;

import java.sql.PreparedStatement;

import enums.RoleChoice;

public class RoleTableDB {
	public static void insertRole(Connection conn, String empid, RoleChoice role) {
		try {
		String roleQuery="INSERT INTO roles (empId,role) VALUES (?,?);";
		PreparedStatement ps=conn.prepareStatement(roleQuery);
		ps.setString(1, empid);
        ps.setString(2, role.name());
		ps.executeUpdate();
		System.out.println("The role "+role +" added for employee "+empid);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}