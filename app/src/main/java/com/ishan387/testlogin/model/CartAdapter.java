package com.ishan387.testlogin.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishan387.testlogin.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>
{
    public CartAdapter(List<CartItems> lisData, Context context) {
        this.lisData = lisData;
        this.context = context;
    }

    private List<CartItems> lisData = new ArrayList<>();
    private Context context;

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cartitemrow,parent,false);
        return new CartViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {

        holder.price.setText(lisData.get(position).getPrice());
        holder.item.setText(lisData.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return lisData.size();
    }
}
