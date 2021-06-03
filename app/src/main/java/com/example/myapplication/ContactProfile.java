package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactProfile extends AppCompatActivity {
    TextView name,post,ward,phone;
    CircleImageView profile;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        //declare the variables
        name=(TextView)findViewById(R.id.NameOfTheContact);
        post=(TextView)findViewById(R.id.postOfThecontact);
        ward=(TextView)findViewById(R.id.WardOfTheContact);
        phone=(TextView)findViewById(R.id.PhoneOfTheContact);

        //circle ImageView

        profile=(CircleImageView)findViewById(R.id.profileOfTheContact);

        cardView=findViewById(R.id.cardView);
        cardView.setBackgroundResource(R.drawable.roundcardview);


        name.setText(getIntent().getStringExtra("name"));
        post.setText(getIntent().getStringExtra("post"));
        ward.setText(getIntent().getStringExtra("ward"));
        phone.setText(getIntent().getStringExtra("phone"));
    }
}