package com.example.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ContactProfile;
import com.example.myapplication.GeneralContact;
import com.example.myapplication.Models.ConModel;
import com.example.myapplication.Models.Model;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ConatctAdapter extends RecyclerView.Adapter<ConatctAdapter.ConViewHolder> {


    //array an context variable
    private ArrayList<ConModel> mList;
    private final Context context;

    public ConatctAdapter(android.content.Context context, ArrayList<ConModel> mList)
    {

        //initializing the context and array with the data we got from the contact page
        this.mList=mList;
        this.context=context;

    }

    @NonNull
    @Override
    public ConatctAdapter.ConViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //connecting the view holder with the item xml file
        View V= LayoutInflater.from(context).inflate(R.layout.cont_items,parent,false);
        return new ConatctAdapter.ConViewHolder(V);

    }

    @Override
    public void onBindViewHolder(@NonNull ConatctAdapter.ConViewHolder holder, int position) {

        //setting the moel object
        ConModel models=mList.get(position);

        //setting the variables with the values
        holder.names.setText(models.getCnNames());
        holder.phones.setText(models.getCnPhones());
        holder.bloodGroups.setText(models.getCnPositions());

        //setting what to do when we click on the card view
        holder.cardView1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {

                //creating an intend to pass the data
                Intent intent=new Intent(context, ContactProfile.class);

                //passing the data
                intent.putExtra("name",models.getCnNames());
                intent.putExtra("phone",models.getCnPhones());
                intent.putExtra("post",models.getCnPositions());
                intent.putExtra("ward",models.getCnWard());
                intent.putExtra("key",models.getKey());
                intent.putExtra("user",models.getUser());

                //start the activity
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity)context).finish();

            }

        });

    }

    @Override
    public int getItemCount() {

        //return the size of the main array list
        return mList.size();

    }

    public void update(ArrayList<ConModel> temp) {
        mList=temp;
        notifyDataSetChanged();

    }


    public class ConViewHolder extends RecyclerView.ViewHolder
    {

        //creating variables for text view and card view

        TextView names,phones,bloodGroups;
        CardView cardView1;

        public ConViewHolder(@NonNull View itemViews) {
            super(itemViews);

            //connecting the  variable with the xml file
            names=itemViews.findViewById(R.id.conNameFromDatabase);
            phones=itemViews.findViewById(R.id.conPhoneNumberFromDatabase);
            bloodGroups=itemViews.findViewById(R.id.conBloodTypeFromDatabase);
            cardView1=itemViews.findViewById(R.id.ConCard);

        }
    }
}
