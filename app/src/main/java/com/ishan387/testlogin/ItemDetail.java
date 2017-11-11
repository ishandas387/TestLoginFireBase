package com.ishan387.testlogin;

import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ishan387.testlogin.model.Product;
import com.squareup.picasso.Picasso;

public class ItemDetail extends AppCompatActivity {


    TextView name;
    ImageView bgi;
    CollapsingToolbarLayout clayout;
    FloatingActionButton fab;
    String productId;
    FirebaseDatabase products;
    DatabaseReference prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        products = FirebaseDatabase.getInstance();
        prod = products.getReference("Products");
        fab = (FloatingActionButton) findViewById(R.id.cart);
        name =(TextView) findViewById(R.id.name);
        bgi =(ImageView) findViewById(R.id.backgroundimage);

        clayout = (CollapsingToolbarLayout) findViewById(R.id.clayout);

        clayout.setExpandedTitleTextAppearance(R.style.expandclayout);
        clayout.setCollapsedTitleTextAppearance(R.style.collapsedclayout);

        if(getIntent() != null)
        {
            productId = getIntent().getStringExtra("productId");

        }
        if(!productId.isEmpty())
        {
            getProductDetails();
        }

    }

    public void getProductDetails() {
        prod.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product p  = dataSnapshot.getValue(Product.class);
                if(null!= p.getImageUrl() && !p.getImageUrl().isEmpty())
                Picasso.with(getBaseContext()).load(Uri.parse(p.getImageUrl())).into(bgi);
                name.setText(p.getName());
                clayout.setTitle(p.getCategory());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
