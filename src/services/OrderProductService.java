/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.OrderProduct;
import models.OrderProductResult;

/**
 *
 * @author thangphan
 */
public class OrderProductService {
	public void insertOrderProduct(OrderProduct order) {
		DBConnection dbConnection = new DBConnection();
		Connection conn = dbConnection.getConnection();
		
		try {
			String sql = "INSERT INTO order_product (product_id, order_id, quantity) VALUES(?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setFloat(1, order.getProductId());
			ps.setInt(2, order.getOrderId());
			ps.setInt(3, order.getQuantity());
			
			ps.executeUpdate();
		} catch(SQLException ex) {
			System.err.println("Exception here: " + ex);
		}
	}
	
	public List<OrderProductResult> getTotalMoneyPerDayPerProduct() {
		List<OrderProductResult> results = new ArrayList<>();

		DBConnection dbConnection = new DBConnection();
		Connection conn = dbConnection.getConnection();
		try {
            String query = "SELECT DATE(o.order_date) AS order_day, p.id AS product_id, p.name AS product_name, p.image AS product_image, SUM(op.quantity * p.price) AS total_money_per_day_per_product " +
                    "FROM orders o " +
                    "JOIN order_product op ON o.id = op.order_id " +
                    "JOIN products p ON op.product_id = p.id " +
                    "GROUP BY order_day, product_id, product_name, product_image;";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        OrderProductResult result = new OrderProductResult(
                                resultSet.getString("order_day"),
                                resultSet.getInt("product_id"),
                                resultSet.getString("product_name"),
                                resultSet.getFloat("total_money_per_day_per_product"),
				resultSet.getBytes("product_image"));

                        results.add(result);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
}
