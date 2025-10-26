package model;

import java.io.Serializable;


public class Product implements Serializable {
private static final long serialVersionUID = 1L;


private String id;
private String name;
private int quantity;
private double price;


public Product(String id, String name, int quantity, double price) {
this.id = id;
this.name = name;
this.quantity = quantity;
this.price = price;
}


// Getters and Setters
public String getId() { return id; }
public String getName() { return name; }
public int getQuantity() { return quantity; }
public double getPrice() { return price; }


public void setName(String name) { this.name = name; }
public void setQuantity(int quantity) { this.quantity = quantity; }
public void setPrice(double price) { this.price = price; }


@Override
public String toString() {
return id + " | " + name + " | Qty:" + quantity + " | Rs:" + price;
}
}