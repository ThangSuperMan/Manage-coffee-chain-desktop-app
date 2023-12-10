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
public class OrderProductResult {
    private String orderDay;
    private int productId;
    private String productName;
    private byte[] productImage;
    private float totalMoneyPerDayPerProduct;

    public OrderProductResult(String orderDay, int productId, String productName, float totalMoneyPerDayPerProduct, byte[] productImage) {
        this.orderDay = orderDay;
        this.productId = productId;
        this.productName = productName;
	this.productImage = productImage;
        this.totalMoneyPerDayPerProduct = totalMoneyPerDayPerProduct;
    }

    public String getOrderDay() {
        return orderDay;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public float getTotalMoneyPerDayPerProduct() {
        return totalMoneyPerDayPerProduct;
    }
    
    public byte[] getProductImage() {
	return productImage;
    }
    
    public void setProductImage(byte[] image) {
	this.productImage = image;
    }

    @Override
    public String toString() {
        return "OrderProductResult{" +
                "orderDay='" + orderDay + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
		", productImage='" + productImage + '\'' +
                ", totalMoneyPerDayPerProduct=" + totalMoneyPerDayPerProduct +
                '}';
    }
}
