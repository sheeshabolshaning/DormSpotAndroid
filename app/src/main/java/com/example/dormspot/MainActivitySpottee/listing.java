package com.example.dormspot.MainActivitySpottee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dormspot.R;

public class listing extends AppCompatActivity {

    private Button myListingsButton;  // Declare your button
    private Button statisticButton;
    private Button reviewsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        // Initialize buttons
        myListingsButton = findViewById(R.id.my_listings);
        statisticButton = findViewById(R.id.statistic);
        reviewsButton = findViewById(R.id.reviews);

        // Set default selected button (My Listings)
        myListingsButton.setSelected(true);

        // Set the click listeners for the buttons
        myListingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change selection state
                selectButton(myListingsButton);
                // Start activity or any other action
                Intent intent = new Intent(listing.this, listing2.class);
                startActivity(intent);
            }
        });

        statisticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change selection state
                selectButton(statisticButton);
                // Additional actions for statistic button
            }
        });

        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change selection state
                selectButton(reviewsButton);
                // Additional actions for reviews button
            }
        });
    }

    // Helper method to manage button selection
    private void selectButton(Button selectedButton) {
        // Unselect other buttons
        myListingsButton.setSelected(false);
        statisticButton.setSelected(false);
        reviewsButton.setSelected(false);

        // Select the clicked button
        selectedButton.setSelected(true);
    }
}
