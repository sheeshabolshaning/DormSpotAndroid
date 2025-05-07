package com.example.dormspot.OnLaunch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.R;
<<<<<<< Updated upstream
<<<<<<< Updated upstream

public class SplashActivity extends AppCompatActivity {
=======
import com.example.dormspot.OnLaunch.LoginActivity;
>>>>>>> Stashed changes

public class SplashActivity extends AppCompatActivity {
=======
import com.example.dormspot.OnLaunch.LoginActivity;

public class SplashActivity extends AppCompatActivity {
>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

<<<<<<< Updated upstream
<<<<<<< Updated upstream
        // Delay before starting the SignUpActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to start the SignUpActivity
                Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent);

                // Close SplashActivity so that it doesn't remain in the stack
                finish();
            }
        }, 3000); // 3 seconds delay (you can adjust the time)
=======
=======
>>>>>>> Stashed changes
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
>>>>>>> Stashed changes
    }
}