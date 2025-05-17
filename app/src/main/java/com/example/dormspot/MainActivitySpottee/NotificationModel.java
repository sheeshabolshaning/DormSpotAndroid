package com.example.dormspot.MainActivitySpottee;

import java.util.Date;

public class NotificationModel {
    private String spotteeId;
    private String title;
    private String message;
    private Date timestamp;

    public NotificationModel() {} // Required for Firestore

    public String getSpotteeId() {
        return spotteeId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
