package com.example.dormspot.MainActivitySpottr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

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
        setContentView(R.layout.booking); // Make sure this matches your XML filename

        viewPager2 = findViewById(R.id.propertyImagePager);

        // List of images to show in the ViewPager2
        List<Integer> imageResources = Arrays.asList(
                R.drawable.house,
                R.drawable.house,
                R.drawable.house
        );

        adapter = new ImagePagerAdapter(imageResources);
        viewPager2.setAdapter(adapter);

        // ✅ Back Button Functionality
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // Go back to Home screen (or previous activity)
        });
    }
}
