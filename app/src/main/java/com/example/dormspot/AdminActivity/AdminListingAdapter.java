package com.example.dormspot.AdminActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

        // Set basic info
        holder.dormName.setText(listing.getDormName());
        holder.location.setText("Location: " + listing.getLocation());
        holder.adminStatus.setText("Status: " + listing.getStatus());
        holder.occupancyStatus.setText("Occupancy: " + listing.getOccupancyStatus());
        holder.activeProperties.setText("Active Properties: 3");
        holder.postLabel.setText("Post: " + listing.getDormName());
        holder.description.setText(listing.getDescription());

        // Get status and occupancy
        String status = listing.getStatus() != null ? listing.getStatus().toLowerCase() : "";
        String occupancy = listing.getOccupancyStatus() != null ? listing.getOccupancyStatus().toLowerCase() : "";

        // Status color
        switch (status) {
            case "approved":
                holder.adminStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                break;
            case "pending":
                holder.adminStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.pending_yellow));
                break;
            case "rejected":
                holder.adminStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
                break;
            default:
                holder.adminStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                break;
        }

        // Occupancy color
        switch (occupancy) {
            case "occupied":
                holder.occupancyStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.occupied_red));
                break;
            case "unoccupied":
                holder.occupancyStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.unoccupied_green));
                break;
            default:
                holder.occupancyStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
                break;
        }

        // Hide buttons if not pending
        boolean isPending = status.equals("pending");
        holder.approveButton.setVisibility(isPending ? View.VISIBLE : View.GONE);
        holder.rejectButton.setVisibility(isPending ? View.VISIBLE : View.GONE);

        // Approve button logic
        holder.approveButton.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("listings")
                    .document(listing.getId())
                    .update("status", "approved")
                    .addOnSuccessListener(aVoid -> {
                        listing.setStatus("approved");
                        animateStatusChange(holder);

                        holder.adminStatus.setText("Status: approved");
                        holder.adminStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
                        holder.approveButton.setVisibility(View.GONE);
                        holder.rejectButton.setVisibility(View.GONE);

                        Toast.makeText(holder.itemView.getContext(), "Listing approved!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("AdminListingAdapter", "Approval failed", e);
                        Toast.makeText(holder.itemView.getContext(), "Failed to approve listing.", Toast.LENGTH_SHORT).show();
                    });
        });

        // Reject button logic
        holder.rejectButton.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("listings")
                    .document(listing.getId())
                    .update("status", "rejected")
                    .addOnSuccessListener(aVoid -> {
                        listing.setStatus("rejected");
                        animateStatusChange(holder);

                        holder.adminStatus.setText("Status: rejected");
                        holder.adminStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
                        holder.approveButton.setVisibility(View.GONE);
                        holder.rejectButton.setVisibility(View.GONE);

                        Toast.makeText(holder.itemView.getContext(), "Listing rejected.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("AdminListingAdapter", "Rejection failed", e);
                        Toast.makeText(holder.itemView.getContext(), "Failed to reject listing.", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void animateStatusChange(ListingViewHolder holder) {
        holder.adminStatus.setAlpha(0f);
        holder.adminStatus.animate()
                .alpha(1f)
                .setDuration(400)
                .start();
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
            postLabel = itemView.findViewById(R.id.textView_postLabel);
            description = itemView.findViewById(R.id.textView_description);
            approveButton = itemView.findViewById(R.id.button_approve);
            rejectButton = itemView.findViewById(R.id.button_reject);
        }
    }
}
