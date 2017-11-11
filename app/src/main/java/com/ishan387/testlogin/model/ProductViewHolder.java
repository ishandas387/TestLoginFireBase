package com.ishan387.testlogin.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishan387.testlogin.R;
import com.ishan387.testlogin.onClickInterface;

/**
 * Created by ishan on 11-11-2017.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
    public TextView title, price, category;
    public ImageView bgi;

    public void setItemClickListener(onClickInterface itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private onClickInterface itemClickListener;
    public ProductViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        category = (TextView) view.findViewById(R.id.category);
        price = (TextView) view.findViewById(R.id.price);
        bgi =(ImageView) view.findViewById(R.id.backgroundimage);
        view.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
