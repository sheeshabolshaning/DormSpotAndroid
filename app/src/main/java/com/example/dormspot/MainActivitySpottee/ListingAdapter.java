package com.example.dormspot.MainActivitySpottee;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.core.content.ContextCompat;
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
        boolean isExpanded = (position == expandedPosition);
        holder.bind(listing, isExpanded);

        holder.itemView.setOnClickListener(v -> {
            expandedPosition = isExpanded ? -1 : position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return listingList.size();
    }

    public class ListingViewHolder extends RecyclerView.ViewHolder {
        TextView dormName, dormCapacity, dormPrice, dormStatus;
        TextView viewLocation, viewInclusion, viewDescription;
        EditText editLocation, editInclusion, editDescription;
        ImageView dormImage, imageView1, imageView2, editButton;
        LinearLayout expandableLayout;
        Button submitButton;

        public ListingViewHolder(View itemView) {
            super(itemView);
            dormName = itemView.findViewById(R.id.textView_dormName);
            dormCapacity = itemView.findViewById(R.id.textView_dormCapacity);
            dormPrice = itemView.findViewById(R.id.textView_dormPrice);
            dormStatus = itemView.findViewById(R.id.textView_dormStatus);

            viewLocation = itemView.findViewById(R.id.textView_location);
            viewInclusion = itemView.findViewById(R.id.textView_inclusions);
            viewDescription = itemView.findViewById(R.id.textView_description);

            editLocation = itemView.findViewById(R.id.edit_location);
            editInclusion = itemView.findViewById(R.id.edit_inclusions);
            editDescription = itemView.findViewById(R.id.edit_description);

            dormImage = itemView.findViewById(R.id.imageView_dorm);
            imageView1 = itemView.findViewById(R.id.imageView_room1);
            imageView2 = itemView.findViewById(R.id.imageView_room2);

            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            editButton = itemView.findViewById(R.id.button_edit_listing);
            submitButton = itemView.findViewById(R.id.button_submit);
        }

        public void bind(Listing listing, boolean isExpanded) {
            dormName.setText(listing.getDormName());
            dormCapacity.setText("Capacity: " + listing.getCapacity());
            dormPrice.setText("₱" + listing.getPrice() + "/month");
            dormStatus.setText("Status: " + listing.getStatus());

            dormStatus.setTextColor(ContextCompat.getColor(itemView.getContext(),
                    "occupied".equalsIgnoreCase(listing.getStatus()) ? R.color.red : R.color.green));

            Glide.with(itemView.getContext())
                    .load(listing.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(dormImage);

            Glide.with(itemView.getContext())
                    .load(listing.getImageUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(imageView1);

            imageView2.setImageResource(R.drawable.placeholder);

            // Expand/Collapse
            expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            viewLocation.setVisibility(View.VISIBLE);
            viewInclusion.setVisibility(View.VISIBLE);
            viewDescription.setVisibility(View.VISIBLE);

            editLocation.setVisibility(View.GONE);
            editInclusion.setVisibility(View.GONE);
            editDescription.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);

            // Set values
            viewLocation.setText("Location: " + listing.getLocation());
            viewInclusion.setText("Inclusions: " + listing.getInclusions());
            viewDescription.setText("Description: " + listing.getDescription());

            editLocation.setText(listing.getLocation());
            editInclusion.setText(listing.getInclusions());
            editDescription.setText(listing.getDescription());

            editButton.setOnClickListener(v -> {
                // Toggle to edit mode
                viewLocation.setVisibility(View.GONE);
                viewInclusion.setVisibility(View.GONE);
                viewDescription.setVisibility(View.GONE);

                editLocation.setVisibility(View.VISIBLE);
                editInclusion.setVisibility(View.VISIBLE);
                editDescription.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.VISIBLE);
            });

            submitButton.setOnClickListener(v -> {
                String newLocation = editLocation.getText().toString().trim();
                String newInclusion = editInclusion.getText().toString().trim();
                String newDescription = editDescription.getText().toString().trim();

                if (TextUtils.isEmpty(newLocation) || TextUtils.isEmpty(newInclusion) || TextUtils.isEmpty(newDescription)) {
                    Toast.makeText(itemView.getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore.getInstance()
                        .collection("listings")
                        .document(listing.getId())
                        .update("location", newLocation,
                                "inclusions", newInclusion,
                                "description", newDescription)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(itemView.getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                            listing.setLocation(newLocation);
                            listing.setInclusions(newInclusion);
                            listing.setDescription(newDescription);

                            notifyItemChanged(getAdapterPosition());
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(itemView.getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                        });
            });
        }
    }
}
