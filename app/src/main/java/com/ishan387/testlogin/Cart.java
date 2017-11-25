package com.ishan387.testlogin;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ishan387.testlogin.com.ishan387.db.CartDatabase;
import com.ishan387.testlogin.model.CartAdapter;
import com.ishan387.testlogin.model.CartItems;
import com.ishan387.testlogin.model.OrderItem;
import com.ishan387.testlogin.model.Orders;
import com.ishan387.testlogin.model.Product;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference orderrequest;
    Button selectDate, selectTime;

    public TextView getTotal() {
        return total;
    }

    public void setTotal(TextView total) {
        this.total = total;
    }

    public TextView total;
    List<CartItems> itemsInCart = new ArrayList<>();
    CartAdapter adapter;
    Button placeOrder;
    List<OrderItem> productList = new ArrayList<>();
    private FirebaseAuth mAuth;

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
        placeOrder = (Button) findViewById(R.id.placeorder);
        total = (TextView) findViewById(R.id.total);
        mAuth = FirebaseAuth.getInstance();
        selectDate =(Button) findViewById(R.id.selectdate);
        selectTime =(Button) findViewById(R.id.selecttime);
        loadList();



        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (productList.isEmpty())
                {
                    Toast.makeText(Cart.this,"Cart is empty",Toast.LENGTH_LONG).show();

                }
                else
                {

                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    Orders o = new  Orders();
                    // o.setUserPhoneNumber(editText.getText().toString());
                    o.setOrderId(String.valueOf(System.currentTimeMillis()));
                    if(currentUser!=null)
                    {
                        o.setUserName(currentUser.getDisplayName());
                    }
                    o.setProducts(productList);
                    String date = "";
                    String time="";

                    if(!selectDate.getText().toString().equals("DATE"))
                    {
                        date = selectDate.getText().toString();
                    }
                    if(!selectTime.getText().toString().equals("TIME"))
                    {
                        date = selectTime.getText().toString();
                    }
                    o.setServiceTime(date+"-"+time);
                    Float tot =0.0f;
                    for(OrderItem pt : productList)
                    {
                        tot += pt.getPrice();
                    }
                    o.setTotal(tot.toString());
                    orderrequest.child(o.getOrderId()).setValue(o);
                    new CartDatabase(getBaseContext()).cleanCart();
                    Toast.makeText(Cart.this,"Order placed",Toast.LENGTH_LONG).show();
                    finish();

                }

             //  showAlertDialog();
            }
        });


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(Cart.this,

                        now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.setTitle("SELECT SERVICE DATE");
                datePickerDialog.show(getFragmentManager(),"Date");
                datePickerDialog.setMinDate(now);
                Calendar now7 =now;
                now7.add(Calendar.DAY_OF_MONTH,7);
                datePickerDialog.setMaxDate(now7);

            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(Cart.this,

                        now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND),true
                );
                timePickerDialog.setTitle("SELECT SERVICE TIME");
                timePickerDialog.show(getFragmentManager(),"TIME");
                timePickerDialog.setTimeInterval(7);
                timePickerDialog.setMinTime( now.get(Calendar.HOUR_OF_DAY)+1, now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
                timePickerDialog.setMaxTime( now.get(Calendar.HOUR_OF_DAY)+7, now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
            }
        });
    }


    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step");
        alertDialog.setMessage("Add your phone number");
        final EditText editText = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if(null != editText.getText() && !editText.getText().toString().isEmpty())
               {
                   Orders o = new  Orders();
                   o.setUserPhoneNumber(editText.getText().toString());
                   o.setOrderId(String.valueOf(System.currentTimeMillis()));
                   o.setProducts(productList);
                   o.setStatus(0);
                   o.setServiceTime(productList.get(0).getTime());
                   Float tot =0.0f;
                   for(OrderItem pt : productList)
                   {
                       tot += pt.getPrice();
                   }
                   o.setTotal(tot.toString());
                   orderrequest.child(o.getTimeStamp()).setValue(o);
                   new CartDatabase(getBaseContext()).cleanCart();
                   Toast.makeText(Cart.this,"Order placed",Toast.LENGTH_LONG).show();
               }
               else
               {
                   showAlertDialog();
               }
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private void loadList() {

        itemsInCart = new CartDatabase(this).getCart();
        adapter = new CartAdapter(itemsInCart,this);
        recyclerView.setAdapter(adapter);

        float t = 0.0f;
        for(CartItems item : itemsInCart)
        {
            OrderItem p = new OrderItem();
            p.setPrice(Float.parseFloat(item.getPrice()));
            p.setName(item.getProductName());
           // p.setTime(item.getServiceTime());
            productList.add(p);
            t += Float.parseFloat(item.getPrice());
        }
        Locale local = new Locale("en","IN");
        NumberFormat n = NumberFormat.getCurrencyInstance(local);
        total.setText(Float.toString(t));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        selectDate.setText(monthOfYear+"-"+dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        selectTime.setText(hourOfDay+":"+minute);

    }
}
