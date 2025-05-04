package com.example.dormspot.OnLaunch;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText editFirstName, editLastName, editUsername, editEmail, editPassword, editConfirmPassword, editPhoneNumber;
    private Button signUpButton;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Link UI elements (the EditTexts and Button) to the Java variables
        editFirstName = findViewById(R.id.inputFirstName);        // Linking first name input field
        editLastName = findViewById(R.id.inputLastName);          // Linking last name input field
        editUsername = findViewById(R.id.inputUsername);          // Linking username input field
        editEmail = findViewById(R.id.inputEmail);                // Linking email input field
        editPhoneNumber = findViewById(R.id.inputPhoneNumber);    // Linking phone number input field
        editPassword = findViewById(R.id.inputPassword);          // Linking password input field
        editConfirmPassword = findViewById(R.id.inputConfirmPassword);  // Linking confirm password input field

        signUpButton = findViewById(R.id.btnSignUp); // Linking the Sign Up button


        //Back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());


        // Register logic
        signUpButton.setOnClickListener(v -> registerUser());

    }

    private void registerUser() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String userName = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();
        String phoneNumber = editPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(userName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNumber.length() != 11) {
            Toast.makeText(this, "Please enter a valid 11-digit phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user in Firebase Auth
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = auth.getCurrentUser().getUid();

                        // Save user data to Firestore
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("firstName", firstName);
                        userMap.put("lastName", lastName);
                        userMap.put("userName", userName);
                        userMap.put("email", email);
                        userMap.put("phoneNumber", phoneNumber);

                        db.collection("users").document(userId).set(userMap)
                                .addOnSuccessListener(aVoid -> {
                                    // Send verification email
                                    auth.getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(verifyTask -> {
                                                if (verifyTask.isSuccessful()) {
                                                    Toast.makeText(this, "Verification email sent. Please check your inbox.", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(this, VerifyActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(this, "Failed to send verification email: " + verifyTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to save user info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
