package com.example.dormspot.MainActivitySpottr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.dormspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.util.Log;
import android.widget.Toast;

public class Booking extends AppCompatActivity {

    private ViewPager2 viewPager2;
    public ImagePagerAdapter adapter;
    private Button bookNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);

        // ✅ Get data from intent
        Intent intent = getIntent();
        String dormName = intent.getStringExtra("dormName");
        int capacity = intent.getIntExtra("capacity", 0);
        int price = intent.getIntExtra("price", 0);
        String status = intent.getStringExtra("status");
        String description = intent.getStringExtra("description");
        String listingId = intent.getStringExtra("listingId");
        String imageUrl = intent.getStringExtra("imageUrl");

        // ✅ Check for listing ID
        if (listingId == null || listingId.isEmpty()) {
            Toast.makeText(this, "Missing listing ID.", Toast.LENGTH_SHORT).show();
            finish(); // Optional: exit if ID is critical
            return;
        }

        // ✅ Log the listing ID for debugging
        android.util.Log.d("BookingDebug", "Received listingId: " + listingId);

        // ✅ Bind views
        TextView title = findViewById(R.id.dormTitleText);
        TextView priceText = findViewById(R.id.priceText);
        TextView descriptionText = findViewById(R.id.descriptionText);
        bookNowBtn = findViewById(R.id.bookNowBtn);

        // ✅ Set values
        title.setText(dormName != null ? dormName : "No Name");
        priceText.setText("₱" + price + "/month");
        descriptionText.setText(description != null ? description : "No description");

        // ✅ Load image (replace with ViewPager if needed)
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.imageholder)
                .into(new ImageView(this)); // Not displayed but avoids crash

        // ✅ Disable Book button if already occupied
        if ("occupied".equalsIgnoreCase(status)) {
            bookNowBtn.setEnabled(false);
            bookNowBtn.setText("Already Occupied");
            bookNowBtn.setAlpha(0.6f);
        } else {
            // ✅ Enable booking with dialog using listingId
            bookNowBtn.setOnClickListener(v -> showCustomConfirmationDialog(listingId));
        }

        // ✅ Back button behavior
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(Booking.this, Home.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(backIntent);
            finish();
        });

        // ✅ ViewPager2 image setup
        viewPager2 = findViewById(R.id.propertyImagePager);
        List<Integer> imageResources = Arrays.asList(
                R.drawable.house,
                R.drawable.house,
                R.drawable.house
        );
        adapter = new ImagePagerAdapter(imageResources);
        viewPager2.setAdapter(adapter);
    }

    // ✅ Custom Confirmation Dialog Method
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
            Toast.makeText(Booking.this, "Missing user or listing ID.", Toast.LENGTH_SHORT).show();
            Log.d("BookingDebug", "user: " + currentUser + " | listingId: " + listingId);
            return;
        }

        String userId = currentUser.getUid();
        String userName = currentUser.getDisplayName(); // Optional: Pull from Firestore users collection
        String dormName = getIntent().getStringExtra("dormName");
        int price = getIntent().getIntExtra("price", 0);
        String landlordId = getIntent().getStringExtra("landlordId"); // You need to pass this via Intent when opening Booking.java

        // Generate booking document ID
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String bookingId = db.collection("booking_requests").document().getId();

        Map<String, Object> booking = new HashMap<>();
        booking.put("bookingId", bookingId);
        booking.put("userId", userId);
        booking.put("userName", userName != null ? userName : "Unknown User");
        booking.put("dormName", dormName != null ? dormName : "N/A");
        booking.put("totalPrice", "₱" + price);
        booking.put("landlordId", landlordId != null ? landlordId : "N/A");
        booking.put("status", "pending");
        booking.put("listingId", listingId);
        booking.put("bookingDates", "2025-05-20 - 2025-06-20"); // Static for now; replace with DatePicker input if needed
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
