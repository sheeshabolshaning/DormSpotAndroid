package com.example.dormspot.MainActivitySpottee;

public class Listing {
    private String dormName;
    private int capacity;
    private double price;
    private String status;
    private String imageUrl;
    private int id;

    // 🆕 Added fields for full listing details
    private String location;
    private String inclusions;
    private String description;

    public Listing() {
        // Required empty constructor (for Firebase or other uses)
    }

    public Listing(String dormName, int capacity, double price, String status, String imageUrl,
                   int id, String location, String inclusions, String description) {
        this.dormName = dormName;
        this.capacity = capacity;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
        this.id = id;
        this.location = location;
        this.inclusions = inclusions;
        this.description = description;
    }

    // Getters
    public String getDormName() {
        return dormName;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getInclusions() {
        return inclusions;
    }

    public String getDescription() {
        return description;
    }

    // Setters (Optional: for Firebase, form submissions, or updates)
    public void setDormName(String dormName) {
        this.dormName = dormName;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
