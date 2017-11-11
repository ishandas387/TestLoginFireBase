package com.ishan387.testlogin.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ishan387.testlogin.R;

/**
 * Created by ishan on 11-11-2017.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder {
    public TextView title, price, category;
    public ProductViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        category = (TextView) view.findViewById(R.id.category);
        price = (TextView) view.findViewById(R.id.price);

    }
}
