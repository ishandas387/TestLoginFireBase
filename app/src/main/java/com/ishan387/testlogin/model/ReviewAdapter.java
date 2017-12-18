package com.ishan387.testlogin.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishan387.testlogin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 16-12-2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private List<Review> lisData = new ArrayList<>();
    private Context context;

    public ReviewAdapter(List<Review> lisData, Context context) {
        this.lisData = lisData;
        this.context = context;
    }


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.reviewsingleview,parent,false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

            holder.reviewRate.setText(Integer.toString((int)lisData.get(position).getRating()));
            if(null != lisData.get(position).getDate())
            holder.reviewDate.setText(lisData.get(position).getDate().toString());
            holder.reviewerEmail.setText(lisData.get(position).getUserEmail());
            holder.reviewDescription.setText(lisData.get(position).getReview());
            holder.reviewerName.setText(lisData.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
      return lisData.size();
    }
}
