package com.example.dormspot.AdminActivity;

import android.os.Bundle;
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
    private List<Listing> listingList;

    private Button buttonPending, buttonApproved, buttonRejected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_home_spottee);

        // Buttons and indicator
        buttonPending = findViewById(R.id.button_pending);
        buttonApproved = findViewById(R.id.button_approved);
        buttonRejected = findViewById(R.id.button_rejected);
        indicator = findViewById(R.id.indicator); // View for animated tab underline

        // RecyclerView setup
        listingRecyclerView = findViewById(R.id.listingRecyclerView);
        listingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        listingList = new ArrayList<>();
        adapter = new AdminListingAdapter(listingList);
        listingRecyclerView.setAdapter(adapter);

        // Button listeners
        buttonPending.setOnClickListener(v -> {
            loadListingsByStatus("pending");
            highlightButton(buttonPending);
        });

        buttonApproved.setOnClickListener(v -> {
            loadListingsByStatus("approved");
            highlightButton(buttonApproved);
        });

        buttonRejected.setOnClickListener(v -> {
            loadListingsByStatus("rejected");
            highlightButton(buttonRejected);
        });

        // Load default tab
        buttonPending.post(() -> {
            loadListingsByStatus("pending");
            highlightButton(buttonPending);
        });
    }

    private void loadListingsByStatus(String status) {
        FirebaseFirestore.getInstance()
                .collection("listings")
                .whereEqualTo("status", status)
                .get()
                .addOnSuccessListener(snapshots -> {
                    listingList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        Listing listing = doc.toObject(Listing.class);
                        if (listing != null) {
                            listing.setId(doc.getId());
                            listingList.add(listing);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to fetch " + status + " listings.", Toast.LENGTH_SHORT).show()
                );
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
