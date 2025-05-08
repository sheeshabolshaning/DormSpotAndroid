package com.example.dormspot.MainActivitySpottee;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormspot.R;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {
    private int expandedPosition = -1;
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

        boolean isExpanded = position == expandedPosition;

        if (isExpanded) {
            holder.expandableLayout.setVisibility(View.VISIBLE);
            holder.expandableLayout.setAlpha(0f);
            holder.expandableLayout.animate().alpha(1f).setDuration(300).start();
        } else {
            holder.expandableLayout.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            expandedPosition = isExpanded ? -1 : position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listingList.size();
    }

    public static class ListingViewHolder extends RecyclerView.ViewHolder {
        TextView dormName, dormCapacity, dormPrice, dormStatus;
        TextView location, inclusions, description;
        ImageView dormImage, editButton, imageView1, imageView2;
        LinearLayout expandableLayout;
        Button submitButton;

        public ListingViewHolder(View itemView) {
            super(itemView);
            dormName = itemView.findViewById(R.id.textView_dormName);
            dormCapacity = itemView.findViewById(R.id.textView_dormCapacity);
            dormPrice = itemView.findViewById(R.id.textView_dormPrice);
            dormStatus = itemView.findViewById(R.id.textView_dormStatus);
            dormImage = itemView.findViewById(R.id.imageView_dorm);
            editButton = itemView.findViewById(R.id.button_edit_listing);

            // Expandable views
            location = itemView.findViewById(R.id.textView_location);
            inclusions = itemView.findViewById(R.id.textView_inclusions);
            description = itemView.findViewById(R.id.textView_description);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            imageView1 = itemView.findViewById(R.id.imageView_room1);
            imageView2 = itemView.findViewById(R.id.imageView_room2);
            submitButton = itemView.findViewById(R.id.button_submit);
        }

        public void bind(Listing listing) {
            dormName.setText(listing.getDormName() != null ? listing.getDormName() : "No Name Available");
            dormCapacity.setText("Capacity: " + (listing.getCapacity() != 0 ? listing.getCapacity() : "N/A"));
            dormPrice.setText(formatPrice(listing.getPrice()));
            dormStatus.setText("Status: " + (listing.getStatus() != null ? listing.getStatus() : "Unknown"));

            // Status color coding for pending, passed, rejected
            if (listing.getStatus() != null) {
                switch (listing.getStatus().toLowerCase()) {
                    case "passed":
                        dormStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
                        break;
                    case "rejected":
                        dormStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
                        break;
                    case "pending":
                        dormStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.star_yellow));
                        break;
                    default:
                        dormStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
                        break;
                }
            }

            // Load images
            loadDormImage(listing.getImageUrl(), dormImage);
            loadDormImage(listing.getImageUrl(), imageView1);
            imageView2.setImageResource(R.drawable.placeholder);

            // Set text for expandable fields
            location.setText("Location: " + listing.getLocation());
            inclusions.setText("Inclusions: " + listing.getInclusions());
            description.setText("Description: " + listing.getDescription());

            // Submit button placeholder
            submitButton.setOnClickListener(v -> {
                // Booking or approval logic (future)
            });

            // Edit button (currently opens detail activity)
            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), ListingDetailsActivity.class);
                intent.putExtra("listingId", listing.getId());
                itemView.getContext().startActivity(intent);
            });
        }

        private String formatPrice(double price) {
            return "₱" + String.format("%.2f", price) + "/month";
        }

        private void loadDormImage(String imageUrl, ImageView target) {
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(target);
        }
    }
}