package entity;

import java.time.LocalDate;

public class Product {
    private int id;
    private String name;
    private String manufacturer;
    private double price;
    private int shelfLife;
    private int quantity;
    
    public Product() {
    }
    

    public Product(int id, String name, String manufacturer, double price, int shelfLife, int quantity) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.shelfLife = shelfLife;
        this.quantity = quantity;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getShelfLife() {
        return shelfLife;
    }
    
    public int getQuantity() {
        return quantity;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', manufacturer='%s', price=%.2f, shelfLife=%d days, quantity=%d}",
                id, name, manufacturer, price, shelfLife, quantity);
    }
}
