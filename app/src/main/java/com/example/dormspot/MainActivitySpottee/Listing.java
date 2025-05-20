package com.example.dormspot.MainActivitySpottee;

public class Listing {
    private String dormName;
    private int capacity;
    private Double price;
    private String Status;
    private String id;
    private String location;
    private String inclusions;
    private String description;
    private String landmark;
    private String occupancyStatus;
    private String userMode;
    private String imageUrl;
    private String landlordId;


    // ✅ New fields for statistics
    private Integer views;
    private Integer saves;
    private Integer inquiries;
    private String rentedPeriod;
    private String occupancy;
    private Integer revenue;

    public Listing() {
        // Required empty constructor
    }

    public Listing(String dormName, int capacity, Double price, String Status,  String id,
                   String location, String inclusions, String description,
                   Integer views, Integer saves, Integer inquiries,
                   String rentedPeriod, String occupancy, Integer revenue, String occupancyStatus, String userRole) {

        this.dormName = dormName;
        this.capacity = capacity;
        this.price = price;
        this.Status = Status;
        this.id = id;
        this.location = location;
        this.inclusions = inclusions;
        this.description = description;
        this.occupancyStatus = occupancyStatus;

        this.views = views;
        this.saves = saves;
        this.inquiries = inquiries;
        this.rentedPeriod = rentedPeriod;
        this.occupancy = occupancy;
        this.revenue = revenue;

        this.userMode = userMode;
    }

    // Standard Getters
    public String getUserRole() {
        return userMode;
    }
    public String getOccupancyStatus() {
        return occupancyStatus;
    }
    public String getDormName() { return dormName; }

    public String getLandmark() {
        return landmark;
    }
    public int getCapacity() { return capacity; }
    public Double getPrice() { return price; }
    public String getStatus() { return Status; }
    public String getId() { return id; }
    public String getLocation() { return location; }
    public String getInclusions() { return inclusions; }
    public String getDescription() { return description; }
    public String getImageUrl() {
        return imageUrl;
    }

    public String getLandlordId() {
        return landlordId;
    }

    public Integer getViews() { return views; }
    public Integer getSaves() { return saves; }
    public Integer getInquiries() { return inquiries; }
    public String getRentedPeriod() { return rentedPeriod; }
    public String getOccupancy() { return occupancy; }
    public Integer getRevenue() { return revenue; }

    // Setters
    public void setDormName(String dormName) { this.dormName = dormName; }
    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
    public void setUserRole(String userRole) {
        this.userMode = userRole;
    }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setPrice(Double price) { this.price = price; }
    public void setStatus(String status) { this.Status = status; }
    public void setId(String id) { this.id = id; }
    public void setLocation(String location) { this.location = location; }
    public void setInclusions(String inclusions) { this.inclusions = inclusions; }
    public void setDescription(String description) { this.description = description; }
    public void setOccupancyStatus(String occupancyStatus) {
        this.occupancyStatus = occupancyStatus;
    }
    // Optional: Add setters too if needed
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLandlordId(String landlordId) {
        this.landlordId = landlordId;
    }
    public void setViews(Integer views) { this.views = views; }
    public void setSaves(Integer saves) { this.saves = saves; }
    public void setInquiries(Integer inquiries) { this.inquiries = inquiries; }
    public void setRentedPeriod(String rentedPeriod) { this.rentedPeriod = rentedPeriod; }
    public void setOccupancy(String occupancy) { this.occupancy = occupancy; }
    public void setRevenue(Integer revenue) { this.revenue = revenue; }
}
