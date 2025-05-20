package com.example.dormspot.MainActivitySpottr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dormspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Booking extends AppCompatActivity {

    private Button bookNowBtn;
    private long priceValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);

        // ✅ Get data from intent
        Intent intent = getIntent();
        String dormName = intent.getStringExtra("dormName");
        int capacity = intent.getIntExtra("capacity", 0);
        priceValue = getIntent().hasExtra("price")
                ? Math.round(getIntent().getDoubleExtra("price", 0.0))
                : 0;
        String status = intent.getStringExtra("status");
        String description = intent.getStringExtra("description");
        String listingId = intent.getStringExtra("listingId");
        String imageUrl = intent.getStringExtra("imageUrl");

        if (listingId == null || listingId.isEmpty()) {
            Toast.makeText(this, "Missing listing ID.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("BookingDebug", "Received listingId: " + listingId);
        Log.d("BookingDebug", "Received price: " + priceValue);

        // ✅ Bind views
        TextView title = findViewById(R.id.dormTitleText);
        TextView priceText = findViewById(R.id.priceText);
        TextView descriptionText = findViewById(R.id.descriptionText);
        bookNowBtn = findViewById(R.id.bookNowBtn);

        // ✅ Format and set values
        title.setText(dormName != null ? dormName : "No Name");

        String formattedPrice = "₱" + NumberFormat.getNumberInstance(Locale.US).format(priceValue) + ".00/month";
        priceText.setText(formattedPrice);

        descriptionText.setText(description != null ? description : "No description");

        // ✅ Image loading (optional fallback)
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.imageholder)
                    .into(new ImageView(this)); // Load silently; not displayed
        }

        // ✅ Book button logic
        if ("occupied".equalsIgnoreCase(status)) {
            bookNowBtn.setEnabled(false);
            bookNowBtn.setText("Already Occupied");
            bookNowBtn.setAlpha(0.6f);
        } else {
            bookNowBtn.setOnClickListener(v -> showCustomConfirmationDialog(listingId));
        }

        // ✅ Back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(Booking.this, Home.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(backIntent);
            finish();
        });
    }

    private void showCustomConfirmationDialog(String listingId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm_booking, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        Button yesBtn = dialogView.findViewById(R.id.yesBtn);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        yesBtn.setOnClickListener(v -> {
            sendBookingRequest(listingId);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void sendBookingRequest(String listingId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null || listingId == null || listingId.isEmpty()) {
            Toast.makeText(this, "Missing user or listing ID.", Toast.LENGTH_SHORT).show();
            Log.d("BookingDebug", "user: " + currentUser + " | listingId: " + listingId);
            return;
        }

        String userId = currentUser.getUid();
        String userName = currentUser.getDisplayName();
        String dormName = getIntent().getStringExtra("dormName");
        String landlordId = getIntent().getStringExtra("landlordId");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String bookingId = db.collection("booking_requests").document().getId();

        String formattedPrice = "₱" + NumberFormat.getNumberInstance(Locale.US).format(priceValue) + ".00";

        Map<String, Object> booking = new HashMap<>();
        booking.put("bookingId", bookingId);
        booking.put("userId", userId);
        booking.put("userName", userName != null ? userName : "Unknown User");
        booking.put("dormName", dormName != null ? dormName : "N/A");
        booking.put("totalPrice", formattedPrice);
        booking.put("landlordId", landlordId != null ? landlordId : "N/A");
        booking.put("status", "pending");
        booking.put("listingId", listingId);
        booking.put("bookingDates", "2025-05-20 - 2025-06-20");
        booking.put("timestamp", FieldValue.serverTimestamp());

        db.collection("booking_requests").document(bookingId)
                .set(booking)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Booking request sent!", Toast.LENGTH_SHORT).show();
                    bookNowBtn.setEnabled(false);
                    bookNowBtn.setText("Pending Approval");
                    bookNowBtn.setAlpha(0.6f);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
