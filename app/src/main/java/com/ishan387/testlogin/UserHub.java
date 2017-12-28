package com.ishan387.testlogin;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ishan387.testlogin.com.ishan387.db.UserDatabase;
import com.ishan387.testlogin.model.MyDailogueFragment;
import com.ishan387.testlogin.model.OrderHistoryViewHolder;
import com.ishan387.testlogin.model.OrderItem;
import com.ishan387.testlogin.model.Orders;
import com.ishan387.testlogin.model.Product;
import com.ishan387.testlogin.model.Users;

import java.util.ArrayList;
import java.util.List;

public class UserHub extends AppCompatActivity {

    TextView username,useremail,userhuaddress;
    FloatingActionButton edit ;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    DatabaseReference orders;
    FirebaseUser user;
    CollapsingToolbarLayout clayout;
    FirebaseRecyclerAdapter<Orders, OrderHistoryViewHolder> adapter;
    MyDailogueFragment fragment = new MyDailogueFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userhub);
        mAuth = FirebaseAuth.getInstance();
        username = (TextView) findViewById(R.id.username);
        userhuaddress = (TextView) findViewById(R.id.userhuaddress);
        recyclerView = (RecyclerView) findViewById(R.id.order_history_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        Query filter = null;
        if (mAuth != null) {
            user = mAuth.getCurrentUser();
            username.setText(user.getDisplayName());
        }
        orders = FirebaseDatabase.getInstance().getReference("Orders");
        filter = orders.orderByChild("userName").equalTo(user.getDisplayName());
        edit = (FloatingActionButton) findViewById(R.id.editupdate);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserHub.this,UserAdd.class);
                startActivity(i);
            }
        });
        Users u = new Users();
        List<Users> userDetails = new UserDatabase(this).getUser();
        if(userDetails!= null && !userDetails.isEmpty())
         u = userDetails.get(0);
        username.setText(u.getNm());
        userhuaddress.setText(u.getNu()+"\n"+u.getAddAt()+"\n"+u.getAddNear()+"\n"+u.getAddCity());
            loadRecylerView(filter);
        clayout = (CollapsingToolbarLayout) findViewById(R.id.clayout);

        clayout.setExpandedTitleTextAppearance(R.style.expandclayout);
        clayout.setCollapsedTitleTextAppearance(R.style.collapsedclayout);

        clayout.setTitle("ORDER HISTORY");

    }

    private void loadRecylerView(Query filter) {
        adapter = new FirebaseRecyclerAdapter<Orders, OrderHistoryViewHolder>(Orders.class,R.layout.orderhistorysinglerow,OrderHistoryViewHolder.class,filter) {
            @Override
            protected void populateViewHolder(final OrderHistoryViewHolder viewHolder, final Orders model, int position) {

                viewHolder.orderTime.setText(model.getServiceTime());
                viewHolder.orderId.setText(model.getOrderId());
                viewHolder.orderPrice.setText("₹ "+model.getTotal());
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

                viewHolder.setItemClickListener(new onClickInterface() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        List<OrderItem> pl = model.getProducts();
                        if(pl !=null)
                        {
                            List<String> itemNames = new ArrayList<>();
                            for (OrderItem oi : pl)
                            {
                                itemNames.add(oi.getName());
                            }
                            fragment.setListItems(itemNames);
                            fragment.setAddress(model.getAddress());
                           // fragment.setAddrViewToGone();
                        }

                        fragment.show(getFragmentManager(),"");

                    }
                });
            }


        };
        recyclerView.setAdapter(adapter);
    }

}
