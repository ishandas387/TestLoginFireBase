package com.ishan387.testlogin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class CustomerCare extends AppCompatActivity {

    ImageView call, mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus);

        call = (ImageView) findViewById(R.id.call_care);
        mail = (ImageView) findViewById(R.id.mail_care);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "08527678238"));
                if (ActivityCompat.checkSelfPermission(CustomerCare.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CustomerCare.this, "Hey Marshmellow Smarty\nAuthorize This app to make calls", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(intent);

            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "mejablu@gmail.com","jerseytownindia@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Customers Mail");
                email.putExtra(Intent.EXTRA_TEXT, "Sent From Android App\n");
                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

    }
}
