package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DonorProfile;
import com.example.myapplication.Models.Model;
import com.example.myapplication.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {


    ArrayList<Model> mList;
    Context context;
    public Adapter(Context context, ArrayList<Model> mList){
        this.mList=mList;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View V= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Model model=mList.get(position);
        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());
        holder.bloodGroup.setText(model.getBloodgroup());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DonorProfile.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("phone",model.getPhone());
                intent.putExtra("bdgroup",model.getBloodgroup());
                intent.putExtra("places",model.getPlaces());
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
       return mList.size();
    }

    public static  class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView name,phone,bloodGroup;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.NameFromDatabase);
            phone=itemView.findViewById(R.id.PhoneNumberFromDatabase);
            bloodGroup=itemView.findViewById(R.id.BloodTypeFromDatabase);

            cardView=itemView.findViewById(R.id.cardV);
        }
    }
}
