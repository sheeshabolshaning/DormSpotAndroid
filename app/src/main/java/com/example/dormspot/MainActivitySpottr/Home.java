package com.example.dormspot.MainActivitySpottr;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormspot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DormAdapter adapter;
    private final List<DormItem> dormList = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔧 Firestore & FirebaseAuth setup
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // 📦 RecyclerView setup
        recyclerView = findViewById(R.id.recyclerViewDorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DormAdapter(this, dormList);
        recyclerView.setAdapter(adapter);

        // 🔁 Load and listen to Firestore updates
        loadListings();
    }

    private void loadListings() {
        String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (currentUserId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("listings")
                .addSnapshotListener((querySnapshots, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dormList.clear();
                    if (querySnapshots != null) {
                        for (DocumentSnapshot doc : querySnapshots) {
                            DormItem item = doc.toObject(DormItem.class);
                            if (item != null) {
                                dormList.add(item);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }

    // 🔖 Optional legacy function - no longer needed but kept for reference
    private void applyBookmarkToggleToAll(View parent) {
        if (parent instanceof ImageButton) {
            ImageButton button = (ImageButton) parent;
            CharSequence desc = button.getContentDescription();
            if (desc != null && desc.toString().equalsIgnoreCase("Bookmark")) {
                final boolean[] isBookmarked = {false};
                button.setOnClickListener(v -> {
                    isBookmarked[0] = !isBookmarked[0];
                    button.setImageResource(
                            isBookmarked[0] ? R.drawable.bookmark_filled : R.drawable.bookmark
                    );
                });
            }
        }

        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            for (int i = 0; i < group.getChildCount(); i++) {
                applyBookmarkToggleToAll(group.getChildAt(i));
            }
        }
    }
}
