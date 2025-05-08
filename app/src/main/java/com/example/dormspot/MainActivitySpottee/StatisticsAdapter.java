package com.example.dormspot.MainActivitySpottee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dormspot.R;

import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticViewHolder> {

    private List<Listing> statisticList;
    private Context context;

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
    }

    @Override
    public int getItemCount() {
        return statisticList.size();
    }

    public class StatisticViewHolder extends RecyclerView.ViewHolder {
        TextView dormName, views, saves, inquiries, rentedPeriod, occupancy, revenue, status;
        Button viewButton;
        ImageView editButton;

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
            viewButton = itemView.findViewById(R.id.button_view_listing);
            editButton = itemView.findViewById(R.id.button_edit_listing);
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

            // Optional: Set text color based on status
            if ("Occupied".equalsIgnoreCase(listing.getStatus())) {
                status.setTextColor(context.getResources().getColor(R.color.green));
            } else {
                status.setTextColor(context.getResources().getColor(R.color.red));
            }

            viewButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, ListingDetailsActivity.class);
                intent.putExtra("listingId", listing.getId());
                context.startActivity(intent);
            });

            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, ListingDetailsActivity.class);
                intent.putExtra("listingId", listing.getId());
                context.startActivity(intent);
            });
        }

        private String getSafeNumber(Integer number) {
            return number == null ? "0" : String.valueOf(number);
        }
    }
}
