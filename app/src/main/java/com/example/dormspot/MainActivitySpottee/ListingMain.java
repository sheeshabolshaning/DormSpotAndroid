package com.example.dormspot.MainActivitySpottee;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListingMain extends AppCompatActivity {

    private Button addListingButton, myListingsButton, statisticButton, reviewsButton;
    private RecyclerView recyclerView;
    private enum ViewMode { LISTINGS, STATISTICS, REVIEWS }
    private ViewMode currentMode = ViewMode.LISTINGS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        initViews();
        setupRecyclerView();
        setupNavigationBar();
        setupTabNavigation();

        selectButton(myListingsButton);
        fetchListingsFromFirestore();

        ImageButton bellButton = findViewById(R.id.nav_notifications);
        bellButton.setOnClickListener(v -> {
            Intent intent = new Intent(ListingMain.this, BookingRequest.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (currentMode) {
            case LISTINGS: fetchListingsFromFirestore(); break;
            case STATISTICS: fetchStatisticsFromFirestore(); break;
            case REVIEWS: fetchReviewsFromFirestore(); break;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Step 1: Get userMode from users collection
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String userMode = documentSnapshot.getString("userMode");

                // Step 2: Fetch listings belonging to the user
                db.collection("listings")
                        .whereEqualTo("landlordId", userId)
                        .get()
                        .addOnSuccessListener(querySnapshot -> {
                            List<Listing> listings = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : querySnapshot) {
                                Listing listing = doc.toObject(Listing.class);
                                listing.setId(doc.getId());
                                listings.add(listing);
                            }

                            // Step 3: Pass the role to the adapter
                            recyclerView.setAdapter(new ListingAdapter(listings, userMode));
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Error loading listings", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "User profile not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to get user role", Toast.LENGTH_SHORT).show();
        });
    }



    private void fetchStatisticsFromFirestore() {
        String landlordId = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("listings")
                .whereEqualTo("landlordId", landlordId) //
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Listing> stats = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Listing listing = doc.toObject(Listing.class);
                        listing.setId(doc.getId());
                        stats.add(listing);
                    }
                    stats.sort((a, b) -> Integer.compare(getStatusRank(a.getStatus()), getStatusRank(b.getStatus())));
                    recyclerView.setAdapter(new StatisticsAdapter(this, stats));
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading statistics", Toast.LENGTH_SHORT).show());
    }


    private void fetchReviewsFromFirestore() {
        String landlordId = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("reviews")
                .whereEqualTo("landlordId", landlordId) // 🔒 Only fetch reviews for this landlord
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Review> reviews = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Review review = doc.toObject(Review.class);
                        reviews.add(review);
                    }
                    recyclerView.setAdapter(new ReviewAdapter(this, reviews));
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load reviews", Toast.LENGTH_SHORT).show());
    }


    private int getStatusRank(String status) {
        if (status == null) return 3;
        switch (status.toLowerCase()) {
            case "occupied": return 0;
            case "unoccupied": return 1;
            case "pending": return 2;
            default: return 3;
        }
    }

    private void selectButton(Button selectedButton) {
        resetButtons();
        selectedButton.setSelected(true);
        selectedButton.setBackgroundResource(R.drawable.button_selector);
        selectedButton.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private void resetButtons() {
        Button[] buttons = {myListingsButton, statisticButton, reviewsButton};
        for (Button btn : buttons) {
            btn.setSelected(false);
            btn.setBackgroundResource(R.drawable.button_selector);
            btn.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    private void setupNavigationBar() {
        ImageView navHome = findViewById(R.id.nav_home);
        ImageView navNearby = findViewById(R.id.nav_nearby);
        ImageView navChat = findViewById(R.id.nav_chat);
        ImageView navBell = findViewById(R.id.nav_notifications);
        ImageView navProfile = findViewById(R.id.nav_profile);

        highlightNavigation(navHome);

        navHome.setOnClickListener(v -> highlightNavigation(navHome));
        navNearby.setOnClickListener(v -> {
            highlightNavigation(navNearby);
            startActivity(new Intent(this, NearbyActivity.class));
        });
        navChat.setOnClickListener(v -> {
            highlightNavigation(navChat);
            startActivity(new Intent(this, ChatActivity.class));
        });
        navBell.setOnClickListener(v -> {
            highlightNavigation(navBell);
            startActivity(new Intent(this, NotificationsActivity.class)); // 🔄 Spottee notification screen
        });

        navProfile.setOnClickListener(v -> {
            highlightNavigation(navProfile);
            startActivity(new Intent(this, ProfileActivity.class));
        });
    }

    private void highlightNavigation(ImageView selected) {
        int activeColor = ContextCompat.getColor(this, R.color.black);
        int inactiveColor = ContextCompat.getColor(this, R.color.white);

        ImageView[] icons = {
                findViewById(R.id.nav_home),
                findViewById(R.id.nav_nearby),
                findViewById(R.id.nav_chat),
                findViewById(R.id.nav_notifications),
                findViewById(R.id.nav_profile)
        };

        for (ImageView icon : icons) {
            icon.setColorFilter(inactiveColor);
            if (icon.getId() == R.id.nav_home) {
                icon.setBackgroundResource(R.drawable.circle_mask);
            } else {
                icon.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        selected.setColorFilter(activeColor);
    }
}
