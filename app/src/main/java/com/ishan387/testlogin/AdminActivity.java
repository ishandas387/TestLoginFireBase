package com.ishan387.testlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ishan387.testlogin.model.Product;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AdminActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =111 ;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText itemName;
    Spinner category;
    private EditText itemDescription;
    private EditText itemPrice;
    Button save,upload;

    Uri filePath;
    byte[] bytearray;
    StorageReference storage;

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
        upload =(Button) findViewById(R.id.uploadimage);
        products = FirebaseDatabase.getInstance().getReference("Products");
        storage = FirebaseStorage.getInstance().getReference();

       // FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReferenceFromUrl("testlogin-6db22.appspot.com");    //change the url according to your firebase app


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToDb();
                Snackbar.make(view, "Product Added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode ==RESULT_OK)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            //progressDialog.show();
            filePath = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bytearray = baos.toByteArray();
           /* StorageReference filePathStorage = storage.child("photos").child(filePath.getLastPathSegment());
            filePathStorage.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_LONG).show();
                    progressDialog.hide();
                }
            });*/

        }
    }

    private void addItemToDb() {

        final String id = products.push().getKey();
         String[] downloadUrl = new String[3];
        if(bytearray != null)
        {

            final StorageReference filePathStorage = storage.child("photos").child(id);
            UploadTask task = filePathStorage.putBytes(bytearray);

            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String urlD =taskSnapshot.getDownloadUrl().toString();
                    Product product = new Product();

                    product.setCategory(category.getSelectedItem().toString());
                    product.setPrice(Float.parseFloat(itemPrice.getText().toString()));
                    product.setName(itemName.getText().toString());
                    product.setDescription(itemDescription.getText().toString());
                    product.setImageUrl(urlD);
                    products.child(id).setValue(product);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }

        else {
            Product product = new Product();
            product.setCategory(category.getSelectedItem().toString());
            product.setPrice(Float.parseFloat(itemPrice.getText().toString()));
            product.setName(itemName.getText().toString());
            product.setDescription(itemDescription.getText().toString());
            products.child(id).setValue(product);

        }
    }

}
