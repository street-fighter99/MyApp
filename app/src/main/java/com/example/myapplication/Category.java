package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Category extends AppCompatActivity {

    CardView BloodBankCard,ContactCard,DownloadsCard,MapCard,ServiceCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_category);



        //Connect the CArdview variable with their ID
        BloodBankCard=findViewById(R.id.BloodBamk);
        ContactCard=findViewById(R.id.Contacts);
        DownloadsCard=findViewById(R.id.Downloads);
        MapCard=findViewById(R.id.Maps);
        ServiceCard=findViewById(R.id.Services);



        //make the cardview to go to certain pages

        MapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent contact=new Intent(getApplicationContext(),Webpage.class);
                startActivity(contact);
                finish();

            }
        });

        ContactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contact=new Intent(getApplicationContext(),GeneralContact.class);
                startActivity(contact);
                finish();
            }
        });



        BloodBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(getApplicationContext(),BloodBankPage.class);
                startActivity(intent);
                finish();

            }
        });

        DownloadsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Downlad.class);
                startActivity(intent);
                finish();
            }
        });

        ServiceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),ServicePage.class);
                startActivity(intent);
                finish();

            }
        });


    }
}