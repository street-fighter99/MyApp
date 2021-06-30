package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactProfile extends AppCompatActivity {
    TextView name,post,ward,phone;
    CircleImageView profile;
    CardView cardView;
    Button caller,updater,deleter,Add;

    EditText Name,Phone,Post,Ward;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        back=(ImageView) findViewById(R.id.backsign);

        //declare the variables
        name=(TextView)findViewById(R.id.NameOfTheContact);
        post=(TextView)findViewById(R.id.postOfThecontact);
        ward=(TextView)findViewById(R.id.WardOfTheContact);
        phone=(TextView)findViewById(R.id.PhoneOfTheContact);

        //Button
        caller=(Button)findViewById(R.id.Contactcall);
        updater=(Button)findViewById(R.id.Contactedit);
        deleter=(Button)findViewById(R.id.contactDeleter);

        //circle ImageView
        profile=(CircleImageView)findViewById(R.id.profileOfTheContact);

        cardView=findViewById(R.id.cardView);
        cardView.setBackgroundResource(R.drawable.roundcardview);

        //setting the textview with the data from the blood bank
        name.setText(getIntent().getStringExtra("name"));
        post.setText(getIntent().getStringExtra("post"));
        ward.setText(getIntent().getStringExtra("ward"));
        phone.setText(getIntent().getStringExtra("phone"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),GeneralContact.class));
                finish();
            }
        });

        caller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91"+getIntent().getStringExtra("phone")));
                startActivity(intent);

            }
        });

        updater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String user=currentFirebaseUser.getUid().toString();
                String dbuser=getIntent().getStringExtra("user");
                if (dbuser.equals(user)){
                    updatedata();

                }else{
                    Toast.makeText(ContactProfile.this, "you can't Delete this beacuse you're not the uploader ", Toast.LENGTH_SHORT).show();
                }




            }
        });
        deleter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String user=currentFirebaseUser.getUid().toString();
                String dbuser=getIntent().getStringExtra("user");

                if (dbuser.equals(user)){
                    String key =getIntent().getStringExtra("key");
                    FirebaseDatabase.getInstance().getReference().child("VillageOfficials").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ContactProfile.this, "Data is deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),GeneralContact.class));
                            finish();
                        }
                    });

                }else{
                    Toast.makeText(ContactProfile.this, "you can't Delete this beacuse you're not the uploader ", Toast.LENGTH_SHORT).show();
                }




            }
        });

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),GeneralContact.class));
        finish();
        super.onBackPressed();
    }

    private void updatedata() {
        Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.conpop);



        Name=(EditText) dialog.findViewById(R.id.NameEditText);
        Phone=(EditText) dialog.findViewById(R.id.PhoneNumberPop);
        Post=(EditText) dialog.findViewById(R.id.CPosition);
        Ward=(EditText) dialog.findViewById(R.id.Cward);
        Add=(Button) dialog.findViewById(R.id.AddThePerson);



        Name.setText(getIntent().getStringExtra("name"));
        Phone.setText(getIntent().getStringExtra("phone"));
        Post.setText(getIntent().getStringExtra("post"));
        Ward.setText(getIntent().getStringExtra("ward"));


        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Updatefire(Name,Phone,Post,Ward,dialog);

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
    }

    private void Updatefire(EditText name, EditText phone, EditText post, EditText ward, Dialog dialog) {
        Map<String,Object> map=new HashMap<>();
        map.put("cnNames",Name.getText().toString());
        map.put("cnPhones",Phone.getText().toString());
        map.put("cnPositions",Post.getText().toString());
        map.put("cnWard",Ward.getText().toString());

        String  key=getIntent().getStringExtra("key");

        FirebaseDatabase.getInstance().getReference().child("VillageOfficials").child(key).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(ContactProfile.this,"Data is Updated",Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),GeneralContact.class));
                        finish();


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ContactProfile.this,"Error>>>"+e,Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
    }
}