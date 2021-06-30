package com.example.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ContactProfile;
import com.example.myapplication.Models.ServiceModel;
import com.example.myapplication.R;
import com.example.myapplication.ServiceProfile;

import java.util.ArrayList;

public class ServiceAapter extends RecyclerView.Adapter<ServiceAapter.holder> {

    private ArrayList<ServiceModel> Slist;
    private Context context;

    public ServiceAapter(ArrayList<ServiceModel> slist, Context context) {
        this.Slist = slist;
        this.context = context;
    }

    @Override
    public ServiceAapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.service_recycler_card,parent,false);

        return new ServiceAapter.holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAapter.holder holder, int position) {

        ServiceModel model=Slist.get(position);

        holder.name.setText(model.getName());
        holder.phone.setText(model.getPlace());
        holder.service.setText(model.getService());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating an intend to pass the data
                Intent intent=new Intent(context, ServiceProfile.class);

                //passing the data
                intent.putExtra("name",model.getName());
                intent.putExtra("phone",model.getPhone());
                intent.putExtra("service",model.getService());
                intent.putExtra("place",model.getPlace());
                intent.putExtra("key",model.getKey());
                intent.putExtra("user",model.getUser());

                //start the activity
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });



    }

    @Override
    public int getItemCount() {
        return Slist.size();
    }

    public void update(ArrayList<ServiceModel> temp) {

        Slist=temp;
        notifyDataSetChanged();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView name,phone,service;
        CardView cardView;

        public holder(@NonNull View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.ServicerName);
            phone=(TextView)itemView.findViewById(R.id.Servicerplace);
            service=(TextView)itemView.findViewById(R.id.ServicerService);
            cardView=(CardView) itemView.findViewById(R.id.cardV);


        }
    }
}
