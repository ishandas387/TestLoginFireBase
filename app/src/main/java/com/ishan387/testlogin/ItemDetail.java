package com.ishan387.testlogin;


import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ishan387.testlogin.com.ishan387.db.CartDatabase;

import com.ishan387.testlogin.model.CartItems;
import com.ishan387.testlogin.model.Product;
import com.ishan387.testlogin.model.Review;
import com.ishan387.testlogin.model.ReviewAdapter;
import com.squareup.picasso.Picasso;

import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemDetail extends AppCompatActivity implements RatingDialogListener {


    TextView name,price;
    ImageView bgi;
    CollapsingToolbarLayout clayout;
    FloatingActionButton fab;
    String productId;
    FirebaseDatabase products;
    DatabaseReference prod;
    Product p;
    RatingBar ratingbar;
    FloatingActionButton ratingButton;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    Button adminEdit,adminDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        mAuth = FirebaseAuth.getInstance();

        products = FirebaseDatabase.getInstance();
        prod = products.getReference("Products");
        fab = (FloatingActionButton) findViewById(R.id.cart);
        name = (TextView) findViewById(R.id.name);
        price =(TextView) findViewById(R.id.itemdetail_price);
        bgi = (ImageView) findViewById(R.id.backgroundimage);
        ratingButton = (FloatingActionButton) findViewById(R.id.ratingbutton);
        ratingbar = (RatingBar) findViewById(R.id.ratngbar);
        adminDelete =(Button) findViewById(R.id.adminremoveitem);
        adminEdit =(Button) findViewById(R.id.adminedititem);
        user = mAuth.getCurrentUser();

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialogue();
            }
        });

        recyclerView =(RecyclerView) findViewById(R.id.reviewlist);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);


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
        adminDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });
        adminEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editItem(p);
            }
        });
    }

    private void editItem(Product p) {

        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putSerializable("ProductPassed", p);
        i.putExtras(b);
        i.setClass(this, AdminActivity.class);
        startActivity(i);

    }

    private void deleteItem() {
        prod.child(productId).removeValue();
        Toast.makeText(ItemDetail.this, "Item removed",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showRatingDialogue() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Ok","Good","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate the service")
                .setDescription("Select stars and submit feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here")
                .setWindowAnimation(R.style.RatingDialogueFadeAnimation)
                .create(ItemDetail.this).show();



    }


    public void getProductDetails() {
        prod.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 p  = dataSnapshot.getValue(Product.class);
                 if(null != p)
                 {
                     if(null!= p.getImageUrl() && !p.getImageUrl().isEmpty())
                         Picasso.with(getBaseContext()).load(Uri.parse(p.getImageUrl())).into(bgi);
                     name.setText(p.getName());
                     price.setText("₹"+p.getPrice());
                     clayout.setTitle(p.getCategory());
                     List<Review> currentReviewList = p.getReviewList();
                     if(null != currentReviewList && !currentReviewList.isEmpty())
                     {
                         int sum=0, count=0;
                         for(Review r : currentReviewList)
                         {
                             sum = sum + (int) r.getRating();
                             count++;
                         }
                         float average = sum/count;
                         ratingbar.setRating(average);
                         settingListOfReviews(currentReviewList);
                     }
                 }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void settingListOfReviews(List<Review> currentReviewList) {

        ReviewAdapter adapter = new ReviewAdapter(currentReviewList, this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onPositiveButtonClicked(int i, String s) {

        Review r = new Review();
        r.setReview(s);
        r.setDate(new Date());
        if(null != user)
        {
            if(!user.getDisplayName().isEmpty())
            {

            r.setUserName(user.getDisplayName());
            }
            else
            {
                if(!user.getEmail().isEmpty())
                r.setUserName(user.getEmail());
            }

            if(!user.getEmail().isEmpty())
            {

                r.setUserEmail(user.getEmail());
            }
            r.setRating(i);

        }
        if (null != p)
        {
            checkReviewExists(p,r);

        }
        else
        {
            getProductDetails();
            checkReviewExists(p,r);
        }
        Toast.makeText(ItemDetail.this, "Thank you for your feedback",
                Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onNegativeButtonClicked() {

    }

    private void checkReviewExists(Product p, Review r) {
        List<Review> reviewList = p.getReviewList();
        boolean flag  =false;
        for( Review review : reviewList)
        {
            if (review.getUserEmail().equalsIgnoreCase(r.getUserEmail()) || review.getUserName().equalsIgnoreCase(r.getUserName()))
            {
                review.setRating(r.getRating());
                review.setReview(r.getReview());
                review.setDate(new Date());
                //p.getReviewList().add(review);
                prod.child(productId).setValue(p);
                flag = true;
            }

        }
        if(!flag)
        {
            p.getReviewList().add(r);
            prod.child(productId).setValue(p);
        }
    }
}
