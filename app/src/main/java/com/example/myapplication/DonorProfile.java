package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapters.Adapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DonorProfile extends AppCompatActivity {

    //initialize variable
    TextView t1,t2,t3,t4;
    ImageView back;

    String BloodGroup;


    //dialog
    EditText Name,Phone,Places;
    Spinner Blood;
    Button update,caller,deleter,editor;

    Adapter adapter;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);


        //setting the variables to the ID
        t1=(TextView) findViewById(R.id.RBTextView);
        t2=(TextView) findViewById(R.id.NameOfThedonor);
        t3=(TextView) findViewById(R.id.PlaceOfTheDonor);
        t4=(TextView) findViewById(R.id.PhoneOfTheDonor);

        //Image View
        caller=(Button) findViewById(R.id.callDonor);
        deleter=(Button) findViewById(R.id.deleteDonor);
        editor=(Button) findViewById(R.id.editDonor);
        back=(ImageView) findViewById(R.id.backsign);

        //getting the data from the blood bank
        BloodGroup=getIntent().getStringExtra("bdgroup");

        //setting the switch for the text view
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

        //getting the data from the intent
        //and setting it up
        t2.setText(getIntent().getStringExtra("name"));
        t3.setText(getIntent().getStringExtra("places"));
        t4.setText(getIntent().getStringExtra("phone"));



       caller.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent=new Intent(Intent.ACTION_DIAL);
               intent.setData(Uri.parse("tel:+91"+getIntent().getStringExtra("phone")));
               startActivity(intent);
           }
       });



       deleter.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
               String user=currentFirebaseUser.getUid().toString();
               String dbuser=getIntent().getStringExtra("user");
               if (dbuser.equals(user)){
                   deletedata();

               }else{
                   Toast.makeText(DonorProfile.this, "you can't Delete this beacuse you're not the uploader ", Toast.LENGTH_SHORT).show();
               }


           }
       });

       editor.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
               String user=currentFirebaseUser.getUid().toString();
               String dbuser=getIntent().getStringExtra("user");
               if (dbuser.equals(user)){
                   updatedata();

               }else{
                   Toast.makeText(DonorProfile.this, "you can't Edit this beacuse you're not the uploader ", Toast.LENGTH_SHORT).show();
               }



           }
       });

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),BloodBankPage.class));
               finish();
           }
       });



    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),BloodBankPage.class));
        finish();
        super.onBackPressed();
    }

    private void deletedata() {

        String key =getIntent().getStringExtra("key");
        FirebaseDatabase.getInstance().getReference().child("BloodBank").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(DonorProfile.this, "Data is deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),BloodBankPage.class));
                finish();
            }
        });





    }

    private void updatedata() {
        Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_pop_up);



        Name=(EditText) dialog.findViewById(R.id.NameEditText);
        Phone=(EditText) dialog.findViewById(R.id.PhoneNumberPop);
        Places=(EditText) dialog.findViewById(R.id.Place);
        Blood=(Spinner) dialog.findViewById(R.id.BloodPop);
        update=(Button) dialog.findViewById(R.id.AddThePerson);


        Name.setText(getIntent().getStringExtra("name"));
        Phone.setText(getIntent().getStringExtra("phone"));
        Places.setText(getIntent().getStringExtra("places"));


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Updatefire(Name,Phone,Places,Blood,dialog);

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

    }

    private void Updatefire(EditText name, EditText phone, EditText places, Spinner blood, Dialog dialog) {

        Map<String,Object> map=new HashMap<>();
        map.put("name",Name.getText().toString());
        map.put("phone",Phone.getText().toString());
        map.put("places",Places.getText().toString());
        map.put("bloodgroup",Blood.getSelectedItem().toString());

        String  key=getIntent().getStringExtra("key");

        FirebaseDatabase.getInstance().getReference().child("BloodBank").child(key).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(DonorProfile.this,"Data is Updated",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        startActivity(new Intent(getApplicationContext(),BloodBankPage.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DonorProfile.this,"Error>>>"+e,Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
    }
}
