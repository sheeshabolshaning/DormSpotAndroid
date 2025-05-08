package com.example.dormspot.MainActivitySpottee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dormspot.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView textName, textReview, textRating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.image_profile);
            textName = itemView.findViewById(R.id.text_name);
            textReview = itemView.findViewById(R.id.text_review);
            textRating = itemView.findViewById(R.id.text_rating);
        }

        public void bind(Review review) {
            textName.setText(review.getName());
            textReview.setText(review.getComment());
            textRating.setText(getStarString(review.getStars())); // uses int stars

            Glide.with(context)
                    .load(review.getImageUrl() != null && !review.getImageUrl().isEmpty()
                            ? review.getImageUrl()
                            : R.drawable.user)
                    .placeholder(R.drawable.user)
                    .into(imageProfile);
        }

        private String getStarString(int rating) {
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                stars.append(i < rating ? "★" : "☆");
            }
            return stars.toString();
        }
    }
}
