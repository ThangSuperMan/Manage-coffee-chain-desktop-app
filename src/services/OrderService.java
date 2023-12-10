package services;

import models.Order;
import database.DBConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import models.Product;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thangphan
 */
public class OrderService {
	public int insertOrder(Order order) {
		DBConnection dbConnection = new DBConnection();
		Connection conn = dbConnection.getConnection();
		int generatedOrderId = -1; // Default value indicating failure

		try {
		    String sql = "INSERT INTO orders (total_amount, user_id) VALUES(?, ?)";
		    PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		    ps.setFloat(1, order.getTotalAmount());
		    ps.setInt(2, order.getUserId()); // Assuming you want to set the user ID

		    int affectedRows = ps.executeUpdate();

		    if (affectedRows > 0) {
			ResultSet generatedKeys = ps.getGeneratedKeys();

			if (generatedKeys.next()) {
			    generatedOrderId = generatedKeys.getInt(1);
			} else {
			    System.err.println("Failed to retrieve generated order ID.");
			}
		    } else {
			System.err.println("Failed to insert order.");
		    }
		} catch (SQLException ex) {
		    System.err.println("Exception here: " + ex);
		}

		return generatedOrderId;
	}
	
	public Float getTotalAmountByPaymentMethod(String paymentMethod) {
		Order order = null;
		DBConnection dbConnection = new DBConnection();
		Connection conn = dbConnection.getConnection();
		String query = "SELECT sum(total_amount) as total_amount FROM orders WHERE payment_method = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
		    preparedStatement = conn.prepareStatement(query);
		    preparedStatement.setString(1, paymentMethod);

		    resultSet = preparedStatement.executeQuery();
		    if (resultSet.next()) {
			Float totalAmount = resultSet.getFloat("total_amount");
			return totalAmount;
		    }
		} catch (SQLException ex) {
		    System.out.println("Exception here: " + ex);
		} finally {
		    // Close resources in the reverse order of their creation to avoid resource leaks
		    try {
			if (resultSet != null) resultSet.close();
			if (preparedStatement != null) preparedStatement.close();
			if (conn != null) conn.close();
		    } catch (SQLException e) {
			e.printStackTrace();
		    }
		}

		return null;
	}

}
