package com.example.dormspot.MainActivitySpottee;

public class Listing {
    private String dormName;
    private int capacity;
    private double price;
    private String status;
    private String imageUrl;

    private int Id;

    public Listing() {

    }

    public Listing(String dormName, int capacity, double price, String status, String imageUrl, int Id) {
        this.dormName = dormName;
        this.capacity = capacity;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
        this.Id=Id;
    }

    // Getters
    public String getDormName() { return dormName; }
    public int getCapacity() { return capacity; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public String getImageUrl() { return imageUrl; }

    public int getId() {
        return Id;
    }
}
