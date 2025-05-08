package com.example.dormspot.OnLaunch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.cardview.widget.CardView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.MainActivitySpottr.Home;
import com.example.dormspot.MainActivitySpottee.listing1;
import com.example.dormspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

        // Check if the user already has a userMode set
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String existingUserMode = documentSnapshot.getString("userMode");
                            if (existingUserMode != null) {
                                // If userMode already exists, navigate to the appropriate activity
                                navigateToNextActivity(existingUserMode);
                            } else {
                                // If userMode is not set, allow them to choose
                                setUpUserModeSelection();
                            }
                        } else {
                            // If no document exists for the user, allow them to choose
                            setUpUserModeSelection();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    /**
     * Sets up the UI and listeners to allow user mode selection.
     */
    private void setUpUserModeSelection() {
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

            // Save selected mode to Firestore
            saveUserModeToFirestore(selectedUserMode);
        });
    }

    /**
     * Saves the selected user mode to Firestore.
     * @param userMode The selected user mode.
     */
    private void saveUserModeToFirestore(String userMode) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(uid)
                    .update("userMode", userMode)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "User mode saved", Toast.LENGTH_SHORT).show();
                        navigateToNextActivity(userMode);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to save user mode", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    /**
     * Navigates the user to the appropriate activity based on the selected user mode.
     * @param userMode The user mode (either "spottr" or "spotee").
     */
    private void navigateToNextActivity(String userMode) {
        Intent intent = "spottr".equals(userMode)
                ? new Intent(this, Home.class)
                : new Intent(this, listing1.class);
        startActivity(intent);
        finish(); // Close this activity after navigating
    }

    /**
     * Helper method to visually indicate selected card.
     */
    private void highlightSelectedCard(CardView selected, CardView unselected) {
        selected.setCardBackgroundColor(getResources().getColor(R.color.light_blue, null));
        unselected.setCardBackgroundColor(getResources().getColor(R.color.dormspot_bg, null));
    }
}
 