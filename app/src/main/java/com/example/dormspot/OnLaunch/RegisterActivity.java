package com.example.dormspot.OnLaunch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.R;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toast.makeText(this, "Register Activity Opened!", Toast.LENGTH_SHORT).show();

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // This closes the current activity and returns to the previous one
            }
        });

        // Sign Up button logic
        Button signUpButton = findViewById(R.id.btnSignUp);
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, VerifyActivity.class);
            startActivity(intent);
            // finish(); // Optional if you don't want to return to RegisterActivity
        });
    }
}
