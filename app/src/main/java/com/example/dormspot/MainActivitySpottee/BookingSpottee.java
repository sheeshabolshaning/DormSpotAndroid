package com.example.dormspot.MainActivitySpottee;

public class BookingSpottee {
    private String userName;
    private String dormName;
    private String bookingDates;
    private String totalPrice;
    private String status;
    private String bookingId; // ✅ Needed for Firestore updates

    public BookingSpottee() {
        // required by Firebase
    }

    public String getUserName() { return userName; }
    public String getDormName() { return dormName; }
    public String getBookingDates() { return bookingDates; }
    public String getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}
