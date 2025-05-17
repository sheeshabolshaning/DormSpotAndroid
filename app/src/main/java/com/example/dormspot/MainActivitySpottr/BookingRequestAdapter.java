package com.example.dormspot.MainActivitySpottr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dormspot.R;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingRequestAdapter extends RecyclerView.Adapter<BookingRequestAdapter.BookingViewHolder> {

    private List<BookingModel> bookingList;
    private Context context;
    private FirebaseFirestore db;

    public BookingRequestAdapter(List<BookingModel> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_booking_request, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingModel booking = bookingList.get(position);

        holder.userName.setText("Spottee: " + booking.getUserName());
        holder.dormName.setText("Dorm: " + booking.getDormName());
        holder.dateRange.setText("Dates: " + booking.getBookingDates());
        holder.totalPrice.setText("Total: " + booking.getTotalPrice());

        holder.confirmBtn.setOnClickListener(v -> {
            db.collection("booking_requests").document(booking.getBookingId())
                    .update("status", "confirmed")
                    .addOnSuccessListener(aVoid -> {
                        // 🔥 Create notification
                        Map<String, Object> notif = new HashMap<>();
                        notif.put("spotteeId", booking.getUserId()); // spottee UID
                        notif.put("title", "Booking Confirmed");
                        notif.put("message", "Your booking at " + booking.getDormName() + " has been confirmed!");
                        notif.put("timestamp", FieldValue.serverTimestamp());

                        db.collection("notifications")
                                .add(notif)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(context, "Booking confirmed and notification sent!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Confirmed, but failed to save notification.", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to confirm booking.", Toast.LENGTH_SHORT).show();
                    });
        });


        holder.rejectBtn.setOnClickListener(v -> {
            db.collection("booking_requests").document(booking.getBookingId())
                    .update("status", "rejected")
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(context, "Booking rejected", Toast.LENGTH_SHORT).show()
                    );
        });
    }

    private void sendNotification(String spotteeId, String dormName) {
        Map<String, Object> notif = new HashMap<>();
        notif.put("spotteeId", spotteeId);
        notif.put("title", "Booking Confirmed");
        notif.put("message", "Your booking at " + dormName + " has been confirmed!");
        notif.put("timestamp", FieldValue.serverTimestamp());

        db.collection("notifications").add(notif);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView userName, dormName, dateRange, totalPrice;
        Button confirmBtn, rejectBtn;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.textUserName);
            dormName = itemView.findViewById(R.id.textDormName);
            dateRange = itemView.findViewById(R.id.textBookingDates);
            totalPrice = itemView.findViewById(R.id.textTotalPrice);
            confirmBtn = itemView.findViewById(R.id.confirmButton);
            rejectBtn = itemView.findViewById(R.id.rejectButton);
        }
    }
}
