package com.ishan387.testlogin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ishan387.testlogin.model.OrderAcceptRejectAdapter;
import com.ishan387.testlogin.model.OrderAcceptRejectViewHolder;
import com.ishan387.testlogin.model.OrderHistoryViewHolder;
import com.ishan387.testlogin.model.Orders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminAcceptReject extends AppCompatActivity {

    TextView username,useremail;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    DatabaseReference orders;
    FirebaseUser user;
    FirebaseRecyclerAdapter<Orders, OrderAcceptRejectViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_accept_reject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.acceptrejectorderview);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        orders = FirebaseDatabase.getInstance().getReference("Orders");
        Query q = orders.orderByKey();
        /*final List<Orders> list1 = new ArrayList<>();
        orders.orderByChild("orderId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //List<Orders> list2 = (List<Orders>) dataSnapshot.getValue();
                //list1.addAll(list2);

                Object value = dataSnapshot.getValue();
                if(value instanceof List) {
                    List<Object> values = (List<Object>) value;
                    // do your magic with values
                }
                else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Collections.reverse(list1);

        OrderAcceptRejectAdapter ad = new OrderAcceptRejectAdapter(list1,this);
        recyclerView.setAdapter(ad);
*/
        loadRecycler(q);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                orders = FirebaseDatabase.getInstance().getReference("Orders");
                Query q = orders.orderByChild("status").equalTo(0);
                loadRecycler(q);
                Snackbar.make(view, "showing only pending orders", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void loadRecycler(Query q) {



        adapter = new FirebaseRecyclerAdapter<Orders, OrderAcceptRejectViewHolder>(Orders.class,R.layout.acceptrejctsingleview,OrderAcceptRejectViewHolder.class,q) {

            @Override
            protected void populateViewHolder(final OrderAcceptRejectViewHolder viewHolder, final Orders model, int position) {

                viewHolder.orderTime.setText(model.getServiceTime());
                viewHolder.orderId.setText(model.getOrderId());
                viewHolder.orderPrice.setText(model.getTotal());
                viewHolder.customerName.setText(model.getUserName());
                viewHolder.customerPh.setText(model.getUserPhoneNumber());
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
                viewHolder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.setStatus(1);
                        orders.child(model.getOrderId()).setValue(model);
                        //toast
                    }
                });

                viewHolder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.setStatus(2);
                        orders.child(model.getOrderId()).setValue(model);
                        //toast
                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);

    }

}
