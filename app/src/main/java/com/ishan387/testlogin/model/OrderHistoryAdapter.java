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
 * Created by ishan on 07-12-2017.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryViewHolder> {

    public OrderHistoryAdapter(List<Orders> lisData, Context context) {
        this.lisData = lisData;
        this.context = context;
    }

    private List<Orders> lisData = new ArrayList<>();
    private Context context;
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.orderhistorysinglerow,parent,false);
        return new OrderHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderHistoryViewHolder holder, int position) {
        holder.orderPrice.setText(lisData.get(position).getTotal());
        holder.orderId.setText(lisData.get(position).getTimeStamp());
        holder.orderTime.setText(lisData.get(position).getServiceTime());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
