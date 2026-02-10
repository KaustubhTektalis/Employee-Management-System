package service;

import java.sql.Connection;

import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import enums.RoleChoice;

public class RoleTableDB {
	private static final Logger logger = LogManager.getLogger(RoleTableDB.class);
	public static void insertRole(Connection conn, String empid, RoleChoice role) {
		try {
		String roleQuery="INSERT INTO roles (empId,role) VALUES (?,?);";
		PreparedStatement ps=conn.prepareStatement(roleQuery);
		ps.setString(1, empid);
        ps.setString(2, role.name());
		ps.executeUpdate();
		logger.info("The role {} added for employee {}" , role, empid);
		}
		catch (Exception e) {
		    logger.error("Error adding role {} for employee {}", role, empid, e);
		    throw new RuntimeException(e);
		}
	}
}