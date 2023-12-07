/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import models.Product;
import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author thangphan
 */
public class ProductService {
	public ArrayList<Product> getProducts() {
		ArrayList<Product> products = new ArrayList<Product>();
		DBConnection dbConnection = new DBConnection();
		Connection conn = dbConnection.getConnection();
		String query = "SELECT * FROM products";
		Statement statement;
		ResultSet resultSet;

		try {
			statement = conn.createStatement();
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				Product product = new Product(
					resultSet.getInt("id"),
					resultSet.getString("name"),
					resultSet.getFloat("price"),
					resultSet.getString("add_date"),
					resultSet.getBytes("image"));
				
				products.add(product);
			}
		} catch (SQLException ex) {
			System.out.println("Exception here: " + ex);
		}

		return products;
	}
	
	public Product getProductById(int productId) {
		Product product = null;
		DBConnection dbConnection = new DBConnection();
		Connection conn = dbConnection.getConnection();
		String query = "SELECT * FROM products WHERE id = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
		    preparedStatement = conn.prepareStatement(query);
		    preparedStatement.setInt(1, productId);

		    resultSet = preparedStatement.executeQuery();
		    if (resultSet.next()) {
			product = new Product(
			    resultSet.getInt("id"),
			    resultSet.getString("name"),
			    resultSet.getFloat("price"),
			    resultSet.getString("add_date"),
			    null
			);
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

		return product;
	}
}
