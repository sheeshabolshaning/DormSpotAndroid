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

    // ✅ Fixed: Added REVIEWS here
    private enum ViewMode { LISTINGS, STATISTICS, REVIEWS }
    private ViewMode currentMode = ViewMode.LISTINGS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        initViews();
        setupRecyclerView();
        fetchListingsFromFirestore();
        selectButton(myListingsButton);
        setupTabNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentMode == ViewMode.LISTINGS) {
            fetchListingsFromFirestore();
        } else if (currentMode == ViewMode.STATISTICS) {
            fetchStatisticsFromFirestore();
        } else {
            fetchReviewsFromFirestore();
        }
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

    private void setupTabNavigation() {
        myListingsButton.setOnClickListener(v -> {
            selectButton(myListingsButton);
            currentMode = ViewMode.LISTINGS;
            fetchListingsFromFirestore();
        });

        statisticButton.setOnClickListener(v -> {
            selectButton(statisticButton);
            currentMode = ViewMode.STATISTICS;
            fetchStatisticsFromFirestore();
        });

        reviewsButton.setOnClickListener(v -> {
            selectButton(reviewsButton);
            currentMode = ViewMode.REVIEWS;
            fetchReviewsFromFirestore();
        });
    }

    private void fetchListingsFromFirestore() {
        FirebaseFirestore.getInstance().collection("listings")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Listing> data = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Listing listing = doc.toObject(Listing.class);
                        listing.setId(doc.getId());
                        data.add(listing);
                    }

                    if (currentMode == ViewMode.LISTINGS) {
                        listingAdapter = new ListingAdapter(data);
                        recyclerView.setAdapter(listingAdapter);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading listings", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchStatisticsFromFirestore() {
        FirebaseFirestore.getInstance().collection("listings")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Listing> stats = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Listing listing = doc.toObject(Listing.class);
                        listing.setId(doc.getId());
                        stats.add(listing);
                    }

                    if (currentMode == ViewMode.STATISTICS) {
                        StatisticsAdapter statAdapter = new StatisticsAdapter(this, stats);
                        recyclerView.setAdapter(statAdapter);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading statistics", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchReviewsFromFirestore() {
        FirebaseFirestore.getInstance().collection("reviews")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Review> reviews = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Review review = doc.toObject(Review.class);
                        reviews.add(review);
                    }

                    if (currentMode == ViewMode.REVIEWS) {
                        ReviewAdapter reviewAdapter = new ReviewAdapter(this, reviews);
                        recyclerView.setAdapter(reviewAdapter);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
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
