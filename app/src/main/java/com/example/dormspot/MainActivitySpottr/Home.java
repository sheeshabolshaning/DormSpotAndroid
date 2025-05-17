package com.example.dormspot.MainActivitySpottr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormspot.MainActivitySpottee.Listing;
import com.example.dormspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements DormAdapter.OnDormClickListener {

    private RecyclerView recyclerView;
    private DormAdapter adapter;
    private List<Listing> dormList = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Check if user is logged in
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Optional: prevent using Home if not logged in
            return;
        }

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerViewDorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DormAdapter(this, dormList, this); // pass 'this' as click listener
        recyclerView.setAdapter(adapter);

        loadListings();
    }

    private void loadListings() {
        db.collection("listings")
                .whereEqualTo("status", "approved")
                .addSnapshotListener((querySnapshots, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dormList.clear();
                    if (querySnapshots != null) {
                        for (DocumentSnapshot doc : querySnapshots) {
                            Listing item = doc.toObject(Listing.class);
                            if (item != null) {
                                item.setId(doc.getId());
                                dormList.add(item);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onDormClick(String listingId) {
        // Find the clicked listing by ID
        Listing clickedListing = null;
        for (Listing l : dormList) {
            if (l.getId().equals(listingId)) {
                clickedListing = l;
                break;
            }
        }

        if (clickedListing == null) {
            Toast.makeText(this, "Listing not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pass full dorm data to Booking activity
        Intent intent = new Intent(Home.this, Booking.class);
        intent.putExtra("listingId", clickedListing.getId());
        intent.putExtra("dormName", clickedListing.getDormName());
        intent.putExtra("price", clickedListing.getPrice());
        intent.putExtra("capacity", clickedListing.getCapacity());
        intent.putExtra("status", clickedListing.getStatus());
        intent.putExtra("description", clickedListing.getDescription());
        intent.putExtra("imageUrl", clickedListing.getImageUrl());
        intent.putExtra("landlordId", clickedListing.getLandlordId()); // ✅ important
        startActivity(intent);
    }

    // Optional legacy function
    private void applyBookmarkToggleToAll(View parent) {
        if (parent instanceof ImageButton) {
            ImageButton button = (ImageButton) parent;
            CharSequence desc = button.getContentDescription();
            if (desc != null && desc.toString().equalsIgnoreCase("Bookmark")) {
                final boolean[] isBookmarked = {false};
                button.setOnClickListener(v -> {
                    isBookmarked[0] = !isBookmarked[0];
                    button.setImageResource(
                            isBookmarked[0] ? R.drawable.bookmark_filled : R.drawable.bookmark
                    );
                });
            }
        }

        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            for (int i = 0; i < group.getChildCount(); i++) {
                applyBookmarkToggleToAll(group.getChildAt(i));
            }
        }
    }
}
