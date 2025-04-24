package com.example.dormspot.OnLaunch;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.R;

public class SignUpActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);  // Always call this first!


        // Setup for other buttons (Google, Email) here...
        Button btnFacebook= findViewById(R.id.btnFacebook);
        Button btnGoogle = findViewById(R.id.btnGoogle);
        Button btnEmail = findViewById(R.id.btnEmail);

        // Google button listener
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUpActivity.this, "Google sign-up clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Email button listener
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUpActivity.this, "Email sign-up clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // TextView listener for "Already have an account?"
        TextView textView = findViewById(R.id.textAlreadyHaveAccount);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}
