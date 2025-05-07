package com.example.dormspot.MainActivitySpottee;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormspot.R;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {

    private List<Listing> listingList;

    public ListingAdapter(List<Listing> listingList) {
        this.listingList = listingList;
    }

    @Override
    public ListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item view for each listing
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listing, parent, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListingViewHolder holder, int position) {
        // Bind data to the views in the ViewHolder
        Listing listing = listingList.get(position);
        holder.bind(listing);
    }

    @Override
    public int getItemCount() {
        return listingList.size();
    }

    public static class ListingViewHolder extends RecyclerView.ViewHolder {
        TextView dormName, dormCapacity, dormPrice, dormStatus;
        ImageView dormImage, editButton;

        public ListingViewHolder(View itemView) {
            super(itemView);
            dormName = itemView.findViewById(R.id.textView_dormName);
            dormCapacity = itemView.findViewById(R.id.textView_dormCapacity);
            dormPrice = itemView.findViewById(R.id.textView_dormPrice);
            dormStatus = itemView.findViewById(R.id.textView_dormStatus);
            dormImage = itemView.findViewById(R.id.imageView_dorm);
            editButton = itemView.findViewById(R.id.button_edit_listing);  // ImageView for edit
        }

        public void bind(Listing listing) {
            // Set dorm name, capacity, price, and status, with null safety
            dormName.setText(listing.getDormName() != null ? listing.getDormName() : "No Name Available");
            dormCapacity.setText("Capacity: " + (listing.getCapacity() != 0 ? listing.getCapacity() : "N/A"));
            dormPrice.setText(formatPrice(listing.getPrice() != 0 ? listing.getPrice() : 0));
            dormStatus.setText("Status: " + (listing.getStatus() != null ? listing.getStatus() : "Unknown"));

            // Change text color based on the dorm status
            if (listing.getStatus() != null) {
                switch (listing.getStatus().toLowerCase()) {
                    case "occupied":
                        dormStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.red)); // Red color
                        break;
                    case "unoccupied":
                        dormStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.green)); // Green color
                        break;
                    default:
                        dormStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.black)); // Default color (black or any other color)
                        break;
                }
            }

            // Load dorm image using Glide
            loadDormImage(listing.getImageUrl());

            // Set edit button click listener to navigate to listing2 activity
            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), listing2.class);
                intent.putExtra("listingId", listing.getId()); // Pass the listing ID to listing2 activity
                itemView.getContext().startActivity(intent);
            });
        }

        // Helper method to format the price
        private String formatPrice(double price) {
            return "₱" + String.format("%.2f", price) + "/month";
        }

        // Helper method to load the dorm image using Glide
        private void loadDormImage(String imageUrl) {
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder) // Use your placeholder image
                    .into(dormImage);
        }
    }
}
