package com.example.dormspot.OnLaunch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.dormspot.R;
import com.example.dormspot.MainActivitySpottr.Home;
import com.example.dormspot.OnLaunch.LoginActivity;
public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = auth.getCurrentUser();

            if (currentUser != null && currentUser.isEmailVerified()) {
                Intent intent = new Intent(SplashActivity.this, Home.class); // Correct target
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent);
            }

            finish();
        }, 3000);
    }
}
