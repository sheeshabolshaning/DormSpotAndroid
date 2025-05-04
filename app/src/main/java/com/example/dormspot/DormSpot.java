package com.example.dormspot;
import android.app.Application;
import com.google.firebase.FirebaseApp;
public class DormSpot extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase globally for the entire app
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
    }
}
