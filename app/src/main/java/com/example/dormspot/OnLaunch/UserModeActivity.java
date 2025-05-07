package com.example.dormspot.OnLaunch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.cardview.widget.CardView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.MainActivitySpottr.Home;
import com.example.dormspot.MainActivitySpottee.listing;
import com.example.dormspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserModeActivity extends AppCompatActivity {

    private CardView cardViewSpottr, cardViewSpotee;
    private Button buttonGetStarted;
    private String selectedUserMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_mode);

        // Initialize UI components
        cardViewSpottr = findViewById(R.id.cardViewSpottr);
        cardViewSpotee = findViewById(R.id.cardViewSpotee);
        buttonGetStarted = findViewById(R.id.buttonGetStarted);

        // Handle Spottr selection
        cardViewSpottr.setOnClickListener(v -> {
            selectedUserMode = "spottr";
            highlightSelectedCard(cardViewSpottr, cardViewSpotee);
            Toast.makeText(this, "Selected: Spottr", Toast.LENGTH_SHORT).show();
        });

        // Handle Spotee selection
        cardViewSpotee.setOnClickListener(v -> {
            selectedUserMode = "spotee";
            highlightSelectedCard(cardViewSpotee, cardViewSpottr);
            Toast.makeText(this, "Selected: Spotee", Toast.LENGTH_SHORT).show();
        });

        // Handle Get Started button click
        buttonGetStarted.setOnClickListener(v -> {
            if (selectedUserMode == null) {
                Toast.makeText(this, "Please select Spottr or Spotee", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String uid = user.getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> data = new HashMap<>();
                data.put("userMode", selectedUserMode);

                db.collection("users").document(uid)
                        .set(data) // Use set() to create or overwrite the document
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "User mode saved", Toast.LENGTH_SHORT).show();
                            Intent intent = "spottr".equals(selectedUserMode)
                                    ? new Intent(this, Home.class)
                                    : new Intent(this, listing.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to save user mode", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "No user is signed in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Helper method to visually indicate selected card.
     */
    private void highlightSelectedCard(CardView selected, CardView unselected) {
        selected.setCardBackgroundColor(getResources().getColor(R.color.light_blue, null));
        unselected.setCardBackgroundColor(getResources().getColor(R.color.dormspot_bg, null));
    }
}
