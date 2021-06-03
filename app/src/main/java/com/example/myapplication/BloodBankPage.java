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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Adapters.Adapter;
import com.example.myapplication.Models.Model;
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

public class BloodBankPage extends AppCompatActivity {
    Button AddThe;
    FirebaseDatabase rootnode;
    DatabaseReference reference;
    EditText Name,Phone,Places;
    Spinner Blood;
    FloatingActionButton addPersonfloat;
    RecyclerView recyclerView1;
    FirebaseDatabase rootnodee= FirebaseDatabase.getInstance();
   DatabaseReference referencee=rootnodee.getReference().child("BloodBank");
    Adapter adapter;
    ArrayList<Model> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_page);



        addPersonfloat=findViewById(R.id.addperson);
        recyclerView1=findViewById(R.id.recycleView);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        list=new ArrayList<>();
        adapter=new Adapter(this,list);
        recyclerView1.setAdapter(adapter);
        referencee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Model model=dataSnapshot.getValue(Model.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        addPersonfloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpService();
            }
        });


    }

    private void PopUpService(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_pop_up);
        AddThe=dialog.findViewById(R.id.AddThePerson);
        Name=dialog.findViewById(R.id.NameEditText);
        Blood=dialog.findViewById(R.id.BloodPop);
        Phone=dialog.findViewById(R.id.PhoneNumberPop);
        Places=dialog.findViewById(R.id.Place);
        AddThe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootnode= FirebaseDatabase.getInstance();
                reference=rootnode.getReference().child("BloodBank");
                String name1=Name.getText().toString();
                String blood1=Blood.getSelectedItem().toString();
                String phone1=Phone.getText().toString();
                String place=Places.getText().toString();
                UserHelperClass helperClass=new UserHelperClass(name1,phone1,blood1,place);

                reference.push().setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(BloodBankPage.this, "Data has been saved .", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
    }
}