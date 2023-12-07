package services;

import models.Order;
import database.DBConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

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

}
