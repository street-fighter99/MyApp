package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collection;

public class DownAdapter extends RecyclerView.Adapter<DownAdapter.holder>

{
    //variables
    private ArrayList<DownModel> dList;
    private final Context context;

    //Constructor
    public DownAdapter(ArrayList<DownModel> dList, Context context) {
        this.dList = dList;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //attach the layout with the adapter
        View view=LayoutInflater.from(context).inflate(R.layout.down_item,parent,false);
        return new DownAdapter.holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownAdapter.holder holder, int position) {

        //copying the data from  arraylist
        DownModel Dmodel=dList.get(position);

        //setting the text view with the name get from the model
        holder.DocName.setText(Dmodel.getName());

        //holder.Desc=Dmodel.getDescription().toString();

      // Glide.with(holder.img.getContext()).load(Dmodel.getImgUrl()).into(holder.img);

       holder.seeMore.setText(Dmodel.getDescription());

       holder.deleteBttn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
               String user=currentFirebaseUser.getUid().toString();
               String dbuser=Dmodel.getUser();

               if (dbuser.equals(user)){

                   String key =Dmodel.getKey();
                   String url=Dmodel.getImgUrl();
                   FirebaseStorage.getInstance().getReferenceFromUrl(url)
                           .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           FirebaseDatabase.getInstance().getReference().child("Application").child(key).removeValue()
                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void unused) {
                                           Toast.makeText(context, ">>>>Data is DELETED<<<<<", Toast.LENGTH_SHORT).show();
                                           notifyDataSetChanged();



                                       }
                                   });
                       }
                   });

               } else {
                   Toast.makeText(context, "....Only the one who uploaded the file can distroy  it.... ", Toast.LENGTH_SHORT).show();
               }





           }
       });


       holder.downloadBttn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(Intent.ACTION_VIEW);
               intent.setType("application/pdf");
               intent.setData(Uri.parse(Dmodel.getImgUrl()));
               context.startActivity(intent);


           }
       });
    }

    @Override
    public int getItemCount() {

        //return the array size
        return dList.size();

    }




    public void updatelist(ArrayList<DownModel> temp) {

        //copying the data from temp to dlist
        dList=temp;

        //notify the adapte or refresh the data
        notifyDataSetChanged();

    }


    class holder extends RecyclerView.ViewHolder
    {
        //initialize data
        ImageView img;
        TextView DocName,seeMore;
        Button downloadBttn,deleteBttn;

        public holder(@NonNull  View itemView) {
            super(itemView);

            //connecting the variable with the ID
            DocName=(TextView) itemView.findViewById(R.id.DocumentName);
            seeMore=itemView.findViewById(R.id.seemore);
            downloadBttn=itemView.findViewById(R.id.PDFDownloads);
            deleteBttn=itemView.findViewById(R.id.Delete);



        }
    }

}
