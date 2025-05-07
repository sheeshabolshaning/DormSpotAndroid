package com.example.dormspot.OnLaunch;

import android.widget.ImageButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< Updated upstream
=======
import com.example.dormspot.MainActivitySpottee.listing1;
import com.example.dormspot.MainActivitySpottr.Home;
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
import com.example.dormspot.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

<<<<<<< Updated upstream
        // Handle back button click
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close LoginActivity and go back to the previous screen
=======
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // UI elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        textDontHaveAccount = findViewById(R.id.textDontHaveAccount);
        textForgotPassword = findViewById(R.id.textForgotPassword);
        checkboxRememberMe = findViewById(R.id.checkboxRememberMe);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        textDontHaveAccount.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        SharedPreferences preferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        boolean rememberMe = preferences.getBoolean("rememberMe", false);
        checkboxRememberMe.setChecked(rememberMe);

        if (rememberMe) {
            String savedEmail = preferences.getString("email", "");
            editTextUsername.setText(savedEmail);
        }

        btnSignIn.setOnClickListener(v -> {
            String email = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (checkboxRememberMe.isChecked()) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("rememberMe", true);
                editor.putString("email", email);
                editor.apply();
            } else {
                preferences.edit().clear().apply();
            }

            loginUser(email, password);
        });

        textForgotPassword.setOnClickListener(v -> {
            String email = editTextUsername.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Please enter your email to reset password", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                            }
                        });
>>>>>>> Stashed changes
            }
        });

<<<<<<< Updated upstream
        // TextView listener for "Don't have an account?"
        TextView textView = findViewById(R.id.textDontHaveAccount);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
=======
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            redirectToUserHome(user);
                        } else {
                            Toast.makeText(LoginActivity.this, "Please verify your email first.", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
>>>>>>> Stashed changes
    }

    private void redirectToUserHome(FirebaseUser user) {
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userMode = documentSnapshot.getString("userMode");
                        Intent intent;
                        if ("spottr".equals(userMode)) {
                            intent = new Intent(LoginActivity.this, Home.class);
                        } else if ("spotee".equals(userMode)) {
                            intent = new Intent(LoginActivity.this, listing1.class);
                        } else {
                            intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show();
                });
    }

    private void redirectToUserHome(FirebaseUser user) {
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userMode = documentSnapshot.getString("userMode");
                        Intent intent;
                        if ("spottr".equals(userMode)) {
                            intent = new Intent(LoginActivity.this, Home.class);
                        } else if ("spotee".equals(userMode)) {
                            intent = new Intent(LoginActivity.this, listing1.class);
                        } else {
                            intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show();
                });
    }
}
