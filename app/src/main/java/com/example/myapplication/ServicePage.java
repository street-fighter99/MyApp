package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Adapters.ServiceAapter;
import com.example.myapplication.Models.ServiceModel;
import com.example.myapplication.UserHelper.ServiceHelper;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServicePage extends AppCompatActivity {

    //recycler
    RecyclerView recyclerView;

    //floating minibuttons
    FloatingActionButton floatAddButton,floatSearchButton;

    //adapter
    ServiceAapter adapter;

    //arraylist
    ArrayList<ServiceModel> list;

    //firebase Database and it's reference
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference().child("Service Database");

    //add dialog EDIT TEXT
    EditText DET1,DET2,DET3;

    //add DIALOG SPINNER
    Spinner DS1;

    //add dialog button
    Button DBttn;

    //search dialog edittext
    EditText SET1;

    //search dialog spinner
    Spinner SS1;

    //search dialog button
    Button SBttn;

    //Database and reference for add
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_page);

        //setting up with IDs
        floatAddButton=(FloatingActionButton)findViewById(R.id.FloatAddBttn);
        floatSearchButton=(FloatingActionButton)findViewById(R.id.FloatSearchBttn);

        //SettingUp with the ID
        recyclerView=(RecyclerView)findViewById(R.id.Recycler);
        recyclerView.setHasFixedSize(true);

        //Setting the layout model for the  recycler
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //list initialization
        list=new ArrayList<>();

        //passing the data and context to the adapter
        adapter=new ServiceAapter(list,this);

        //setting adapter
        recyclerView.setAdapter(adapter);


        //getting the data through the Database Reference
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //if there is no error getting the data
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    ServiceModel serviceModel=dataSnapshot.getValue(ServiceModel.class);

                    String Key=dataSnapshot.getKey();

                    serviceModel.setKey(Key);

                    list.add(serviceModel);

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ServicePage.this, "Error>>>"+error, Toast.LENGTH_SHORT).show();

            }

        });

        floatAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddData();

            }
        });


        floatSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchData();

            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Category.class));
        finish();
        super.onBackPressed();
    }

    private void SearchData() {

        final Dialog SDialog=new Dialog(this);

        SDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        SDialog.setContentView(R.layout.search_dialog);

        SET1=(EditText)SDialog.findViewById(R.id.Search_Servicer_place);

        SS1=(Spinner)SDialog.findViewById(R.id.search_Servicer_service);

        SBttn=(Button)SDialog.findViewById(R.id.search_servicer_Data);

        SBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String place,service;
                place=SET1.getText().toString();
                service=SS1.getSelectedItem().toString();

                if (place.isEmpty() && service.equalsIgnoreCase("choose service")){
                    SET1.setError("Enter place or select service.");
                    SET1.requestFocus();
                }else {

                    search(place, service);

                    SDialog.dismiss();
                }

            }
        });


        SDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        SDialog.setCancelable(true);
        SDialog.show();


    }

    private void search(String place, String service) {

        ArrayList<ServiceModel> temp=new ArrayList<>();

        for (ServiceModel model:list)
        {
            if (place.isEmpty())
            {

                if (model.getService().toLowerCase().contains(service.toLowerCase()))
                {
                    temp.add(model);

                }



            }


            if (service.equalsIgnoreCase("choose service"))

            {

                if (model.getPlace().toLowerCase().contains(place.toLowerCase())){

                    temp.add(model);
                }


            }else if (model.getPlace().toLowerCase().contains(place.toLowerCase()) && model.getService().toLowerCase().contains(service.toLowerCase())){

                temp.add(model);
            }

        }



        adapter.update(temp);


    }

    private void AddData() {

        final Dialog dialog=new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.add_dialog);

        DET1=(EditText)dialog.findViewById(R.id.Servicer_name);
        DET2=(EditText)dialog.findViewById(R.id.Servicer_phone);
        DET3=(EditText)dialog.findViewById(R.id.Servicer_place);

        DS1=(Spinner)dialog.findViewById(R.id.Servicer_service);

        DBttn=(Button)dialog.findViewById(R.id.Add_servicer_Data);

        DBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseDatabase=FirebaseDatabase.getInstance();

                databaseReference=firebaseDatabase.getReference().child("Service Database");

                String name,service,phone,place;

                name=DET1.getText().toString();
                phone=DET2.getText().toString();
                place=DET3.getText().toString();
                service=DS1.getSelectedItem().toString();

                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String user=currentFirebaseUser.getUid().toString();

                ServiceHelper serviceHelper=new ServiceHelper(name,service,phone,place,user);

                databaseReference
                        .push()
                        .setValue(serviceHelper)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ServicePage.this, ">>THE DATA IS UPLOADED<<", Toast.LENGTH_SHORT).show();

                                
                                dialog.dismiss();
                            }

                        });



            }

        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

    }


}