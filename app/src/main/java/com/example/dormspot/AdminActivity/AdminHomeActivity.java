package com.example.dormspot.AdminActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormspot.MainActivitySpottee.Listing;
import com.example.dormspot.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    private RecyclerView listingRecyclerView;
    private AdminListingAdapter adapter; // ✅ Correct adapter
    private List<Listing> listingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_home_spottee);

        listingRecyclerView = findViewById(R.id.listingRecyclerView);
        listingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        listingList = new ArrayList<>();
        adapter = new AdminListingAdapter(listingList);
        listingRecyclerView.setAdapter(adapter);

        FirebaseFirestore.getInstance()
                .collection("listings")
                .whereEqualTo("status", "pending")
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Listen failed.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    listingList.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        Listing listing = doc.toObject(Listing.class);
                        if (listing != null) {
                            listing.setId(doc.getId()); // important for updating
                            listingList.add(listing);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
