/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managecoffeechain;

/**
 *
 * @author thangphan
 */
public class Product {
    private int id;
    private String name;
    private float price;
    private String addDate;
    private byte[] image;
    
    public Product(int id, String name, float price, String addDate, byte[] image) {
        this.id = id;
        this.name = name;
        this.addDate = addDate;
        this.image = image;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public float getPrice() {
        return price;
    }
    
    public String getAddDate() {
        return addDate;
    }
    
    public byte[] getImage() {
        return image;
    }
}
