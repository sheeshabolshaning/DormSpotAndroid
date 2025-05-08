package com.example.dormspot.MainActivitySpottr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dormspot.R;

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

        // ✅ Bind views
        TextView title = findViewById(R.id.dormTitleText);
        TextView priceText = findViewById(R.id.priceText);
        TextView descriptionText = findViewById(R.id.descriptionText); // Make sure it's in XML

        // ✅ Set text values
        title.setText(dormName);
        priceText.setText("₱" + price + "/month");
        descriptionText.setText(description); // optional

        // ✅ Set up ViewPager2 with images
        viewPager2 = findViewById(R.id.propertyImagePager);

        List<Integer> imageResources = Arrays.asList(
                R.drawable.house,  // Replace these with dynamic data later
                R.drawable.house,
                R.drawable.house
        );

        adapter = new ImagePagerAdapter(imageResources);
        viewPager2.setAdapter(adapter);

        // ✅ Back button logic
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
}
