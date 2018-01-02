package com.ishan387.testlogin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.ishan387.testlogin.com.ishan387.db.CartDatabase;
import com.ishan387.testlogin.com.ishan387.db.UserDatabase;
import com.ishan387.testlogin.model.CartAdapter;
import com.ishan387.testlogin.model.CartItems;
import com.ishan387.testlogin.model.OrderItem;
import com.ishan387.testlogin.model.Orders;
import com.ishan387.testlogin.model.Users;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

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
    float t = 0.0f;
    public static TextView total;
    List<CartItems> itemsInCart = new ArrayList<>();
    CartAdapter adapter;
    Button placeOrder;
    List<OrderItem> productList = new ArrayList<>();
    private FirebaseAuth mAuth;
    String addr="";


    public TextView getTotal() {
        return total;
    }

    public void modifyCartTotal(float f)
    {
        t = t -f ;
        total.setText("₹" +Float.toString(t));

    }

    public void setTotal(TextView total) {
        this.total = total;
    }


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
        loadListAndAdapter();
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // placeorderMethod(editText.getText().toString());
                if(selectDate.getText().toString().equals("DATE"))
                {
                    Toast.makeText(Cart.this,"Please select a service date",Toast.LENGTH_LONG).show();
                }
                else if(selectTime.getText().toString().equals("TIME"))
                {
                    Toast.makeText(Cart.this,"Please select a service time",Toast.LENGTH_LONG).show();
                } else
                {

                    showAlertDialog();
                }
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
                Calendar now7 =Calendar.getInstance();
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
                timePickerDialog.setMinTime(8,0,0);
                timePickerDialog.setMaxTime(18,0,0);
               // timePickerDialog.setTimeInterval(7);
              /*  timePickerDialog.setMinTime( now.get(Calendar.HOUR_OF_DAY)+1, now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
                timePickerDialog.setMaxTime( now.get(Calendar.HOUR_OF_DAY)+7, now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
       */     }
        });
    }

    private void loadListAndAdapter() {
        itemsInCart = new CartDatabase(this).getCart();
        adapter = new CartAdapter(itemsInCart,this);
        recyclerView.setAdapter(adapter);


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
        total.setText("₹" +Float.toString(t));
    }

    private void placeorderMethod(String userPhoneNumber) {

        productList.clear();
        itemsInCart.clear();
        itemsInCart = new CartDatabase(this).getCart();
        for(CartItems item : itemsInCart)
        {
            OrderItem p = new OrderItem();
            p.setPrice(Float.parseFloat(item.getPrice()));
            p.setName(item.getProductName());
            // p.setTime(item.getServiceTime());
            productList.add(p);

        }
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
                time = selectTime.getText().toString();
            }
            o.setServiceTime(date+" @ "+time);
            o.setUserPhoneNumber(userPhoneNumber);
            Float tot =0.0f;
            for(OrderItem pt : productList)
            {
                tot += pt.getPrice();
            }
            o.setTotal(tot.toString());

            o.setAddress(addr);
            o.setUserToken(FirebaseInstanceId.getInstance().getToken());
            orderrequest.child(o.getOrderId()).setValue(o);
            new CartDatabase(getBaseContext()).cleanCart();
            Toast.makeText(Cart.this,"Order placed",Toast.LENGTH_LONG).show();
            finish();

        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step");
        alertDialog.setMessage("Add your phone number");
        final EditText editText = new EditText(Cart.this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER

        );
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        Users u = new Users();
        List<Users> userDetails = new UserDatabase(this).getUser();

        if(userDetails!= null && !userDetails.isEmpty())
        {

            u = userDetails.get(0);
            if(null != u.getNu() && !u.getNu().isEmpty())
            {
                editText.setText(u.getNu());
            }
            addr= u.getNm()+" "+u.getAddAt()+" "+u.getAddNear()+" "+u.getAddCity();
        }
        else
        {
            //toast
            Toast.makeText(Cart.this,"Please add your details",Toast.LENGTH_LONG).show();
            //send user to user hub
            Intent i = new Intent(getApplicationContext(),UserHub.class);
            startActivity(i);
        }
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if(null != editText.getText() && !editText.getText().toString().isEmpty())
               {
                  placeorderMethod(editText.getText().toString());
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

        alertDialog.show();
    }

    private void loadList() {


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String m="";
        switch(monthOfYear)
        {
            case 0:
                m="Jan";
                break;
            case 1:
                m="Feb";
                break;
            case 2:
                m="Mar";
                break;
            case 3:
                m="Apr";
                break;
            case 4:
                m="May";
                break;
            case 5:
                m="Jun";
                break;
            case 6:
                m="Jul";
                break;
            case 7:
                m="Aug";
                break;
            case 8:
                m="Sep";
                break;
            case 9:
                m="Oct";
                break;
            case 10:
                m="Nov";
                break;
            case 11:
                m="Dec";
                break;
        }

        selectDate.setText(m+"-"+dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        selectTime.setText(hourOfDay+":"+minute);

    }
}
