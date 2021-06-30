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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Adapters.Adapter;
import com.example.myapplication.Models.Model;
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

public class BloodBankPage extends AppCompatActivity {

    Button AddThe,SearchButton;

    EditText Name,Phone,Places,placespop;

    Spinner Blood,SBlood;

    FloatingActionButton FloatAddButton,FloatSearchButton;

    RecyclerView recyclerView1;

    FirebaseDatabase rootnode,rootnodee= FirebaseDatabase.getInstance();

    DatabaseReference reference,referencee=rootnodee.getReference().child("BloodBank");

    Adapter adapter;

    ArrayList<Model> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_page);

        //floating button sett
        FloatSearchButton=(FloatingActionButton) findViewById(R.id.search);
        FloatAddButton=(FloatingActionButton) findViewById(R.id.addperson);


        //Recyclerview sett
        recyclerView1=(RecyclerView) findViewById(R.id.recycleView);

        //recyclerview set fixed size
        recyclerView1.setHasFixedSize(true);

        //setting layout manager like grid or linear
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        //initilizing list array
        list=new ArrayList<>();

        //giving the list and the context to adapter to work
        //until now the list has no data
        adapter=new Adapter(this,list);

        //setting the adapter for the recycler view to work
        recyclerView1.setAdapter(adapter);


        referencee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                //using the for each loop to get all the data from the firebase
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    String id=dataSnapshot.getKey();



                    //getting Model class and getting the values from the dataSnapshot it contain all the values
                    Model model=dataSnapshot.getValue(Model.class);




                    model.setBloodkey(id);

                    //we add data to Array list as per the Model class
                    list.add(model);

                }


                //we are updating the adapter because now the list has changed or it have data
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //toasting the error message
                Toast.makeText(BloodBankPage.this, "ERROR>> "+error, Toast.LENGTH_SHORT).show();

            }
        });




        //adding action to the float search button
        FloatSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //callling the searchpop function
                Searchpop();
            }
        });

        //adding action to the float Adding button
        FloatAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpService();
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Category.class));
        finish();
        super.onBackPressed();
    }

    //created the popup to add donors to the database
    //for that we need a pop up window
    //to create a pop up window we here use Dialog boxes
    private void PopUpService(){

        //calling the dialog or initializing the dialog
        final Dialog dialog=new Dialog(this);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //setting the layout for the dialog popup window
        dialog.setContentView(R.layout.activity_pop_up);

        //connecting the buttons with their id from the layout
        Name=(EditText) dialog.findViewById(R.id.NameEditText);
        Phone=(EditText) dialog.findViewById(R.id.PhoneNumberPop);
        Places=(EditText) dialog.findViewById(R.id.Place);
        Blood=(Spinner) dialog.findViewById(R.id.BloodPop);
        AddThe=(Button) dialog.findViewById(R.id.AddThePerson);



        AddThe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the database connected to the project
                rootnode= FirebaseDatabase.getInstance();

                //reference that connect to the child node as per your need
                reference=rootnode.getReference().child("BloodBank");

                //changing the text that we get from the Edit Text to Strings
                //because in our USERHELPER class we only use strings
                String name1=Name.getText().toString();
                String phone1=Phone.getText().toString();
                String place=Places.getText().toString();
                String blood1=Blood.getSelectedItem().toString();
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String user=currentFirebaseUser.getUid().toString();

                //Passing the Strings to the helper class
                UserHelperClass helperClass=new UserHelperClass(name1,phone1,blood1,place,user);

                //we are uploading the data to the firebase
                reference
                        //to push the data
                        .push()
                        //In here there are multiple data so we upload using helper class
                        .setValue(helperClass)
                        //this help you to do things after compleating or you now upload has done
                        .addOnCompleteListener(new OnCompleteListener<Void>() {


                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(BloodBankPage.this, ">>>Data has been saved.<<<", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        //setting the bacground for the  dialog
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //setting the dialog cancelable so you can dissmiss the dialog easily
        dialog.setCancelable(true);

        //it show the dialog
        dialog.show();
    }


    //searchpop function
    private void Searchpop(){
        //Setting the dialog box
        final Dialog searchDialog=new Dialog(this);

        searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //setting the layout for the dialog popup window
        searchDialog.setContentView(R.layout.blood_search_pop);

        //connect the variable with id
        SearchButton=(Button) searchDialog.findViewById(R.id.SearchBloodG);
        placespop=(EditText) searchDialog.findViewById(R.id.SearchPlace);
        SBlood=(Spinner) searchDialog.findViewById(R.id.SearchBlood);

        //adding action to the search button in popup
        SearchButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                //convert to string from text
                String place=placespop.getText().toString();
                String blood=SBlood.getSelectedItem().toString();

                //calling the filter function and passing the string
                filter(place,blood);

                //after the filter process done the search dialog dissappear automatically
                searchDialog.dismiss();

            }

        });

        //setting bacground
        searchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //setting cancelable
        searchDialog.setCancelable(true);

        //show the popup
        searchDialog.show();

    }



    private void filter(String place, String blood)

    {
        //initialize a temperory array for filtering data
        ArrayList<Model> temp=new ArrayList<>();

        //for each loop for the list (main data) array
        for (Model model:list){

            //checking the place edit text is empty or not
            if (place.isEmpty())

            //if it is then
            {
              //if the edit text is empty
              //we are checking blood group from the list array
            if (model.getBloodgroup().toLowerCase().contains(blood.toLowerCase()))
            {

                //copying the data from the list to the temp array as per the model class
                temp.add(model);

            }

            }
            //checking the place and blood group with the list array
            else if(model.getPlaces().toLowerCase().contains(place.toLowerCase()) && model.getBloodgroup().toLowerCase().contains(blood.toLowerCase()))
            {

                //copying the data we got from the search to the temporary array
                temp.add(model);


            }

        }

        //we calling the funtion inside the adapter class and passing the temp array to it
        adapter.update(temp);

    }



}