package com.ishan387.testlogin;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ishan387.testlogin.com.ishan387.db.CartDatabase;
import com.ishan387.testlogin.model.CartAdapter;
import com.ishan387.testlogin.model.CartItems;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference orderrequest;
    TextView total;
    List<CartItems> itemsInCart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        orderrequest = database.getReference("Orders");

        recyclerView =(RecyclerView) findViewById(R.id.cartlist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        total = (TextView) findViewById(R.id.total);
        loadList();
    }

    private void loadList() {

        itemsInCart = new CartDatabase(this).getCart();
        adapter = new CartAdapter(itemsInCart,this);
        recyclerView.setAdapter(adapter);

        float t = 0.0f;
        for(CartItems item : itemsInCart)
        {
            t += Float.parseFloat(item.getPrice());
        }
        Locale local = new Locale("en","IN");
        NumberFormat n = NumberFormat.getCurrencyInstance(local);
        total.setText(Float.toString(t));
    }
}
