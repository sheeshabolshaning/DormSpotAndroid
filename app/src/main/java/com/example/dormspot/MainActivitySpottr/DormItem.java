package com.example.dormspot.MainActivitySpottr;

public class DormItem {
    private String dormName;
    private String imageUrl;
    private String status;
    private int price;
    private int capacity;
    private String location;

    public DormItem() {
        // Required for Firestore
    }

    public DormItem(String dormName, String imageUrl, String status, int price, int capacity, String location) {
        this.dormName = dormName;
        this.imageUrl = imageUrl;
        this.status = status;
        this.price = price;
        this.capacity = capacity;
        this.location = location;
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
}
