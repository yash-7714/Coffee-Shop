package com.example.coffeeshop;

public class Product {
    private int id;
    private String name;
    private String price;
    private String category;
    private String imageUrl;

    public Product(int id, String name, String price, String category, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
}
