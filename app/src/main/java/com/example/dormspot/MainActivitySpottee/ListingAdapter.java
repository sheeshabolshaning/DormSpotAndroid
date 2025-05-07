package com.example.dormspot.MainActivitySpottee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormspot.R;
import com.google.firebase.firestore.FirebaseFirestore;

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
        EditText locationEdit, inclusionsEdit, descriptionEdit;
        ImageView dormImage, editButton, imageView1, imageView2;
        LinearLayout expandableLayout;
        Button submitButton;
        boolean isEditing = false;

        public ListingViewHolder(View itemView) {
            super(itemView);
            dormName = itemView.findViewById(R.id.textView_dormName);
            dormCapacity = itemView.findViewById(R.id.textView_dormCapacity);
            dormPrice = itemView.findViewById(R.id.textView_dormPrice);
            dormStatus = itemView.findViewById(R.id.textView_dormStatus);
            dormImage = itemView.findViewById(R.id.imageView_dorm);
            editButton = itemView.findViewById(R.id.button_edit_listing);

            // Editable fields
            locationEdit = itemView.findViewById(R.id.editText_location);
            inclusionsEdit = itemView.findViewById(R.id.editText_inclusions);
            descriptionEdit = itemView.findViewById(R.id.editText_description);

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

            // Status color
            if (listing.getStatus() != null) {
                switch (listing.getStatus().toLowerCase()) {
                    case "occupied":
                        dormStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
                        break;
                    case "unoccupied":
                        dormStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.green));
                        break;
                    default:
                        dormStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                        break;
                }
            }

            // Load image
            loadDormImage(listing.getImageUrl(), dormImage);
            loadDormImage(listing.getImageUrl(), imageView1);
            imageView2.setImageResource(R.drawable.placeholder);

            // Fill editable content
            locationEdit.setText(listing.getLocation());
            inclusionsEdit.setText(listing.getInclusions());
            descriptionEdit.setText(listing.getDescription());

            // Default state: not editable
            locationEdit.setEnabled(false);
            inclusionsEdit.setEnabled(false);
            descriptionEdit.setEnabled(false);
            submitButton.setVisibility(View.GONE);

            editButton.setOnClickListener(v -> {
                isEditing = !isEditing;
                locationEdit.setEnabled(isEditing);
                inclusionsEdit.setEnabled(isEditing);
                descriptionEdit.setEnabled(isEditing);
                submitButton.setVisibility(isEditing ? View.VISIBLE : View.GONE);
            });

            submitButton.setOnClickListener(v -> {
                listing.setLocation(locationEdit.getText().toString());
                listing.setInclusions(inclusionsEdit.getText().toString());
                listing.setDescription(descriptionEdit.getText().toString());

                FirebaseFirestore.getInstance().collection("listings")
                        .document(listing.getId())
                        .update("location", listing.getLocation(),
                                "inclusions", listing.getInclusions(),
                                "description", listing.getDescription())
                        .addOnSuccessListener(aVoid -> Toast.makeText(itemView.getContext(), "Listing updated", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(itemView.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                isEditing = false;
                locationEdit.setEnabled(false);
                inclusionsEdit.setEnabled(false);
                descriptionEdit.setEnabled(false);
                submitButton.setVisibility(View.GONE);
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