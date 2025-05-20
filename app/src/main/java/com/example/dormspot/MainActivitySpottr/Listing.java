package com.example.dormspot.MainActivitySpottr;

public class Listing {
    private String id; // Firestore document ID
    private String dormName;
    private String status;
    private Double price;
    private int capacity;
    private String location;
    private String description;
    private String occupancyStatus;

    public Listing() {
        // Required empty constructor for Firestore
    }

    public Listing(String id, String dormName, String status, Double price, int capacity, String location, String description, String occupancyStatus) {
        this.id = id;
        this.dormName = dormName;
        this.status = status;
        this.price = price;
        this.capacity = capacity;
        this.location = location;
        this.description = description;
        this.occupancyStatus = occupancyStatus;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getDormName() {
        return dormName;
    }

    public String getStatus() {
        return status;
    }

    public Double getPrice() {
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

    public String getOccupancyStatus() {
        return occupancyStatus;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setDormName(String dormName) {
        this.dormName = dormName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice(Double price) {
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

    public void setOccupancyStatus(String occupancyStatus) {
        this.occupancyStatus = occupancyStatus;
    }
}
