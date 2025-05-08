package com.example.dormspot.MainActivitySpottee;

public class Listing {
    private String dormName;
    private int capacity;
    private double price;
    private String status;
    private String imageUrl;
    private String id;
    private String location;
    private String inclusions;
    private String description;

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

    public Listing(String dormName, int capacity, double price, String status, String imageUrl, String id,
                   String location, String inclusions, String description,
                   Integer views, Integer saves, Integer inquiries,
                   String rentedPeriod, String occupancy, Integer revenue) {

        this.dormName = dormName;
        this.capacity = capacity;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
        this.id = id;
        this.location = location;
        this.inclusions = inclusions;
        this.description = description;

        this.views = views;
        this.saves = saves;
        this.inquiries = inquiries;
        this.rentedPeriod = rentedPeriod;
        this.occupancy = occupancy;
        this.revenue = revenue;
    }

    // Standard Getters
    public String getDormName() { return dormName; }
    public int getCapacity() { return capacity; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }
    public String getImageUrl() { return imageUrl; }
    public String getId() { return id; }
    public String getLocation() { return location; }
    public String getInclusions() { return inclusions; }
    public String getDescription() { return description; }

    public Integer getViews() { return views; }
    public Integer getSaves() { return saves; }
    public Integer getInquiries() { return inquiries; }
    public String getRentedPeriod() { return rentedPeriod; }
    public String getOccupancy() { return occupancy; }
    public Integer getRevenue() { return revenue; }

    // Setters
    public void setDormName(String dormName) { this.dormName = dormName; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setPrice(double price) { this.price = price; }
    public void setStatus(String status) { this.status = status; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setId(String id) { this.id = id; }
    public void setLocation(String location) { this.location = location; }
    public void setInclusions(String inclusions) { this.inclusions = inclusions; }
    public void setDescription(String description) { this.description = description; }

    public void setViews(Integer views) { this.views = views; }
    public void setSaves(Integer saves) { this.saves = saves; }
    public void setInquiries(Integer inquiries) { this.inquiries = inquiries; }
    public void setRentedPeriod(String rentedPeriod) { this.rentedPeriod = rentedPeriod; }
    public void setOccupancy(String occupancy) { this.occupancy = occupancy; }
    public void setRevenue(Integer revenue) { this.revenue = revenue; }
}
