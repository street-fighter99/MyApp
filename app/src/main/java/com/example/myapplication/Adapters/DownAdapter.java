package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Models.DownModel;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Collection;

public class DownAdapter extends RecyclerView.Adapter<DownAdapter.holder>

{
    private ArrayList<DownModel> dList;
    private ArrayList<DownModel> backup ;
    private final Context context;

    public DownAdapter(ArrayList<DownModel> dList, Context context) {
        this.dList = dList;
        this.context = context;
        backup=new ArrayList<>(dList);
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view=LayoutInflater.from(context).inflate(R.layout.down_item,parent,false);
        return new DownAdapter.holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownAdapter.holder holder, int position) {
        DownModel Dmodel=dList.get(position);
        holder.DocName.setText(Dmodel.getName());
        //holder.Desc=Dmodel.getDescription().toString();
       Glide.with(holder.img.getContext()).load(Dmodel.getImgUrl()).into(holder.img);




    }

    @Override
    public int getItemCount() {
        return dList.size();

    }




    public void updatelist(ArrayList<DownModel> temp) {
        System.out.println(temp);
        dList=temp;

        notifyDataSetChanged();
    }

    class holder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView DocName,seeMore;



        public holder(@NonNull  View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imgRcl);
            DocName=itemView.findViewById(R.id.DocumentName);
            //seeMore=itemView.findViewById(R.id.seemore);

        }
    }

}
