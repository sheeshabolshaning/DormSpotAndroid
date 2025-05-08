package com.example.dormspot.MainActivitySpottr;

public class DormItem {
    private String id; // Firestore document ID
    private String dormName;
    private String imageUrl;
    private String status;
    private int price;
    private int capacity;
    private String location;
    private String description;

    public DormItem() {
        // Required empty constructor for Firestore
    }

    public DormItem(String id, String dormName, String imageUrl, String status, int price, int capacity, String location, String description) {
        this.id = id;
        this.dormName = dormName;
        this.imageUrl = imageUrl;
        this.status = status;
        this.price = price;
        this.capacity = capacity;
        this.location = location;
        this.description = description;
    }

    // 🔽 Getters
    public String getId() {
        return id;
    }

    public String getDormName() {
        return dormName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public int getPrice() {
        return price;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    // 🔽 Setters (needed for Firestore mapping)
    public void setId(String id) {
        this.id = id;
    }

    public void setDormName(String dormName) {
        this.dormName = dormName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
