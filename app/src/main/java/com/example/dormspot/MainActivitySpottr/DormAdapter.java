package com.example.dormspot.MainActivitySpottr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormspot.R;

import java.util.List;

public class DormAdapter extends RecyclerView.Adapter<DormAdapter.DormViewHolder> {

    public interface OnDormClickListener {
        void onDormClick(String listingId);
    }

    private List<DormItem> dormList;
    private Context context;
    private OnDormClickListener listener;

    public DormAdapter(Context context, List<DormItem> dormList, OnDormClickListener listener) {
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
        DormItem item = dormList.get(position);

        holder.dormTitle.setText(item.getDormName());
        holder.capacity.setText("Capacity: " + item.getCapacity());
        holder.price.setText("₱" + item.getPrice() + "/month");
        holder.status.setText("Status: " + item.getStatus());

        // ✅ Dynamic status color
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

        // ✅ Load image with Glide
        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.imageholder)
                .into(holder.imageBackground);

        // ✅ View Dorm button click
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
        ImageView imageBackground;
        Button viewDormBtn;
        ImageButton bookmarkBtn;

        DormViewHolder(View itemView) {
            super(itemView);
            dormTitle = itemView.findViewById(R.id.dormTitle);
            capacity = itemView.findViewById(R.id.capacity);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
            imageBackground = itemView.findViewById(R.id.imageBackground);
            viewDormBtn = itemView.findViewById(R.id.viewDormBtn);
            bookmarkBtn = itemView.findViewById(R.id.bookmarkBtn);
        }
    }
}
