package com.example.dormspot.MainActivitySpottee;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormspot.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StatisticsAdapter adapter;
    private List<Listing> statisticList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        recyclerView = findViewById(R.id.recyclerViewStatistics);
        statisticList = new ArrayList<>();
        adapter = new StatisticsAdapter(this, statisticList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchListingsFromFirestore();
    }

    private void fetchListingsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("listings")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    statisticList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Listing listing = doc.toObject(Listing.class);
                        listing.setId(doc.getId()); // Use document ID as unique ID
                        statisticList.add(listing);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load listings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
