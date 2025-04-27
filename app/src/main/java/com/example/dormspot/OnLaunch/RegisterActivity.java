package com.example.dormspot.OnLaunch;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.R;

    public class RegisterActivity extends AppCompatActivity {
        protected void onCreate(Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            Toast.makeText(this, "Register Activity Opened!", Toast.LENGTH_SHORT).show();

        }
    }