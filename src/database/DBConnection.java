/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author thangphan
 */
public class DBConnection {
	public Connection getConnection() {
		Connection conn = null;
		String dbName = "manage_coffee_chain";
		String dbUser = "root";
		String dbPassword = "thang123";

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, dbUser, dbPassword);
			System.out.println("DB Connected");
		} catch (SQLException ex) {
			System.out.println("Error sql: " + ex);
			System.out.println("DB Connected");

			return null;
		}

		return conn;
	}
}
