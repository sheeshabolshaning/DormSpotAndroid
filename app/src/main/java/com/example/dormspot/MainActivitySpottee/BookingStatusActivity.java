package com.example.dormspot.MainActivitySpottee;

import android.os.Bundle;
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

public class BookingStatusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingStatusAdapter adapter;
    private final List<BookingSpottee> bookingList = new ArrayList<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_status); // make sure this layout exists

        recyclerView = findViewById(R.id.recyclerViewBookings); // should match layout ID
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookingStatusAdapter(bookingList, this);
        recyclerView.setAdapter(adapter);

        loadBookings();
    }

    private void loadBookings() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("booking_requests")
                .whereEqualTo("spotteeId", currentUserId)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;

                    bookingList.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        BookingSpottee booking = doc.toObject(BookingSpottee.class);
                        if (booking != null) {
                            booking.setBookingId(doc.getId()); // ✅ SET bookingId from Firestore
                            bookingList.add(booking);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
