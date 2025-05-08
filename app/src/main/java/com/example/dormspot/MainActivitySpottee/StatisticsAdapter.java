package com.example.dormspot.MainActivitySpottee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormspot.R;

import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticViewHolder> {

    private final Context context;
    private final List<Listing> statisticList;
    private int expandedPosition = -1;

    public StatisticsAdapter(Context context, List<Listing> statisticList) {
        this.context = context;
        this.statisticList = statisticList;
    }

    @Override
    public StatisticViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_statistic, parent, false);
        return new StatisticViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StatisticViewHolder holder, int position) {
        Listing listing = statisticList.get(position);
        holder.bind(listing);

        boolean isExpanded = (position == expandedPosition);
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            expandedPosition = isExpanded ? -1 : position;
            notifyDataSetChanged(); // Rebind to collapse/expand
        });
    }

    @Override
    public int getItemCount() {
        return statisticList.size();
    }

    public class StatisticViewHolder extends RecyclerView.ViewHolder {
        TextView dormName, views, saves, inquiries, rentedPeriod, occupancy, revenue, status;
        ImageView editButton;
        View expandableLayout;

        public StatisticViewHolder(View itemView) {
            super(itemView);
            dormName = itemView.findViewById(R.id.stat_dorm_name);
            views = itemView.findViewById(R.id.stat_views);
            saves = itemView.findViewById(R.id.stat_saves);
            inquiries = itemView.findViewById(R.id.stat_inquiries);
            rentedPeriod = itemView.findViewById(R.id.stat_rented_period);
            occupancy = itemView.findViewById(R.id.stat_occupancy);
            revenue = itemView.findViewById(R.id.stat_revenue);
            status = itemView.findViewById(R.id.stat_status);
            editButton = itemView.findViewById(R.id.stat_edit);
            expandableLayout = itemView.findViewById(R.id.stat_expandable);
        }

        public void bind(Listing listing) {
            dormName.setText(nullSafe(listing.getDormName()));
            views.setText("👁 " + getSafeNumber(listing.getViews()));
            saves.setText("💾 " + getSafeNumber(listing.getSaves()));
            inquiries.setText("❗ " + getSafeNumber(listing.getInquiries()));
            rentedPeriod.setText("Rented: " + nullSafe(listing.getRentedPeriod()));
            occupancy.setText("Occupant: " + nullSafe(listing.getOccupancy()));
            revenue.setText("Revenue: ₱" + getSafeNumber(listing.getRevenue()));
            status.setText(nullSafe(listing.getStatus()));

            // Optional: status color
            String lowerStatus = listing.getStatus() != null ? listing.getStatus().toLowerCase() : "";
            switch (lowerStatus) {
                case "occupied":
                    status.setTextColor(ContextCompat.getColor(context, R.color.green));
                    break;
                case "unoccupied":
                    status.setTextColor(ContextCompat.getColor(context, R.color.red));
                    break;
                default:
                    status.setTextColor(ContextCompat.getColor(context, R.color.white));
                    break;
            }

            // Edit icon click opens ListingDetailsActivity
            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, ListingDetailsActivity.class);
                intent.putExtra("listingId", listing.getId());
                context.startActivity(intent);
            });
        }

        private String getSafeNumber(Number number) {
            return number == null ? "0" : String.valueOf(number);
        }

        private String nullSafe(String s) {
            return s == null ? "N/A" : s;
        }
    }
}
