package com.example.dormspot.MainActivitySpottee;

import android.content.Intent;
import android.os.Bundle;
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

public class ListingMain extends AppCompatActivity {

    private Button addListingButton, myListingsButton, statisticButton, reviewsButton;
    private RecyclerView recyclerView;
    private ListingAdapter listingAdapter;
    private List<Listing> listingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        // Initialize views
        initViews();

        // Set up RecyclerView
        setupRecyclerView();

        // Fetch Firestore listings
        fetchListingsFromFirestore();

        // Default selected tab
        selectButton(myListingsButton);

        // Tab button listeners
        setupTabNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchListingsFromFirestore(); // Refresh list when returning
    }

    private void initViews() {
        addListingButton = findViewById(R.id.addlisting);
        myListingsButton = findViewById(R.id.my_listings);
        statisticButton = findViewById(R.id.statistic);
        reviewsButton = findViewById(R.id.reviews);

        recyclerView = findViewById(R.id.recyclerViewListings);

        addListingButton.setOnClickListener(v -> {
            Toast.makeText(this, "Add clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ListingAddActivity.class));
        });
    }

    private void setupRecyclerView() {
        listingList = new ArrayList<>();
        listingAdapter = new ListingAdapter(listingList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listingAdapter);
    }

    private void fetchListingsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("listings")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listingList.clear(); // Clear before repopulating

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Listing listing = doc.toObject(Listing.class);
                        listing.setId(doc.getId()); // Use document ID as unique ID
                        listingList.add(listing);
                    }

                    listingAdapter.notifyDataSetChanged(); // Refresh RecyclerView
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching listings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                });
    }

    private void setupTabNavigation() {
        myListingsButton.setOnClickListener(v -> {
            selectButton(myListingsButton);
            // Already on listings, no need to navigate
        });

        statisticButton.setOnClickListener(v -> {
            selectButton(statisticButton);
            // Future: navigate to statistics screen
        });

        reviewsButton.setOnClickListener(v -> {
            selectButton(reviewsButton);
            // Future: navigate to reviews screen
        });
    }

    private void selectButton(Button selectedButton) {
        resetButtons();

        selectedButton.setSelected(true);
        selectedButton.setBackgroundResource(R.drawable.button_selector);
        selectedButton.setTextColor(getResources().getColor(R.color.white));
    }

    private void resetButtons() {
        Button[] buttons = {myListingsButton, statisticButton, reviewsButton};

        for (Button btn : buttons) {
            btn.setSelected(false);
            btn.setBackgroundResource(R.drawable.button_selector);
            btn.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
