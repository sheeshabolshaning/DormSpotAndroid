package com.example.dormspot.MainActivitySpottee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.R;

public class reviews extends AppCompatActivity {

    LinearLayout reviewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews); // assuming your XML file is named reviews.xml

        reviewContainer = findViewById(R.id.reviews_list_container); // We'll add this ID in step 2

        addReview("Knight Peruz",
                "- Stayed at Eagle Crest for a review of my exams. The room was spacious and comfortable. It eased my living for the past 3 months before I left.",
                5);

        addReview("Nahs Wakely",
                "- I took a bedspacer on their Eagle Crest rooms and it was a good experience. The tenants were nice and the whole dorm had accommodating equipment. Had to leave after a month but still good.",
                4);
    }

    private void addReview(String name, String text, int rating) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View reviewCard = inflater.inflate(R.layout.review_card, reviewContainer, false);

        TextView reviewerName = reviewCard.findViewById(R.id.reviewer_name);
        TextView reviewText = reviewCard.findViewById(R.id.review_text);
        RatingBar ratingBar = reviewCard.findViewById(R.id.rating_bar);

        reviewerName.setText(name);
        reviewText.setText(text);
        ratingBar.setRating(rating);

        reviewContainer.addView(reviewCard);
    }
}
