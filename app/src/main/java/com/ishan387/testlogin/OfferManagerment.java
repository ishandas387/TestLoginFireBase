package com.ishan387.testlogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ishan387.testlogin.com.ishan387.db.CartDatabase;
import com.ishan387.testlogin.model.CartItems;
import com.ishan387.testlogin.model.OfferZoneViewHolder;
import com.ishan387.testlogin.model.Offers;
import com.squareup.picasso.Picasso;

public class OfferManagerment extends AppCompatActivity {

    private RecyclerView recyclerView;
    DatabaseReference offers;
    FirebaseUser user;
    boolean isAdmin = false;
    FirebaseRecyclerAdapter<Offers, OfferZoneViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_managerment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.offer_recylerView);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        offers = FirebaseDatabase.getInstance().getReference("Offers");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.offerznadd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddOffer.class);
                startActivity(i);

            }
        });

        //Add to Activity
        FirebaseMessaging.getInstance().subscribeToTopic("offers");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
         isAdmin = pref.getBoolean("isAdmin",false);
        if(!isAdmin)
        {
            fab.setVisibility(View.GONE);
        }
        loadRecylerView();

    }

    private void loadRecylerView() {
        adapter = new FirebaseRecyclerAdapter<Offers, OfferZoneViewHolder>(Offers.class,R.layout.offersinglerow,OfferZoneViewHolder.class,offers) {
            @Override
            protected void populateViewHolder(final OfferZoneViewHolder viewHolder, final Offers model, final int position) {

                viewHolder.offerzndesc.setText(model.getOfferDescription());
                viewHolder.offerznid.setText(model.getOfferId());
                viewHolder.offerznname.setText(model.getOfferName());
                viewHolder.offerznprice.setText("₹"+model.getOfferPrice());
                if(model.getUrl()!= null && !model.getUrl().isEmpty())
                {
                    Uri uri = Uri.parse(model.getUrl());
                    if(null!= uri) {
                        Glide.with(getBaseContext()).load(uri).diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.offrimg);
                        // draweeView.setImageURI(Uri.parse(model.getImageUrl()));
                    }
                }
                if(!isAdmin)
                {
                    viewHolder.delete.setVisibility(View.GONE);
                    LinearLayout.LayoutParams layoutParams;
                    viewHolder.addtocart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                }

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            offers.child(model.getOfferId()).removeValue();
                            notifyItemRemoved(position);
                    }
                });
                viewHolder.addtocart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartItems cartItem = new CartItems(model.getOfferId(), model.getOfferName(),model.getOfferPrice(), "");
                        new CartDatabase(getBaseContext()).addToCart(cartItem);
                        Toast.makeText(OfferManagerment.this, "Added to cart",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
    }

}
