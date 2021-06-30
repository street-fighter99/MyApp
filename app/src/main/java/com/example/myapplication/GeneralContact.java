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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.Adapters.Adapter;
import com.example.myapplication.Adapters.ConatctAdapter;
import com.example.myapplication.Models.ConModel;
import com.example.myapplication.Models.Model;
import com.example.myapplication.UserHelper.ConHelper;
import com.example.myapplication.UserHelper.UserHelperClass;
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

public class GeneralContact extends AppCompatActivity {

    //initialize variables for the buttons and Edittext
    Button AddBttn,searchbttn;



    EditText Names,Phones,CPositions,Ward,Spost,Sward;

    //Firebase database and reference
    FirebaseDatabase CnFData=FirebaseDatabase.getInstance();

    DatabaseReference CnFRef=CnFData.getReference().child("VillageOfficials");


    FloatingActionButton AddDetailsBttn,SearchFloatBttn;

    RecyclerView recyclerView;

   ConatctAdapter conatctAdapters;

   ArrayList<ConModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_general_contact);



        //setting the variables with the ID
        AddDetailsBttn=findViewById(R.id.ContactAddperson);
        SearchFloatBttn=findViewById(R.id.ContactSearch);
        recyclerView=findViewById(R.id.ContactRecycler);

        //setting up the RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initialize the array
        list=new ArrayList<>();

        //passing the data to the adapter
        conatctAdapters=new ConatctAdapter(this,list);

        //setting up the  RecyclerView Aapter
        recyclerView.setAdapter(conatctAdapters);

        //getting the data from the database
        CnFRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                //for Each Loop
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    //Model class object and getting the value to it
                    ConModel conModel=dataSnapshot.getValue(ConModel.class);

                    String Key=dataSnapshot.getKey();

                    conModel.setKey(Key);
                    //add the data to the list
                    list.add(conModel);

                }

                //notify the adapter
                conatctAdapters.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }

        });



        //Action to the Float add Button
        AddDetailsBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Calling the function
                PopUpService();
            }
        });

        SearchFloatBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchPop();
            }
        });







    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),Category.class));
        finish();
        super.onBackPressed();
    }

    private void SearchPop() {

        //setting the dialog box
        final Dialog dialog=new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.search_contact_pop);

        Sward=dialog.findViewById(R.id.SearchWithWard);
        Spost=dialog.findViewById(R.id.SearchWithPost);
        searchbttn=dialog.findViewById(R.id.searchContactBttn);

        searchbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ward=Sward.getText().toString();
                String post=Spost.getText().toString();

                filter(post,ward);

                dialog.dismiss();


            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();


    }

    private void filter(String post,String ward) {

        ArrayList<ConModel> temp = new ArrayList<>();

        for (ConModel conModel : list) {

            if (ward.isEmpty() && !post.isEmpty()) {



                if (conModel.getCnPositions().toLowerCase().contains(post.toLowerCase())) {

                    temp.add(conModel);

                }


            }if (post.isEmpty() && !ward.isEmpty()){


                if (conModel.getCnWard().toLowerCase().contains(ward.toLowerCase())) {

                    temp.add(conModel);

                }

            }else if (!post.isEmpty() && !ward.isEmpty()){
                if (conModel.getCnPositions().toLowerCase().contains(post.toLowerCase()) && conModel.getCnWard().toLowerCase().contains(ward.toLowerCase()))
                {


                    temp.add(conModel);

                }
            }
                else if (post.isEmpty() && ward.isEmpty()){


                    temp.add(conModel);

                }


        }
        conatctAdapters.update(temp);
    }


    //function
    private void PopUpService(){

        //setting up the dailog
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //connecting the dialog with the layout
        dialog.setContentView(R.layout.conpop);

        //ID with variable
        AddBttn=(Button) dialog.findViewById(R.id.AddThePerson);
        Names=(EditText) dialog.findViewById(R.id.NameEditText);
        CPositions=(EditText) dialog.findViewById(R.id.CPosition);
        Phones=(EditText) dialog.findViewById(R.id.PhoneNumberPop);
        Ward=(EditText) dialog.findViewById(R.id.Cward);


    AddBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //connecting to the database
                CnFData= FirebaseDatabase.getInstance();
                CnFRef=CnFData.getReference().child("VillageOfficials");

                //getting the data from editText to string
                String name1=Names.getText().toString();
                String blood1=CPositions.getText().toString();
                String phone1=Phones.getText().toString();
                String ward1=Ward.getText().toString();
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String user=currentFirebaseUser.getUid().toString();

                //UserHelper Class
                ConHelper ConhelperClass=new ConHelper(name1,blood1,phone1,ward1,user);

                //uploading the data to the database
                CnFRef.push().setValue(ConhelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //if the data is upload completely then print this
                        Toast.makeText(GeneralContact.this, "Data has been saved .", Toast.LENGTH_SHORT).show();

                        conatctAdapters.notifyDataSetChanged();
                    }

                });

            }

        });

    //setting the bacground to the dialog
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(true);

        dialog.show();

    }

}