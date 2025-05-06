package com.example.dormspot.MainAcitivitySpottee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dormspot.R;

public class listing2 extends AppCompatActivity {
    EditText titleInput, locationInput, capacityInput, priceInput, inclusionInput, descriptionInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing2);

        titleInput = findViewById(R.id.editTextTitle);
        locationInput = findViewById(R.id.editTextLocation);
        capacityInput = findViewById(R.id.editTextCapacity);
        priceInput = findViewById(R.id.editTextPrice);
        inclusionInput = findViewById(R.id.editTextInclusion);
        descriptionInput = findViewById(R.id.editTextDescription);

        Button submitBtn = findViewById(R.id.button_submit_listing);
        submitBtn.setOnClickListener(v -> {
            Intent intent = new Intent(listing2.this, listing3.class);
            intent.putExtra("title", titleInput.getText().toString());
            intent.putExtra("location", locationInput.getText().toString());
            intent.putExtra("capacity", capacityInput.getText().toString());
            intent.putExtra("price", priceInput.getText().toString());
            intent.putExtra("inclusion", inclusionInput.getText().toString());
            intent.putExtra("description", descriptionInput.getText().toString());
            startActivity(intent);
        });
    }
}
