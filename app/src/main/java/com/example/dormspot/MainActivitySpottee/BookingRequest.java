package com.example.dormspot.MainActivitySpottee;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BookingRequest extends AppCompatActivity {

    private static final String TAG = "BookingRequest";

    private RecyclerView recyclerView;
    private BookingStatusAdapter adapter;
    private final List<BookingSpottee> bookingList = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check if user is logged in
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "User not authenticated. Aborting activity.");
            finish();
            return;
        }

        // Get current user's UID (landlord)
        String landlordId = auth.getCurrentUser().getUid();
        Log.d(TAG, "Logged in landlord UID: " + landlordId);

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingStatusAdapter(bookingList, this);
        recyclerView.setAdapter(adapter);

        // Load booking requests sent to this landlord
        loadBookingsForLandlord(landlordId);
    }

    private void loadBookingsForLandlord(String landlordId) {
        Log.d(TAG, "Fetching bookings for landlordId = " + landlordId);

        db.collection("booking_requests")
                .whereEqualTo("landlordId", landlordId)
                .get()
                .addOnSuccessListener(snapshots -> {
                    bookingList.clear();

                    if (snapshots != null && !snapshots.isEmpty()) {
                        Log.d(TAG, "Found " + snapshots.size() + " booking(s) for landlord.");

                        for (DocumentSnapshot doc : snapshots.getDocuments()) {
                            BookingSpottee booking = doc.toObject(BookingSpottee.class);
                            if (booking != null) {
                                booking.setBookingId(doc.getId());
                                bookingList.add(booking);
                                Log.d(TAG, "✔ Loaded booking: " + booking.getBookingId());
                            } else {
                                Log.w(TAG, "⚠ Null booking object in document: " + doc.getId());
                            }
                        }

                    } else {
                        Log.d(TAG, "⚠ No bookings found for this landlord.");
                        Toast.makeText(this, "No booking requests yet.", Toast.LENGTH_SHORT).show();
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "❌ Firestore error while fetching landlord bookings", e);
                    Toast.makeText(this, "Failed to load bookings.", Toast.LENGTH_SHORT).show();
                });
    }
}
