package com.example.dormspot.MainActivitySpottee;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ListingAddActivity extends AppCompatActivity {

    private EditText dormNameField, capacityField, priceField, statusField, locationField, inclusionsField, descriptionField;
    private Button submitButton;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_add);

        // Safely enable the ActionBar back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize fields
        dormNameField = findViewById(R.id.editText_dormName);
        capacityField = findViewById(R.id.editText_capacity);
        priceField = findViewById(R.id.editText_price);
        statusField = findViewById(R.id.editText_status);
        locationField = findViewById(R.id.editText_location);
        inclusionsField = findViewById(R.id.editText_inclusions);
        descriptionField = findViewById(R.id.editText_description);
        submitButton = findViewById(R.id.button_submit_listing);

        db = FirebaseFirestore.getInstance();

        submitButton.setOnClickListener(v -> saveListing());
    }


    private void saveListing() {
        String dormName = dormNameField.getText().toString().trim();
        String capacity = capacityField.getText().toString().trim();
        String price = priceField.getText().toString().trim();
        String status = statusField.getText().toString().trim();
        String location = locationField.getText().toString().trim();
        String inclusions = inclusionsField.getText().toString().trim();
        String description = descriptionField.getText().toString().trim();

        if (TextUtils.isEmpty(dormName) || TextUtils.isEmpty(capacity) || TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Please fill in required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> listing = new HashMap<>();
        listing.put("dormName", dormName);
        listing.put("capacity", Integer.parseInt(capacity));
        listing.put("price", Double.parseDouble(price));
        listing.put("status", status);
        listing.put("location", location);
        listing.put("inclusions", inclusions);
        listing.put("description", description);
        listing.put("imageUrl", ""); // Placeholder, image upload can be added later

        db.collection("listings")
                .add(listing)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Listing added!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
