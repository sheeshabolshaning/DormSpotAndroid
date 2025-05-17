package com.example.dormspot.MainActivitySpottr;

public class BookingModel {
    private String bookingId;
    private String userId;
    private String userName;
    private String dormName;
    private String totalPrice;
    private String bookingDates;
    private String landlordId;

    public BookingModel() {} // Required for Firebase

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getDormName() { return dormName; }
    public String getTotalPrice() { return totalPrice; }
    public String getBookingDates() { return bookingDates; }
    public String getLandlordId() { return landlordId; }
}
