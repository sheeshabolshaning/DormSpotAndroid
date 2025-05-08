package com.example.dormspot.MainActivitySpottr;

import android.content.Context;
import android.content.Intent;
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
import com.example.dormspot.R;

import java.util.List;

public class DormAdapter extends RecyclerView.Adapter<DormAdapter.DormViewHolder> {

    private List<DormItem> dormList;
    private Context context;

    public DormAdapter(Context context, List<DormItem> dormList) {
        this.context = context;
        this.dormList = dormList;
    }

    @NonNull
    @Override
    public DormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dorm_listing, parent, false);
        return new DormViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DormViewHolder holder, int position) {
        DormItem item = dormList.get(position); // ✅ Moved to the top

        holder.dormTitle.setText(item.getDormName());
        holder.capacity.setText("Capacity: " + item.getCapacity());
        holder.price.setText("₱" + item.getPrice() + "/month");
        holder.status.setText("Status: " + item.getStatus());

        // ✅ Set dynamic text color for status
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

        // ✅ Load image using Glide
        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.imageholder)
                .into(holder.imageBackground);

        // ✅ View Dorm button
        holder.viewDormBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, Booking.class);
            intent.putExtra("dormName", item.getDormName());
            intent.putExtra("capacity", item.getCapacity());
            intent.putExtra("price", item.getPrice());
            intent.putExtra("status", item.getStatus());
            intent.putExtra("imageUrl", item.getImageUrl());
            context.startActivity(intent);
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
