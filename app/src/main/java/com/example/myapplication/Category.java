package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

public class Category extends AppCompatActivity {
    CardView BloodBankCard,ContactCard,AboutUsCard,DownloadsCard,MapCard,ServiceCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        BloodBankCard=findViewById(R.id.BloodBamk);
        ContactCard=findViewById(R.id.Contacts);
        DownloadsCard=findViewById(R.id.Downloads);
        MapCard=findViewById(R.id.Maps);
        ServiceCard=findViewById(R.id.Services);

        ContactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contact=new Intent(getApplicationContext(),GeneralContact.class);
                startActivity(contact);
            }
        });


        BloodBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),BloodBankPage.class);
                startActivity(intent);

            }
        });

        DownloadsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Downlad.class);
                startActivity(intent);
            }
        });


    }
}