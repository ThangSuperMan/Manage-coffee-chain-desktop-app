/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import database.DBConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import models.User;

/**
 *
 * @author thangphan
 */
public class UserService {
	public void insertUser(User user) {
		DBConnection dbConnection = new DBConnection();
		Connection conn = dbConnection.getConnection();
		
		boolean isAddPointsEarn = user.getPointsEarned() != 0;
		String sql = isAddPointsEarn ? "INSERT INTO users (name, phone_number, points_earned) VALUES(?, ?, ?)" : 
						   "INSERT INTO users (name, phone_number) VALUES(?, ?)";
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getName());
			ps.setString(2, user.getPhoneNumber());
			
			if (isAddPointsEarn) {
				ps.setInt(3, user.getPointsEarned());
			}
			ps.executeUpdate();
		} catch(SQLException ex) {
			System.err.println("Exception here: " + ex);
		}
	}
	
	public User getUserByPhoneNumber(String phoneNumber) {
		User user = null;
		DBConnection dbConnection = new DBConnection();
		Connection conn = dbConnection.getConnection();
		String query = "SELECT * FROM users WHERE phone_number = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
		    preparedStatement = conn.prepareStatement(query);
		    preparedStatement.setString(1, phoneNumber);

		    resultSet = preparedStatement.executeQuery();
		    if (resultSet.next()) {
			user = new User(
			    resultSet.getInt("id"),
			    resultSet.getString("name"),
		            resultSet.getString("phone_number"),
			    resultSet.getInt("points_earned")
			);
		    }
		} catch (SQLException ex) {
		    System.out.println("Exception here: " + ex);
		} finally {
		    try {
			if (resultSet != null) resultSet.close();
			if (preparedStatement != null) preparedStatement.close();
			if (conn != null) conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
		}

		return user;
	}
	
	public void updateUserPoints(User user) {
		DBConnection dbConnection = new DBConnection();
		Connection conn = dbConnection.getConnection();

		String sql = "UPDATE users SET points_earned = ? WHERE phone_number = ?";

		try {
		    PreparedStatement ps = conn.prepareStatement(sql);
		    ps.setInt(1, user.getPointsEarned());
		    ps.setString(2, user.getPhoneNumber());

		    int rowsUpdated = ps.executeUpdate();

		    if (rowsUpdated > 0) {
			System.out.println("User points updated successfully.");
		    } else {
			System.out.println("No user found with the given phone number.");
		    }
		} catch (SQLException ex) {
		    System.err.println("Exception here: " + ex);
		}
	}
}
