package com.example.myapplication.Adapters;

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


    private final ArrayList<ConModel> mList;
    private final Context context;

    public ConatctAdapter(android.content.Context context, ArrayList<ConModel> mList) {
        this.mList=mList;
        this.context=context;


    }

    @NonNull
    @Override
    public ConatctAdapter.ConViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View V= LayoutInflater.from(context).inflate(R.layout.cont_items,parent,false);
        return new ConatctAdapter.ConViewHolder(V);

    }

    @Override
    public void onBindViewHolder(@NonNull ConatctAdapter.ConViewHolder holder, int position) {

        ConModel models=mList.get(position);
        holder.names.setText(models.getCnNames());
        holder.phones.setText(models.getCnPhones());
        holder.bloodGroups.setText(models.getCnPositions());

        holder.cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ContactProfile.class);
                intent.putExtra("name",models.getCnNames());
                intent.putExtra("phone",models.getCnPhones());
                intent.putExtra("post",models.getCnPositions());
                intent.putExtra("ward",models.getCnWard());
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    public class ConViewHolder extends RecyclerView.ViewHolder{
        TextView names,phones,bloodGroups;

        CardView cardView1;

        public ConViewHolder(@NonNull View itemViews) {
            super(itemViews);

            names=itemViews.findViewById(R.id.conNameFromDatabase);
            phones=itemViews.findViewById(R.id.conPhoneNumberFromDatabase);
            bloodGroups=itemViews.findViewById(R.id.conBloodTypeFromDatabase);
            cardView1=itemViews.findViewById(R.id.ConCard);

        }
    }
}
