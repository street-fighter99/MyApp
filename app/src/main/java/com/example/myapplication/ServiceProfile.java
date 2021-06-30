package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ServiceProfile extends AppCompatActivity {
    TextView name,place,service,phone;
    Button deleter,updater,caller;


    EditText Ename,Ephone,Eplace;
    Spinner EService;
    Button Update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_profile);

        name=findViewById(R.id.Nameser);
        place=findViewById(R.id.Placeser);
        service=findViewById(R.id.servicer);
        phone=findViewById(R.id.Phoneser);


        deleter=findViewById(R.id.deleteservice);
        caller=findViewById(R.id.callservice);
        updater=findViewById(R.id.editservice);


        name.setText(getIntent().getStringExtra("name"));
        place.setText(getIntent().getStringExtra("place"));
        service.setText(getIntent().getStringExtra("service"));
        phone.setText(getIntent().getStringExtra("phone"));

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


                FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
                String user=currentuser.getUid().toString();
                String dbuser=getIntent().getStringExtra("user");

                if (dbuser.equals(user)){

                    deleteservice();
                }

                else{
                    Toast.makeText(ServiceProfile.this, "you can't delete this data B/c you're not the uploader", Toast.LENGTH_SHORT).show();

                }
            }
        });

        updater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FirebaseUser current=FirebaseAuth.getInstance().getCurrentUser();
               String user=current.getUid().toString();
               String dbuser=getIntent().getStringExtra("user");

               if (dbuser.equals(user)){
                   updatedata();
               }
               else{
                   Toast.makeText(ServiceProfile.this, "YOU CAN'T UPDATE THIS \n USER DIDN'T MATCH ", Toast.LENGTH_SHORT).show();
               }
            }
        });



    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ServicePage.class));
        finish();
        super.onBackPressed();
    }

    private void updatedata() {
        Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_dialog);


        Ename=dialog.findViewById(R.id.Servicer_name);
        Ephone=dialog.findViewById(R.id.Servicer_phone);
        Eplace=dialog.findViewById(R.id.Servicer_place);

        EService=dialog.findViewById(R.id.Servicer_service);

        Update=dialog.findViewById(R.id.Add_servicer_Data);

        Ename.setText(getIntent().getStringExtra("name"));
        Eplace.setText(getIntent().getStringExtra("place"));
        Ephone.setText(getIntent().getStringExtra("phone"));

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Updatefire(Ename,Ephone,Eplace,EService,dialog);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();


    }

    private void Updatefire(EditText ename, EditText ephone, EditText eplace, Spinner eService, Dialog dialog) {

        Map<String,Object> map=new HashMap<>();
        map.put("name",ename.getText().toString());
        map.put("phone",ephone.getText().toString());
        map.put("place",eplace.getText().toString());
        map.put("service",eService.getSelectedItem().toString());

        String key=getIntent().getStringExtra("key");

        FirebaseDatabase.getInstance().getReference().child("Service Database").child(key).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ServiceProfile.this,"Data is Updated",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),ServicePage.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ServiceProfile.this,"Error>>>"+e,Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });




    }

    private void deleteservice() {

        String key=getIntent().getStringExtra("key");
        FirebaseDatabase.getInstance().getReference().child("Service Database").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ServiceProfile.this, "Data is deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),ServicePage.class));
                finish();
            }
        });


    }
}