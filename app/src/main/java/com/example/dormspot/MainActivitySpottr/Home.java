    package com.example.dormspot.MainActivitySpottr;

    import android.os.Bundle;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.ImageButton;
    import android.widget.Toast;
    import android.content.Intent;


    import androidx.activity.EdgeToEdge;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.app.AppCompatDelegate;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import com.example.dormspot.R;

    public class Home extends AppCompatActivity {

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

            // 🔁 Bookmark Toggle Setup
            applyBookmarkToggleToAll(findViewById(R.id.main));

            // ✅ View Dorm Button Listener
            Button viewDormButton = findViewById(R.id.viewDorm1); // or viewDorm2
            viewDormButton.setOnClickListener(v -> {
                Intent intent = new Intent(Home.this, Booking.class);
                startActivity(intent);
            });
        }

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
