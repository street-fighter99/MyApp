package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GeneralContact extends AppCompatActivity {
    Button AddBttn;
    EditText Names,Phones,CPositions,Ward;

    FirebaseDatabase CnFData=FirebaseDatabase.getInstance();
    DatabaseReference CnFRef=CnFData.getReference().child("VillageOfficials");

    FloatingActionButton AddDetailsBttn;
    RecyclerView recyclerView;

   ConatctAdapter conatctAdapters;
   ArrayList<ConModel> list;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_contact);

        AddDetailsBttn=findViewById(R.id.ContactAddperson);

        recyclerView=findViewById(R.id.ContactRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        conatctAdapters=new ConatctAdapter(this,list);
        recyclerView.setAdapter(conatctAdapters);
        CnFRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ConModel conModel=dataSnapshot.getValue(ConModel.class);
                    list.add(conModel);
                }
                conatctAdapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        AddDetailsBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpService();
            }
        });


    }
    private void PopUpService(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.conpop);
        AddBttn=dialog.findViewById(R.id.AddThePerson);
        Names=dialog.findViewById(R.id.NameEditText);
        CPositions=dialog.findViewById(R.id.CPosition);
        Phones=dialog.findViewById(R.id.PhoneNumberPop);
        Ward=dialog.findViewById(R.id.Cward);
    AddBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CnFData= FirebaseDatabase.getInstance();
                CnFRef=CnFData.getReference().child("VillageOfficials");
                String name1=Names.getText().toString();
                String blood1=CPositions.getText().toString();
                String phone1=Phones.getText().toString();
                String ward1=Ward.getText().toString();

                ConHelper ConhelperClass=new ConHelper(name1,phone1,blood1,ward1);

                CnFRef.push().setValue(ConhelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(GeneralContact.this, "Data has been saved .", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
    }
}