package com.example.dormspot.MainActivitySpottee;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormspot.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class listing1 extends AppCompatActivity {

    private Button myListingsButton;
    private Button statisticButton;
    private Button reviewsButton;
    private RecyclerView recyclerView;
    private ListingAdapter listingAdapter;
    private List<Listing> listingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        // Initialize buttons
        myListingsButton = findViewById(R.id.my_listings);
        statisticButton = findViewById(R.id.statistic);
        reviewsButton = findViewById(R.id.reviews);
        myListingsButton.setSelected(true);

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerViewListings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the listing data
        listingList = new ArrayList<>();
        listingAdapter = new ListingAdapter(listingList);
        recyclerView.setAdapter(listingAdapter);

        // Fetch listings from Firestore
        fetchListingsFromFirestore();

        // Button click listeners
        myListingsButton.setOnClickListener(v -> {
            selectButton(myListingsButton);
            Intent intent = new Intent(listing1.this, listing2.class);
            startActivity(intent);
        });

        statisticButton.setOnClickListener(v -> selectButton(statisticButton));
        reviewsButton.setOnClickListener(v -> selectButton(reviewsButton));
    }

    private void fetchListingsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("listings")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(listing1.this, "No listings found", Toast.LENGTH_SHORT).show();
                    } else {
                        // Clear the list before adding new data
                        listingList.clear();

                        // Loop through the documents in Firestore
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Listing listing = doc.toObject(Listing.class);
                            listingList.add(listing);
                        }
                        listingAdapter.notifyDataSetChanged(); // Refresh RecyclerView
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error (log it or show a toast)
                    Toast.makeText(listing1.this, "Error fetching listings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace(); // Log error
                });
    }

    private void selectButton(Button selectedButton) {
        // Unselect other buttons
        myListingsButton.setSelected(false);
        statisticButton.setSelected(false);
        reviewsButton.setSelected(false);
        selectedButton.setSelected(true);
    }
}
