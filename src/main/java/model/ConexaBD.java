package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaBD {
	
	public Connection getConnection() {
		
		try {
			return DriverManager.getConnection("jdbc:postgresql:BancoMaven","postgres","1908");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}	
	}
}
