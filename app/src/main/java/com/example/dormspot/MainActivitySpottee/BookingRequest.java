package com.example.dormspot.MainActivitySpottee;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

    private RecyclerView recyclerView;
    private List<BookingSpottee> bookingList = new ArrayList<>();
    private BookingStatusAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.notifications); // Your layout must have recyclerViewBookings

        recyclerView = findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingStatusAdapter(bookingList, this);
        recyclerView.setAdapter(adapter);

        loadBookings();
    }

    private void loadBookings() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("booking_requests")
                .whereEqualTo("spotteeId", uid) // ✅ Correct: spottee views their own requests
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Error fetching bookings", e);
                        Toast.makeText(this, "Failed to load bookings.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    bookingList.clear();

                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots) {
                            BookingSpottee booking = doc.toObject(BookingSpottee.class);
                            if (booking != null) {
                                booking.setBookingId(doc.getId()); // ✅ Important for update logic
                                bookingList.add(booking);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}
