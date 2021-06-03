package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DonorProfile extends AppCompatActivity {

    TextView t1,t2,t3,t4;
     ImageView caller,deleter,editor;
     String BloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);


        t1=findViewById(R.id.RBTextView);
        t2=findViewById(R.id.NameOfThedonor);
        t3=findViewById(R.id.PlaceOfTheDonor);
        t4=findViewById(R.id.PhoneOfTheDonor);

        caller=findViewById(R.id.callDonor);
        deleter=findViewById(R.id.deleteDonor);
        editor=findViewById(R.id.editDonor);


        BloodGroup=getIntent().getStringExtra("bdgroup");

        switch (BloodGroup){
            case "O Positive(O+)":
                t1.setText("O+ve");
                break;

            case "O Negative(O-)":
                t1.setText("O-ve");
                break;
            case "A Positive(A+)":
                t1.setText("A+ve");
                break;
            case "A Negative(A-)":
                t1.setText("A-ve");
                break;
            case "B Positive(B+)":
                t1.setText("B+ve");
                break;
            case "B Negative(B-)":
                t1.setText("B-ve");
                break;
            default:
                t1.setText("Others");
        }

        t2.setText(getIntent().getStringExtra("name"));
        t3.setText(getIntent().getStringExtra("places"));
        t4.setText(getIntent().getStringExtra("phone"));




    }
}