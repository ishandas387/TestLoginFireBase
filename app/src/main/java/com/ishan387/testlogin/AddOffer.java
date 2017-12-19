package com.ishan387.testlogin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ishan387.testlogin.model.Offers;

import java.util.Date;

public class AddOffer extends AppCompatActivity {

    EditText offerName, offrDescription,offerPrice;
    Button addOfferButton;
    DatabaseReference offers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addofferlayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        offerName = (EditText) findViewById(R.id.offer_name);
        offerPrice = (EditText) findViewById(R.id.offer_price);
        offrDescription = (EditText) findViewById(R.id.offer_description);
        addOfferButton =(Button) findViewById(R.id.add_offer);
        offers = FirebaseDatabase.getInstance().getReference("Offers");

        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Offers of = new Offers();
                of.setOfferId(String.valueOf(System.currentTimeMillis()));
                of.setOfferCreated(new Date());
                of.setOfferDescription(offrDescription.getText().toString());
                of.setOfferName(offerName.getText().toString());
                of.setOfferPrice(offerPrice.getText().toString());
                offers.child(of.getOfferId()).setValue(of);

                Toast.makeText(AddOffer.this,"Offer Added",Toast.LENGTH_LONG).show();
                finish();
            }
        });


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
