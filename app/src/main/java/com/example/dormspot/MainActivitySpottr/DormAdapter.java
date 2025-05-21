package com.example.dormspot.MainActivitySpottr;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormspot.MainActivitySpottee.Listing;
import com.example.dormspot.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DormAdapter extends RecyclerView.Adapter<DormAdapter.DormViewHolder> {

    public interface OnDormClickListener {
        void onDormClick(String listingId);
    }

    private final List<Listing> dormList;
    private final Context context;
    private final OnDormClickListener listener;

    public DormAdapter(Context context, List<Listing> dormList, OnDormClickListener listener) {
        this.context = context;
        this.dormList = dormList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dorm_listing, parent, false);
        return new DormViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DormViewHolder holder, int position) {
        Listing item = dormList.get(position);

        // ✅ Set dorm title and capacity
        holder.dormTitle.setText(item.getDormName() != null ? item.getDormName() : "No Name");
        holder.capacity.setText("Capacity: " + item.getCapacity());

        // ✅ Handle price formatting
        Double price = item.getPrice();
        if (price == null) price = 0.0;

        // Log for debug (optional)
        Log.d("ADAPTER_PRICE", "price = " + price);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        String formattedPrice = currencyFormat.format(price) + "/month";
        holder.price.setText(formattedPrice);

        // ✅ Display occupancy status
        String occupancyStatus = item.getStatus() != null ? item.getStatus() : "N/A";
        holder.status.setText("Status: " + occupancyStatus);

        // ✅ Apply color coding
        switch (occupancyStatus.trim().toLowerCase()) {
            case "occupied":
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.occupied_red));
                break;
            case "pending":
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.pending_yellow));
                break;
            case "unoccupied":
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.unoccupied_green));
                break;
            default:
                holder.status.setTextColor(Color.WHITE);
                break;
        }

        // ✅ View Dorm Button
        holder.viewDormBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDormClick(item.getId());
            }
        });

        // ✅ Bookmark toggle
        holder.bookmarkBtn.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
            ((ImageButton) v).setImageResource(
                    v.isSelected() ? R.drawable.bookmark_filled : R.drawable.bookmark
            );
        });
    }


    @Override
    public int getItemCount() {
        return dormList.size();
    }

    static class DormViewHolder extends RecyclerView.ViewHolder {
        TextView dormTitle, capacity, price, status;
        Button viewDormBtn;
        ImageButton bookmarkBtn;

        DormViewHolder(View itemView) {
            super(itemView);
            dormTitle = itemView.findViewById(R.id.dormTitle);
            capacity = itemView.findViewById(R.id.capacity);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
            viewDormBtn = itemView.findViewById(R.id.viewDormBtn);
            bookmarkBtn = itemView.findViewById(R.id.bookmarkBtn);
        }
    }
}
