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

public class ItemDetail extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    TextView name;
    ImageView bgi;
    CollapsingToolbarLayout clayout;
    FloatingActionButton fab;
    String productId;
    FirebaseDatabase products;
    DatabaseReference prod;
    Button selectDate, selectTime;
    Product p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        products = FirebaseDatabase.getInstance();
        prod = products.getReference("Products");
        fab = (FloatingActionButton) findViewById(R.id.cart);
        name =(TextView) findViewById(R.id.name);
        bgi =(ImageView) findViewById(R.id.backgroundimage);
        selectDate =(Button) findViewById(R.id.selectdate);
        selectTime =(Button) findViewById(R.id.selecttime);

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItems cartItem = new CartItems(productId,p.getName(),Float.toString(p.getPrice()),"9am");
                new CartDatabase(getBaseContext()).addToCart(cartItem);
                Toast.makeText(ItemDetail.this, "Added to cart",
                        Toast.LENGTH_SHORT).show();
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
               DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(ItemDetail.this,

                       now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
               );
               datePickerDialog.setTitle("SELECT SERVICE DATE");
               datePickerDialog.show(getFragmentManager(),"Date");
               datePickerDialog.setMinDate(now);
               Calendar now7 =now;
               now7.add(Calendar.DAY_OF_MONTH,7);
               datePickerDialog.setMinDate(now);
               datePickerDialog.setMaxDate(now7);

            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(ItemDetail.this,

                        now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND),true
                );
                timePickerDialog.setTitle("SELECT SERVICE TIME");
                timePickerDialog.show(getFragmentManager(),"TIME");
                timePickerDialog.setTimeInterval(7);
                timePickerDialog.setMinTime( now.get(Calendar.HOUR_OF_DAY)+1, now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
                timePickerDialog.setMinTime( now.get(Calendar.HOUR_OF_DAY)+7, now.get(Calendar.MINUTE), now.get(Calendar.SECOND));



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



    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        selectDate.setText(monthOfYear+"-"+dayOfMonth);

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }
}
