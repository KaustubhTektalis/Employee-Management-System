package util;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class MakeConnection {

    private static HikariDataSource dataSource;

    private MakeConnection() {}
    
    public static void init(String host, String dbName, String user, String pass, boolean ssl) {
        if (dataSource != null && !dataSource.isClosed()) {
        	return;
        }
        
        HikariConfig config = new HikariConfig();
        String url = "jdbc:postgresql://" + host + ":5432/" + dbName;
		 if (ssl) {
	            url += "?sslmode=require";
	        }
		 config.setJdbcUrl(url);
	        config.setUsername(user);
	        config.setPassword(pass);
	        config.setMaximumPoolSize(10);     // max connections in pool
	        config.setMinimumIdle(2);          // minimum idle connections
	        config.setIdleTimeout(600000);     // 10 minutes
	        config.setMaxLifetime(1800000);    // 30 minutes
	        config.setConnectionTimeout(30000); // 30 seconds

	        dataSource = new HikariDataSource(config);

	        System.out.println("Connection pool initialized successfully");
	    }
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Connection pool not initialized. Call init() first.");
        }
        return dataSource.getConnection();
    }
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Connection pool closed");
        }
    }
}
