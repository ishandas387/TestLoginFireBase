package com.ishan387.testlogin;


import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ishan387.testlogin.com.ishan387.db.CartDatabase;
import com.ishan387.testlogin.model.CartItems;
import com.ishan387.testlogin.model.Product;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class ItemDetail extends AppCompatActivity  {


    TextView name;
    ImageView bgi;
    CollapsingToolbarLayout clayout;
    FloatingActionButton fab;
    String productId;
    FirebaseDatabase products;
    DatabaseReference prod;

    Product p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        products = FirebaseDatabase.getInstance();
        prod = products.getReference("Products");
        fab = (FloatingActionButton) findViewById(R.id.cart);
        name = (TextView) findViewById(R.id.name);
        bgi = (ImageView) findViewById(R.id.backgroundimage);


        clayout = (CollapsingToolbarLayout) findViewById(R.id.clayout);

        clayout.setExpandedTitleTextAppearance(R.style.expandclayout);
        clayout.setCollapsedTitleTextAppearance(R.style.collapsedclayout);

        if (getIntent() != null) {
            productId = getIntent().getStringExtra("productId");

        }
        if (!productId.isEmpty()) {
            getProductDetails();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceDate = "";
                String serviceTime = "";

                CartItems cartItem = new CartItems(productId, p.getName(), Float.toString(p.getPrice()), serviceDate + "/" + serviceTime);
                new CartDatabase(getBaseContext()).addToCart(cartItem);
                Toast.makeText(ItemDetail.this, "Added to cart",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }




    public void getProductDetails() {
        prod.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 p  = dataSnapshot.getValue(Product.class);
                if(null!= p.getImageUrl() && !p.getImageUrl().isEmpty())
                Picasso.with(getBaseContext()).load(Uri.parse(p.getImageUrl())).into(bgi);
                name.setText(p.getCategory());
                clayout.setTitle(p.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




}
