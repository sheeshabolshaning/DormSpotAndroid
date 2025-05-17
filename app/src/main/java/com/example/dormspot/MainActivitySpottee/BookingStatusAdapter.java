package com.example.dormspot.MainActivitySpottee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dormspot.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BookingStatusAdapter extends RecyclerView.Adapter<BookingStatusAdapter.BookingViewHolder> {

    private final List<BookingSpottee> bookingList;
    private final Context context;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BookingStatusAdapter(List<BookingSpottee> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_booking_status, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingSpottee booking = bookingList.get(position);

        holder.userName.setText("You booked at: " + booking.getDormName());
        holder.dates.setText("Dates: " + booking.getBookingDates());
        holder.total.setText("Total: " + booking.getTotalPrice());
        holder.status.setText("Status: " + booking.getStatus());

        boolean isDecisionMade = booking.getStatus().equalsIgnoreCase("approved")
                || booking.getStatus().equalsIgnoreCase("declined");

        holder.approve.setEnabled(!isDecisionMade);
        holder.decline.setEnabled(!isDecisionMade);

        holder.approve.setOnClickListener(v -> {
            updateStatus(booking.getBookingId(), "approved");
            holder.status.setText("Status: approved");
            holder.approve.setEnabled(false);
            holder.decline.setEnabled(false);
        });

        holder.decline.setOnClickListener(v -> {
            updateStatus(booking.getBookingId(), "declined");
            holder.status.setText("Status: declined");
            holder.approve.setEnabled(false);
            holder.decline.setEnabled(false);
        });
    }

    private void updateStatus(String bookingId, String newStatus) {
        db.collection("booking_requests").document(bookingId)
                .update("status", newStatus)
                .addOnSuccessListener(unused ->
                        Toast.makeText(context, "Booking " + newStatus, Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView userName, dates, total, status;
        Button approve, decline;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.textView_userName);
            dates = itemView.findViewById(R.id.textView_bookingDates);
            total = itemView.findViewById(R.id.textView_totalPrice);
            status = itemView.findViewById(R.id.textView_status);
            approve = itemView.findViewById(R.id.button_approveBooking);
            decline = itemView.findViewById(R.id.button_declineBooking);
        }
    }
}
