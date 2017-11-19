package com.ishan387.testlogin.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishan387.testlogin.Cart;
import com.ishan387.testlogin.R;
import com.ishan387.testlogin.com.ishan387.db.CartDatabase;

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
    public void onBindViewHolder(CartViewHolder holder, final int position) {

        holder.price.setText(lisData.get(position).getPrice());
        holder.item.setText(lisData.get(position).getProductName());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CartItems theRemovedItem = lisData.get(position);
                // remove your item from data base

                new CartDatabase(v.getContext()).deleteItem(theRemovedItem.getProductName());
                lisData.remove(position);  // remove the item from list
                notifyItemRemoved(position); // notify the adapter about the removed item
            }
        });

    }

    @Override
    public int getItemCount() {
        return lisData.size();
    }
}
