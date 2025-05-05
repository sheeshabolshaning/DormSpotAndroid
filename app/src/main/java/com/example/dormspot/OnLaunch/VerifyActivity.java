package com.example.dormspot.OnLaunch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView resendLink;
    private Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);  // Make sure your layout file is named activity_verify.xml

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        resendLink = findViewById(R.id.resend);
        btnRefresh = findViewById(R.id.btnRefreshVerification);

        // Back button behavior
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            auth.signOut(); // Sign out in case user goes back before verification
            finish();
        });

        // Resend verification email
        resendLink.setOnClickListener(v -> {
            if (user != null) {
                user.sendEmailVerification()
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(this, "Verification email sent", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to send email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        // Refresh verification status
        btnRefresh.setOnClickListener(v -> {
            user.reload().addOnSuccessListener(aVoid -> {
                if (user.isEmailVerified()) {
                    Toast.makeText(this, "Email verified! Logging in...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, WelcomeActivity.class)); // or MainActivity
                    finish();
                } else {
                    Toast.makeText(this, "Email not verified yet. Please check your inbox.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
