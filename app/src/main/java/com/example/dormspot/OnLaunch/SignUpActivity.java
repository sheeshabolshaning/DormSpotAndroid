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

        Button btnEmail = findViewById(R.id.btnEmail);


        // Handle the "Sign Up with Email" button click
        Button signUpEmailButton = findViewById(R.id.btnEmail);  // Reference the button by its ID
        signUpEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                Intent intent = new Intent(SignUpActivity.this, RegisterActivity.class);
                startActivity(intent);
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
