package com.example.myapplication.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DonorProfile;
import com.example.myapplication.Models.Model;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    //initializing the array and Context
    ArrayList<Model> mList;
    Context context;

    //Constructor
    public Adapter(Context context, ArrayList<Model> mList)

    {
        this.mList=mList;

        this.context=context;

    }



    //update function for the filtering
   public void update(ArrayList<Model> temp) {

        //copying the data from temp to the main list
        mList=temp;

        //refreshing the adapter by sending the notification
        notifyDataSetChanged();

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //connecting the recycle model to the recycler view
        View V= LayoutInflater.from(context).inflate(R.layout.item,parent,false);

        //return the View to the view holder
        return  new MyViewHolder(V);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //getting the data from the main list to the model by the position
        Model model=mList.get(position);

        //here holder connect to the View holder
        //from view holder we get the variable and get the data from the model
        //and set it
        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());
        holder.bloodGroup.setText(model.getBloodgroup());

        //String key=FirebaseDatabase.getInstance().getReference().ref






//        holder.UpdateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog dialog=new Dialog(this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.activity_pop_up);
//
//                FirebaseDatabase database;
//                final DatabaseReference[] reference = new DatabaseReference[1];
//
//
//                EditText Name=(EditText) dialog.findViewById(R.id.NameEditText);
//                EditText Phone=(EditText) dialog.findViewById(R.id.PhoneNumberPop);
//                EditText Places=(EditText) dialog.findViewById(R.id.Place);
//                Spinner Blood=(Spinner) dialog.findViewById(R.id.BloodPop);
//                Button update=(Button) dialog.findViewById(R.id.AddThePerson);
//
//
//                Name.setText(model.getName());
//                Phone.setText(model.getPhone());
//                Places.setText(model.getPlaces());
//
//
//               update.setOnClickListener(new View.OnClickListener() {
//                   @Override
//                   public void onClick(View v) {
//                       Map<String,Object> map=new HashMap<>();
//                       map.put("name",Name.getText().toString());
//                       map.put("phone",Phone.getText().toString());
//                       map.put("places",Places.getText().toString());
//                       map.put("bloodgroup",Blood.getSelectedItem().toString());
//
//
//                       reference =FirebaseDatabase.getInstance().getReference().child("BloodBank")
//                   }
//               });
//
//            }
//        });

        //giving the card view some action
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //we intialize a Intent
                Intent intent=new Intent(context, DonorProfile.class);




                //we pass the data to the page using the intent object
                intent.putExtra("name",model.getName());
                intent.putExtra("phone",model.getPhone());
                intent.putExtra("bdgroup",model.getBloodgroup());
                intent.putExtra("places",model.getPlaces());
                intent.putExtra("key",model.getBloodkey());
                intent.putExtra("user",model.getUser());


                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
                ((Activity)context).finish();




            }
        });
    }



    @Override
    public int getItemCount() {

        //return the size of the main array
        return mList.size();
    }



    public static  class MyViewHolder extends  RecyclerView.ViewHolder{

        //variables
        TextView name,phone,bloodGroup;
        CardView cardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //connecting the variables with the recycler model
            name=(TextView) itemView.findViewById(R.id.NameFromDatabase);
            phone=(TextView) itemView.findViewById(R.id.PhoneNumberFromDatabase);
            bloodGroup=(TextView) itemView.findViewById(R.id.BloodTypeFromDatabase);
            cardView=(CardView) itemView.findViewById(R.id.cardV);
           ;
        }
    }
}
