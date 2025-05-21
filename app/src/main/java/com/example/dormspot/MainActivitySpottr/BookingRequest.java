package com.example.dormspot.MainActivitySpottr;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dormspot.R;
import com.google.firebase.firestore.*;

import java.util.*;

public class BookingRequest extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingRequestAdapter adapter;
    private List<BookingModel> bookingList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_request);

        recyclerView = findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingRequestAdapter(bookingList, this); // uses BookingModel
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadBookings();
    }

    private void loadBookings() {
        db.collection("booking_requests")
                .whereEqualTo("status", "Pending")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;

                    bookingList.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        BookingModel booking = doc.toObject(BookingModel.class);
                        booking.setBookingId(doc.getId()); // Ensure ID is captured
                        bookingList.add(booking);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
