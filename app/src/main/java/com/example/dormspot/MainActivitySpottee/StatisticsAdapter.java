package com.example.dormspot.MainActivitySpottee;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.dormspot.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticViewHolder> {

    private List<Listing> statisticList;

    public StatisticsAdapter(ListingMain context, List<Listing> statisticList) {
        this.statisticList = statisticList;
    }

    @NonNull
    @Override
    public StatisticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statistic, parent, false);
        return new StatisticViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticViewHolder holder, int position) {
        holder.bind(statisticList.get(position));
    }

    @Override
    public int getItemCount() {
        return statisticList.size();
    }

    public static class StatisticViewHolder extends RecyclerView.ViewHolder {

        TextView dormName, views, saves, inquiries, rentedPeriod, occupancy, revenue, status;
        EditText editRented, editOccupancy, editRevenue;
        LinearLayout expandable;
        Button submitButton;
        ImageView editButton;

        public StatisticViewHolder(@NonNull View itemView) {
            super(itemView);

            dormName = itemView.findViewById(R.id.stat_dorm_name);
            views = itemView.findViewById(R.id.stat_views);
            saves = itemView.findViewById(R.id.stat_saves);
            inquiries = itemView.findViewById(R.id.stat_inquiries);
            rentedPeriod = itemView.findViewById(R.id.stat_rented_period);
            occupancy = itemView.findViewById(R.id.stat_occupancy);
            revenue = itemView.findViewById(R.id.stat_revenue);
            status = itemView.findViewById(R.id.stat_status);

            expandable = itemView.findViewById(R.id.stat_expandable);
            submitButton = itemView.findViewById(R.id.button_submit_statistic);
            editButton = itemView.findViewById(R.id.button_edit_listing);

            editRented = itemView.findViewById(R.id.edit_stat_rented_period);
            editOccupancy = itemView.findViewById(R.id.edit_stat_occupancy);
            editRevenue = itemView.findViewById(R.id.edit_stat_revenue);

            submitButton.setVisibility(View.GONE);
            editRented.setVisibility(View.GONE);
            editOccupancy.setVisibility(View.GONE);
            editRevenue.setVisibility(View.GONE);
        }

        public void bind(Listing listing) {
            dormName.setText(listing.getDormName());
            views.setText("👁 " + getSafeNumber(listing.getViews()));
            saves.setText("💾 " + getSafeNumber(listing.getSaves()));
            inquiries.setText("❗ " + getSafeNumber(listing.getInquiries()));
            rentedPeriod.setText("Rented: " + listing.getRentedPeriod());
            occupancy.setText("Occupant: " + listing.getOccupancy());
            revenue.setText("Revenue: ₱" + listing.getRevenue());
            status.setText(listing.getStatus());

            if ("Occupied".equalsIgnoreCase(listing.getStatus())) {
                status.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.green));
            } else {
                status.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
            }

            editButton.setOnClickListener(v -> {
                editRented.setText(listing.getRentedPeriod());
                editOccupancy.setText(listing.getOccupancy());
                editRevenue.setText(String.valueOf(listing.getRevenue()));

                editRented.setVisibility(View.VISIBLE);
                editOccupancy.setVisibility(View.VISIBLE);
                editRevenue.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.VISIBLE);
            });

            submitButton.setOnClickListener(v -> {
                String newRented = editRented.getText().toString().trim();
                String newOccupancy = editOccupancy.getText().toString().trim();
                String newRevenueStr = editRevenue.getText().toString().trim();

                if (TextUtils.isEmpty(newRented) || TextUtils.isEmpty(newOccupancy) || TextUtils.isEmpty(newRevenueStr)) {
                    Toast.makeText(itemView.getContext(), "All fields required", Toast.LENGTH_SHORT).show();
                    return;
                }

                int newRevenue;
                try {
                    newRevenue = Integer.parseInt(newRevenueStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(itemView.getContext(), "Revenue must be a number", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore.getInstance().collection("listings")
                        .document(listing.getId())
                        .update(
                                "rentedPeriod", newRented,
                                "occupancy", newOccupancy,
                                "revenue", newRevenue
                        )
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(itemView.getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();

                            listing.setRentedPeriod(newRented);
                            listing.setOccupancy(newOccupancy);
                            listing.setRevenue(newRevenue);

                            rentedPeriod.setText("Rented: " + newRented);
                            occupancy.setText("Occupant: " + newOccupancy);
                            revenue.setText("Revenue: ₱" + newRevenue);

                            editRented.setVisibility(View.GONE);
                            editOccupancy.setVisibility(View.GONE);
                            editRevenue.setVisibility(View.GONE);
                            submitButton.setVisibility(View.GONE);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(itemView.getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                        });
            });
        }

        private String getSafeNumber(Integer number) {
            return number == null ? "0" : String.valueOf(number);
        }
    }
}
