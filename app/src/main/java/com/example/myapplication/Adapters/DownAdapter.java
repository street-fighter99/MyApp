package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Models.DownModel;
import com.example.myapplication.R;

import java.util.ArrayList;

public class DownAdapter extends RecyclerView.Adapter<DownAdapter.holder> implements Filterable

{
    private ArrayList<DownModel> dList;
    private ArrayList<DownModel> backup ;
    private final Context context;

    public DownAdapter(ArrayList<DownModel> dList, Context context) {
        this.dList = dList;
        this.context = context;
        this.backup=new ArrayList<>(dList);
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

    @Override
    public Filter getFilter() {


        return filter;
    }

    private Filter filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {

            ArrayList<DownModel> filtereddata=new ArrayList<>();

            if (keyword.toString().isEmpty() || keyword.length()==0){

                filtereddata.addAll(backup);

            }else{
                for (DownModel obj:backup){

                    if (obj.getName().toLowerCase().contains(keyword.toString().toLowerCase())){


                        filtereddata.add(obj);

                    }


                }



            }
            FilterResults results=new FilterResults();
            results.values=filtereddata;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dList.clear();
            dList.addAll((ArrayList<DownModel>)results.values);
             notifyDataSetChanged();



        }
    };

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
