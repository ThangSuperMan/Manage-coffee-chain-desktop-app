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
	public void insertOrder(Order order) {
		DBConnection dbConnection = new DBConnection();
		Connection conn = dbConnection.getConnection();
		
		try {
			String sql = "INSERT INTO orders (total_amount, payment_method, product_id) VALUES(?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setFloat(1, order.getTotalAmount());
			ps.setString(2, order.getPaymentMethod());
			ps.setInt(3, order.getProductId());

			ps.executeUpdate();
		} catch(SQLException ex) {
			System.err.println("Exception here: " + ex);
		}
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
