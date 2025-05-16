package com.example.dormspot.MainActivitySpottr;

import android.content.Context;
import android.graphics.Color;
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

import java.util.List;

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

        holder.dormTitle.setText(item.getDormName() != null ? item.getDormName() : "No Name");
        holder.capacity.setText("Capacity: " + item.getCapacity());
        holder.price.setText("₱" + item.getPrice() + "/month");
        holder.status.setText("Status: " + item.getStatus());

        String status = item.getStatus() != null ? item.getStatus().trim().toLowerCase() : "";
        switch (status) {
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

        holder.viewDormBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDormClick(item.getId());
            }
        });

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
        TextView dormTitle, capacity, price, status, location, description;
        ImageView imageBackground;
        Button viewDormBtn;
        ImageButton bookmarkBtn;

        DormViewHolder(View itemView) {
            super(itemView);
            dormTitle = itemView.findViewById(R.id.dormTitle);
            capacity = itemView.findViewById(R.id.capacity);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
            location = itemView.findViewById(R.id.location);
            description = itemView.findViewById(R.id.description);
            imageBackground = itemView.findViewById(R.id.imageBackground);
            viewDormBtn = itemView.findViewById(R.id.viewDormBtn);
            bookmarkBtn = itemView.findViewById(R.id.bookmarkBtn);
        }
    }
}