package com.ishan387.testlogin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ishan387.testlogin.model.Product;

public class AdminActivity extends AppCompatActivity {

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText itemName;
    Spinner category;
    private EditText itemDescription;
    private EditText itemPrice;
    Button save;

    DatabaseReference products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_activuty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        itemName = (EditText) findViewById(R.id.field_service);
        itemDescription = (EditText) findViewById(R.id.editText);
        category = (Spinner) findViewById(R.id.categoryspin);
        itemPrice = (EditText) findViewById(R.id.price);
        save = (Button) findViewById(R.id.saveitem);
        products = FirebaseDatabase.getInstance().getReference("Products");


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToDb();
                Snackbar.make(view, "Product Added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });


    }

    private void addItemToDb() {

        Product product = new Product();
        String id = products.push().getKey();
        product.setCategory(category.getSelectedItem().toString());
        product.setPrice(Float.parseFloat(itemPrice.getText().toString()));
        product.setName(itemName.getText().toString());
        product.setDescription(itemDescription.getText().toString());

        products.child(id).setValue(product);



    }

}
