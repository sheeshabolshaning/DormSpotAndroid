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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listing, parent, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListingViewHolder holder, int position) {
        Listing listing = listingList.get(position);
        holder.bind(listing);
    }

    @Override
    public int getItemCount() {
        return listingList.size();
    }

    public static class ListingViewHolder extends RecyclerView.ViewHolder {
        TextView dormName, dormCapacity, dormPrice, dormStatus, dormLandlord;
        ImageView dormImage, editButton;

        public ListingViewHolder(View itemView) {
            super(itemView);
            dormName = itemView.findViewById(R.id.textView_dormName);
            dormCapacity = itemView.findViewById(R.id.textView_dormCapacity);
            dormPrice = itemView.findViewById(R.id.textView_dormPrice);
            dormStatus = itemView.findViewById(R.id.textView_dormStatus);
            dormLandlord = itemView.findViewById(R.id.textView_dormLandlord);
            dormImage = itemView.findViewById(R.id.imageView_dorm);
            editButton = itemView.findViewById(R.id.button_edit_listing);  // ImageView for edit
        }

        public void bind(Listing listing) {
            dormName.setText(listing.getDormName());
            dormCapacity.setText("Capacity: " + listing.getCapacity());
            dormPrice.setText(formatPrice(listing.getPrice()));
            dormStatus.setText("Status: " + listing.getStatus());
            dormLandlord.setText("Landlord: " + listing.getLandlord());

            loadDormImage(listing.getImageUrl());

            // Set edit button click listener
            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), listing2.class);
                intent.putExtra("listingId", listing.getId()); // Pass the listing ID to listing2 activity
                itemView.getContext().startActivity(intent);
            });
        }

        // Helper method for price formatting
        private String formatPrice(double price) {
            return "₱" + String.format("%.2f", price) + "/month";
        }

        // Helper method to load image using Glide
        private void loadDormImage(String imageUrl) {
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder) // Use your placeholder image
                    .into(dormImage);
        }
    }
}
