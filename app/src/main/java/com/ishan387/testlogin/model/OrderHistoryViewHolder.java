package com.ishan387.testlogin.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ishan387.testlogin.R;
import com.ishan387.testlogin.onClickInterface;

/**
 * Created by ishan on 07-12-2017.
 */

public class OrderHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView orderId, orderPrice, orderTime,status;

    public void setItemClickListener(onClickInterface itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private onClickInterface itemClickListener;
    public OrderHistoryViewHolder(View itemView) {
        super(itemView);
        orderId = (TextView) itemView.findViewById(R.id.orderid);
        orderPrice = (TextView) itemView.findViewById(R.id.ordertotalprice);
        orderTime = (TextView) itemView.findViewById(R.id.servicetime);
        status = (TextView) itemView.findViewById(R.id.orderstatus);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
