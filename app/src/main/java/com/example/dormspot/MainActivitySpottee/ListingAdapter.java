package com.example.dormspot.MainActivitySpottee;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.*;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dormspot.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {
    private int expandedPosition = -1;
    private final List<Listing> listingList;
    private final String userRole;

    public ListingAdapter(List<Listing> listingList, String userRole) {
        this.listingList = listingList;
        this.userRole = userRole;
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
        TextView dormName, dormCapacity, dormPrice;
        TextView adminStatusText, occupancyStatusText;
        TextView viewLocation, viewLandmark, viewInclusion, viewDescription;
        EditText editLocation, editInclusion, editDescription, editLandmark, editOccupancy;
        ImageView editButton;
        LinearLayout expandableLayout;
        Button submitButton;

        public ListingViewHolder(View itemView) {
            super(itemView);
            dormName = itemView.findViewById(R.id.textView_dormName);
            dormCapacity = itemView.findViewById(R.id.textView_dormCapacity);
            dormPrice = itemView.findViewById(R.id.textView_dormPrice);
            adminStatusText = itemView.findViewById(R.id.textView_textAdminStatus);
            occupancyStatusText = itemView.findViewById(R.id.textOccupancyStatus);

            viewLocation = itemView.findViewById(R.id.textView_location);
            viewLandmark = itemView.findViewById(R.id.textView_landmark);
            viewInclusion = itemView.findViewById(R.id.textView_inclusions);
            viewDescription = itemView.findViewById(R.id.textView_description);

            editLandmark = itemView.findViewById(R.id.edit_landmark);
            editLocation = itemView.findViewById(R.id.edit_location);
            editInclusion = itemView.findViewById(R.id.edit_inclusions);
            editDescription = itemView.findViewById(R.id.edit_description);
            editOccupancy = itemView.findViewById(R.id.edit_occupancy); // newly added

            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            editButton = itemView.findViewById(R.id.button_edit_listing);
            submitButton = itemView.findViewById(R.id.button_submit);
        }

        public void bind(Listing listing, boolean isExpanded) {
            dormName.setText(listing.getDormName());
            dormCapacity.setText("Capacity: " + listing.getCapacity());
            dormPrice.setText("₱" + listing.getPrice() + "/month");

            String adminStatus = listing.getStatus();
            adminStatusText.setText("Status: " + adminStatus);
            adminStatusText.setVisibility(("admin".equals(userRole) || "landlord".equals(userRole)) ? View.VISIBLE : View.GONE);

            switch (adminStatus != null ? adminStatus.toLowerCase() : "") {
                case "approved": adminStatusText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green)); break;
                case "pending": adminStatusText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.star_yellow)); break;
                case "rejected": adminStatusText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red)); break;
                default: adminStatusText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white)); break;
            }

            String occupancyStatus = listing.getOccupancyStatus();
            occupancyStatusText.setText("Occupancy: " + occupancyStatus);
            switch (occupancyStatus != null ? occupancyStatus.toLowerCase() : "") {
                case "occupied": occupancyStatusText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red)); break;
                case "unoccupied": occupancyStatusText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green)); break;
                default: occupancyStatusText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white)); break;
            }

            expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            // Static Display
            viewLocation.setText("Location: " + listing.getLocation());
            viewLandmark.setText("Landmark: " + listing.getLandmark());
            viewInclusion.setText("Inclusions: " + listing.getInclusions());
            viewDescription.setText("Description: " + listing.getDescription());

            viewLocation.setVisibility(View.VISIBLE);
            viewLandmark.setVisibility(View.VISIBLE);
            viewInclusion.setVisibility(View.VISIBLE);
            viewDescription.setVisibility(View.VISIBLE);

            editLocation.setText(listing.getLocation());
            editInclusion.setText(listing.getInclusions());
            editDescription.setText(listing.getDescription());
            editLandmark.setText(listing.getLandmark());
            editOccupancy.setText(listing.getOccupancyStatus());

            editLocation.setVisibility(View.GONE);
            editInclusion.setVisibility(View.GONE);
            editDescription.setVisibility(View.GONE);
            editLandmark.setVisibility(View.GONE);
            editOccupancy.setVisibility(View.GONE);
            submitButton.setVisibility(View.GONE);

            editButton.setOnClickListener(v -> {
                viewLocation.setVisibility(View.GONE);
                viewLandmark.setVisibility(View.GONE);
                viewInclusion.setVisibility(View.GONE);
                viewDescription.setVisibility(View.GONE);

                editLocation.setVisibility(View.VISIBLE);
                editInclusion.setVisibility(View.VISIBLE);
                editDescription.setVisibility(View.VISIBLE);
                editLandmark.setVisibility(View.VISIBLE);
                editOccupancy.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.VISIBLE);
            });

            submitButton.setOnClickListener(v -> {
                String newLocation = editLocation.getText().toString().trim();
                String newInclusion = editInclusion.getText().toString().trim();
                String newDescription = editDescription.getText().toString().trim();
                String newLandmark = editLandmark.getText().toString().trim();
                String newOccupancy = editOccupancy.getText().toString().trim();

                if (TextUtils.isEmpty(newLocation) || TextUtils.isEmpty(newInclusion) || TextUtils.isEmpty(newDescription) || TextUtils.isEmpty(newOccupancy)) {
                    Toast.makeText(itemView.getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                submitButton.setEnabled(false);

                FirebaseFirestore.getInstance()
                        .collection("listings")
                        .document(listing.getId())
                        .update(
                                "location", newLocation,
                                "inclusions", newInclusion,
                                "description", newDescription,
                                "landmark", newLandmark,
                                "occupancyStatus", newOccupancy
                        )
                        .addOnSuccessListener(aVoid -> {
                            listing.setLocation(newLocation);
                            listing.setInclusions(newInclusion);
                            listing.setDescription(newDescription);
                            listing.setLandmark(newLandmark);
                            listing.setOccupancyStatus(newOccupancy);

                            Toast.makeText(itemView.getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();

                            AlphaAnimation fade = new AlphaAnimation(0.3f, 1.0f);
                            fade.setDuration(300);
                            itemView.startAnimation(fade);

                            expandedPosition = -1;
                            notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> Toast.makeText(itemView.getContext(), "Failed to update", Toast.LENGTH_SHORT).show())
                        .addOnCompleteListener(task -> submitButton.setEnabled(true));
            });
        }
    }
}
