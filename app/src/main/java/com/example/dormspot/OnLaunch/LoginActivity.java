package com.example.dormspot.OnLaunch;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextUsername, editTextPassword;
    private Button btnSignIn, btnFacebook, btnGoogle;
    private TextView textDontHaveAccount, textForgotPassword;
    private CheckBox checkboxRememberMe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Get references for the UI elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        textDontHaveAccount = findViewById(R.id.textDontHaveAccount);
        textForgotPassword = findViewById(R.id.textForgotPassword);
        checkboxRememberMe = findViewById(R.id.checkboxRememberMe);



        // Back button to close LoginActivity
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Navigate to RegisterActivity when the "Don't have an account?" text is clicked
        textDontHaveAccount.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));


        // Sign in button logic
        btnSignIn.setOnClickListener(v -> {
            String email = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });
        // Forgot password functionality
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
                        // Login success
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                        // Redirect to the main activity or home screen
                        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Exception e = task.getException();
                        e.printStackTrace();
                        // Login failed
                        Toast.makeText(LoginActivity.this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
