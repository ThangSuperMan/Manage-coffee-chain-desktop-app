/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author thangphan
 */
public class OrderProduct {
	private int order_id;
	private int product_id;
	private int quantity;
	
	public OrderProduct(int order_id, int product_id, int quantity) {
		this.order_id = order_id;
		this.product_id = product_id;
		this.quantity = quantity;
	}
	
	public int getOrderId() {
	    return order_id;
	}

	public void setOrderId(int order_id) {
	    this.order_id = order_id;
	}

	public int getProductId() {
	    return product_id;
	}

	public void setProductId(int product_id) {
	    this.product_id = product_id;
	}
	
	public int getQuantity() {
	    return quantity;
	}
	
	public void setQuantity(int quantity) {
	    this.quantity = quantity;
	}
}