package com.example.dormspot.MainActivitySpottee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.R;

public class listing3 extends AppCompatActivity {

    TextView titleView, locationView, capacityView, priceView, inclusionView, descriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing3);

        TextView titleView = findViewById(R.id.titleView);
        TextView locationView = findViewById(R.id.locationView);
        TextView capacityView = findViewById(R.id.capacityView);
        TextView priceView = findViewById(R.id.priceView);
        TextView inclusionView = findViewById(R.id.inclusionView);
        TextView descriptionView = findViewById(R.id.descriptionView);

// Get data from Listing2
        Intent intent = getIntent();
        titleView.setText(intent.getStringExtra("title"));
        locationView.setText(intent.getStringExtra("location"));
        capacityView.setText(intent.getStringExtra("capacity"));
        priceView.setText(intent.getStringExtra("price"));
        inclusionView.setText(intent.getStringExtra("inclusion"));
        descriptionView.setText(intent.getStringExtra("description"));


        Button postBtn = findViewById(R.id.button_post_listing);
        postBtn.setOnClickListener(v -> {
            Intent backToMain = new Intent(listing3.this, ListingMain.class);
            backToMain.putExtra("postedTitle", intent.getStringExtra("title"));
            // You can pass more fields if needed
            startActivity(backToMain);
        });

        Button removeBtn = findViewById(R.id.button_remove_listing);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Optional: show a toast or go back to Listing 1
                Toast.makeText(listing3.this, "Listing removed", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(listing3.this, ListingMain.class);
                startActivity(intent);
            }
        });
    }
}
