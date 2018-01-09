package com.ishan387.testlogin.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ishan387.testlogin.R;
import com.ishan387.testlogin.onClickInterface;

/**
 * Created by ishan on 01-01-2018.
 */

public class CategorySelectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView categoryName;


    public void setItemClickListener(onClickInterface itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private onClickInterface itemClickListener;
    public CategorySelectViewHolder(View itemView) {
        super(itemView);
        categoryName = (TextView) itemView.findViewById(R.id.categoryname);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
