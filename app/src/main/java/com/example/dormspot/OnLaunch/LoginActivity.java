package com.example.dormspot.OnLaunch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.AdminActivity.AdminHomeActivity;
import com.example.dormspot.MainActivitySpottr.Home;
import com.example.dormspot.MainActivitySpottee.ListingMain;
import com.example.dormspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextUsername, editTextPassword;
    private Button btnSignIn;
    private TextView textDontHaveAccount, textForgotPassword;
    private CheckBox checkboxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

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
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("rememberMe", false);
                editor.apply();
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
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            String uid = user.getUid();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(uid).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            String userMode = documentSnapshot.getString("userMode");

                                            if ("admin".equals(userMode)) {
                                                startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                                            } else if ("spottr".equals(userMode)) {
                                                startActivity(new Intent(LoginActivity.this, Home.class));
                                            } else if ("spotee".equals(userMode)) {
                                                startActivity(new Intent(LoginActivity.this, ListingMain.class));
                                            } else {
                                                startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                                            }
                                            finish();
                                        } else {
                                            Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show();
                                    });

                        } else {
                            Toast.makeText(LoginActivity.this, "Please verify your email first.", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
