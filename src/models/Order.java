/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import models.Product;

/**
 *
 * @author thangphan
 */
public class Order {
    private int id;
    private String orderDate;
    private Float totalAmount;
    private int userId;

    public Order(int id, String orderDate, Float totalAmount, int userId) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
	this.userId = userId;
    }
    
    // Constructor without id and orderDate
    public Order(Float totalAmount, int userId) {
        this.totalAmount = totalAmount;
	this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public int getUserId() {
	return userId;
    }
    
    public void setUserId(int userId) {
	this.userId = userId;
    }
}