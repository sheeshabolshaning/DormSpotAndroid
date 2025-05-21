package com.example.dormspot.AdminActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormspot.MainActivitySpottee.Listing;
import com.example.dormspot.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    private View indicator;
    private RecyclerView listingRecyclerView;
    private AdminListingAdapter adapter;
    private final List<Listing> listingList = new ArrayList<>();

    private Button buttonPending, buttonApproved, buttonRejected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_home_spottee);

        // Initialize views
        buttonPending = findViewById(R.id.button_pending);
        buttonApproved = findViewById(R.id.button_approved);
        buttonRejected = findViewById(R.id.button_rejected);
        indicator = findViewById(R.id.indicator);

        // Setup RecyclerView
        listingRecyclerView = findViewById(R.id.listingRecyclerView);
        listingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminListingAdapter(listingList);
        listingRecyclerView.setAdapter(adapter);

        // Tab filter buttons
        buttonPending.setOnClickListener(v -> {
            loadListingsByStatus("Pending");
            highlightButton(buttonPending);
        });

        buttonApproved.setOnClickListener(v -> {
            loadListingsByStatus("Approved");
            highlightButton(buttonApproved);
        });

        buttonRejected.setOnClickListener(v -> {
            loadListingsByStatus("Rejected");
            highlightButton(buttonRejected);
        });

        // Default load
        buttonPending.post(() -> {
            loadListingsByStatus("Pending");
            highlightButton(buttonPending);
        });
    }

    private void loadListingsByStatus(String rawStatus) {
        String targetStatus = rawStatus.trim().toLowerCase(); // Normalize input

        FirebaseFirestore.getInstance()
                .collection("listings")
                .get() // Fetch all listings
                .addOnSuccessListener(snapshots -> {
                    listingList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        Listing listing = doc.toObject(Listing.class);
                        if (listing != null && listing.getStatus() != null &&
                                listing.getStatus().trim().equalsIgnoreCase(targetStatus)) {

                            listing.setId(doc.getId());
                            listingList.add(listing);
                            Log.d("AdminHome", "Loaded: " + listing.getDormName() + " (" + listing.getStatus() + ")");
                        }
                    }

                    adapter.notifyDataSetChanged();

                    if (listingList.isEmpty()) {
                        Toast.makeText(this, "No " + capitalizeStatus(rawStatus) + " listings found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("AdminHome", "Error fetching listings", e);
                    Toast.makeText(this, "Failed to fetch listings.", Toast.LENGTH_SHORT).show();
                });
    }


    private String capitalizeStatus(String text) {
        if (text == null || text.isEmpty()) return text;
        text = text.trim().toLowerCase();
        switch (text) {
            case "pending":
                return "Pending";
            case "approved":
                return "Approved";
            case "rejected":
                return "Rejected";
            default:
                return text;
        }
    }

    private void highlightButton(Button selectedButton) {
        Button[] buttons = {buttonPending, buttonApproved, buttonRejected};
        for (Button b : buttons) {
            b.setSelected(b == selectedButton);
            b.setTextColor(b == selectedButton
                    ? ContextCompat.getColor(this, R.color.white)
                    : ContextCompat.getColor(this, R.color.black));
        }

        moveIndicatorTo(selectedButton);
    }

    private void moveIndicatorTo(Button target) {
        int width = target.getWidth();
        int x = target.getLeft();

        indicator.animate()
                .x(x)
                .setDuration(300)
                .start();

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) indicator.getLayoutParams();
        params.width = width;
        indicator.setLayoutParams(params);
    }
}
