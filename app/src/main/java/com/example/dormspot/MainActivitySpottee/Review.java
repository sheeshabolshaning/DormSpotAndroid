package com.example.dormspot.MainActivitySpottee;

public class Review {
    private String name;
    private String comment;
    private int stars; // e.g., ★★★★☆
    private String imageUrl;


    public Review() {
        // Required empty constructor for Firebase or Gson
    }

    public Review(String name, String comment, int stars, String imageUrl) {
        this.name = name;
        this.comment = comment;
        this.stars = stars;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public int getStars() {
        return stars;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
