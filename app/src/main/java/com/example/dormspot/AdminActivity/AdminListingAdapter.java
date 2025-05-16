package com.example.dormspot.AdminActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormspot.MainActivitySpottee.Listing;
import com.example.dormspot.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdminListingAdapter extends RecyclerView.Adapter<AdminListingAdapter.ListingViewHolder> {

    private final List<Listing> listingList;

    public AdminListingAdapter(List<Listing> listingList) {
        this.listingList = listingList;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_spottee, parent, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Listing listing = listingList.get(position);

        holder.dormName.setText(listing.getDormName());
        holder.location.setText("Location: " + listing.getLocation());
        holder.adminStatus.setText("Status: " + listing.getStatus());
        holder.occupancyStatus.setText("Occupancy: " + listing.getOccupancyStatus());
        holder.activeProperties.setText("Active Properties: 3"); // Optional: replace with real count
        holder.postLabel.setText(listing.getDormName()); // Updated to show dorm name
        holder.description.setText(listing.getDescription()); // Shows description instead of placeholder

        // Approve button
        holder.approveButton.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("listings")
                    .document(listing.getId())
                    .update("status", "approved")
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(holder.itemView.getContext(), "Listing approved!", Toast.LENGTH_SHORT).show();
                        listingList.remove(position);
                        notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(holder.itemView.getContext(), "Failed to approve listing.", Toast.LENGTH_SHORT).show()
                    );
        });

// Reject button
        holder.rejectButton.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("listings")
                    .document(listing.getId())
                    .update("status", "rejected")
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(holder.itemView.getContext(), "Listing rejected.", Toast.LENGTH_SHORT).show();
                        listingList.remove(position);
                        notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(holder.itemView.getContext(), "Failed to reject listing.", Toast.LENGTH_SHORT).show()
                    );
        });

    }

    @Override
    public int getItemCount() {
        return listingList.size();
    }

    public static class ListingViewHolder extends RecyclerView.ViewHolder {
        TextView dormName, location, adminStatus, occupancyStatus, activeProperties, postLabel, description;
        Button approveButton, rejectButton;

        public ListingViewHolder(@NonNull View itemView) {
            super(itemView);
            dormName = itemView.findViewById(R.id.textView_dormName);
            location = itemView.findViewById(R.id.textView_location);
            adminStatus = itemView.findViewById(R.id.textView_textAdminStatus);
            occupancyStatus = itemView.findViewById(R.id.textOccupancyStatus);
            activeProperties = itemView.findViewById(R.id.textView_activeProperties);
            postLabel = itemView.findViewById(R.id.textView_postLabel); // should be a dedicated TextView for post
            description = itemView.findViewById(R.id.textView_description); // should exist in your layout
            approveButton = itemView.findViewById(R.id.button_approve);
            rejectButton = itemView.findViewById(R.id.button_reject);
        }
    }
}