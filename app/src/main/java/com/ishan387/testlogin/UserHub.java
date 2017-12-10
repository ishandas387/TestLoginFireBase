package com.ishan387.testlogin;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ishan387.testlogin.model.OrderHistoryAdapter;
import com.ishan387.testlogin.model.OrderHistoryViewHolder;
import com.ishan387.testlogin.model.Orders;
import com.ishan387.testlogin.model.Product;
import com.ishan387.testlogin.model.ProductViewHolder;

public class UserHub extends AppCompatActivity {

    TextView username,useremail;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    DatabaseReference orders;
    FirebaseUser user;
    FirebaseRecyclerAdapter<Orders, OrderHistoryViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userhub);
        mAuth = FirebaseAuth.getInstance();
        username = (TextView) findViewById(R.id.username);
        recyclerView = (RecyclerView) findViewById(R.id.order_history_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Query filter = null;
        if (mAuth != null) {
            user = mAuth.getCurrentUser();
            username.setText(user.getDisplayName());
        }
        orders = FirebaseDatabase.getInstance().getReference("Orders");
        filter = orders.orderByChild("userName").equalTo(user.getDisplayName());
            loadRecylerView(filter);

    }

    private void loadRecylerView(Query filter) {
        adapter = new FirebaseRecyclerAdapter<Orders, OrderHistoryViewHolder>(Orders.class,R.layout.orderhistorysinglerow,OrderHistoryViewHolder.class,filter) {
            @Override
            protected void populateViewHolder(final OrderHistoryViewHolder viewHolder, Orders model, int position) {

                viewHolder.orderTime.setText(model.getServiceTime());
                viewHolder.orderId.setText(model.getOrderId());
                viewHolder.orderPrice.setText(model.getTotal());
                String statusSet="Placed";

                if (model.getStatus() == 0)
                {
                    statusSet="Placed";
                }
                else if(model.getStatus() == 1)
                {
                    statusSet="Confirmed";
                }
                else if (model.getStatus() == 2)
                {
                    statusSet="Rejected";
                }
                viewHolder.status.setText(statusSet);
            }
        };
        recyclerView.setAdapter(adapter);
    }

}
