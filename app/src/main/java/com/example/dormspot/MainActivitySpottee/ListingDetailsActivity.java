package com.example.dormspot.MainActivitySpottee;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dormspot.R;

public class ListingDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);

        TextView titleView = findViewById(R.id.textView_title);
        TextView locationView = findViewById(R.id.textView_location);
        TextView capacityView = findViewById(R.id.textView_capacity);
        TextView priceView = findViewById(R.id.textView_price);
        TextView inclusionsView = findViewById(R.id.textView_inclusions);
        TextView descriptionView = findViewById(R.id.textView_description);
        ImageView imageView = findViewById(R.id.imageView_room1);

        // Get intent data
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String location = intent.getStringExtra("location");
        int capacity = intent.getIntExtra("capacity", 0);
        double price = intent.getDoubleExtra("price", 0);
        String inclusions = intent.getStringExtra("inclusions");
        String description = intent.getStringExtra("description");
        String imageUrl = intent.getStringExtra("imageUrl");

        // Set to views
        titleView.setText(title);
        locationView.setText("Location: " + location);
        capacityView.setText("Capacity: " + capacity);
        priceView.setText("Price: ₱" + String.format("%.0f", price));
        inclusionsView.setText("Inclusions: " + inclusions);
        descriptionView.setText("Description: " + description);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }
}


