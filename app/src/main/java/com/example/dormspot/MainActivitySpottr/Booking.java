package com.example.dormspot.MainActivitySpottr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.dormspot.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class Booking extends AppCompatActivity {

    private ViewPager2 viewPager2;
    public ImagePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);

        // ✅ Get data from intent
        Intent intent = getIntent();
        String dormName = intent.getStringExtra("dormName");
        int capacity = intent.getIntExtra("capacity", 0);
        int price = intent.getIntExtra("price", 0);
        String status = intent.getStringExtra("status");
        String description = intent.getStringExtra("description");
        String listingId = intent.getStringExtra("listingId"); // 🔸 Ensure this is passed from adapter
        String imageUrl = intent.getStringExtra("imageUrl");

        // ✅ Bind views
        TextView title = findViewById(R.id.dormTitleText);
        TextView priceText = findViewById(R.id.priceText);
        TextView descriptionText = findViewById(R.id.descriptionText);
        Button bookNowBtn = findViewById(R.id.bookNowBtn);

        // ✅ Set values
        title.setText(dormName);
        priceText.setText("₱" + price + "/month");
        descriptionText.setText(description != null ? description : "No description");

        // ✅ Load image
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.imageholder);

        // ✅ Disable Book button if already occupied
        if ("occupied".equalsIgnoreCase(status)) {
            bookNowBtn.setEnabled(false);
            bookNowBtn.setText("Already Occupied");
            bookNowBtn.setAlpha(0.6f);
        } else {
            bookNowBtn.setOnClickListener(v -> {
                FirebaseFirestore.getInstance()
                        .collection("listings")
                        .document(listingId)
                        .update("status", "occupied")
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Booking successful!", Toast.LENGTH_SHORT).show();
                            bookNowBtn.setText("Booked");
                            bookNowBtn.setEnabled(false);
                            bookNowBtn.setAlpha(0.6f);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            });
        }

        // ✅ Back button logic
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // ✅ Optional: ViewPager2 image setup
        viewPager2 = findViewById(R.id.propertyImagePager);
        List<Integer> imageResources = Arrays.asList(
                R.drawable.house,
                R.drawable.house,
                R.drawable.house
        );
        adapter = new ImagePagerAdapter(imageResources);
        viewPager2.setAdapter(adapter);
    }
}
