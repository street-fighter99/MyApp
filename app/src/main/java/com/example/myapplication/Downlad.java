package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapters.Adapter;
import com.example.myapplication.Adapters.DownAdapter;
import com.example.myapplication.Models.DownModel;
import com.example.myapplication.UserHelper.DownHelper;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class Downlad extends AppCompatActivity {


    Button UploadBttn;
    FloatingActionButton AddApplication,sear;
    EditText ApplicationName1,ApplicationDescription1,searchbar;
    ImageView ApplicationImage;


    Bitmap bitmap;
    Uri filePath;



    RecyclerView DRcl;
    FirebaseDatabase rootNode=FirebaseDatabase.getInstance();
    DatabaseReference dReference=rootNode.getReference().child("Application");
    DownAdapter adapter;
    ArrayList<DownModel> list;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downlad);

        AddApplication=findViewById(R.id.AddDownload);
        sear=findViewById(R.id.searchDown);

        //RecyclerView intialization

        DRcl=findViewById(R.id.downrecycler);

        DRcl.setHasFixedSize(true);
        DRcl.setLayoutManager(new GridLayoutManager(this,2));

        list=new ArrayList<>();
        adapter=new DownAdapter(list,this);
        DRcl.setAdapter(adapter);



        dReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    DownModel model=dataSnapshot.getValue(DownModel.class);
                    list.add(model);
                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        AddApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpUpload();
            }
        });




        searchbar=findViewById(R.id.searchBar);
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());


            }
        });




    }



    private void PopUpUpload(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.down_pop);

        UploadBttn=dialog.findViewById(R.id.UploadButton);
        ApplicationImage=dialog.findViewById(R.id.Application);
        ApplicationName1=dialog.findViewById(R.id.ApplicationName);
        ApplicationDescription1=dialog.findViewById(R.id.ApplicationDescription);

        ApplicationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity(Downlad.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"select The Form You Want To Upload"),1);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });



        UploadBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadToFirebase();


            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==1 && resultCode==RESULT_OK)
        {
            filePath=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(filePath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                ApplicationImage.setImageBitmap(bitmap);

            }catch(Exception ex){

            }

        }


        super.onActivityResult(requestCode, resultCode, data);

    }


    private void UploadToFirebase() {
        ProgressDialog proDialog=new ProgressDialog(this);
        proDialog.setTitle("File Uploaded");
        proDialog.show();











        FirebaseStorage Storage=FirebaseStorage.getInstance();
        StorageReference Sreference=Storage.getReference("Application1"+new Random().nextInt(50));



        Sreference.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String name=ApplicationName1.getText().toString();
                        String description=ApplicationDescription1.getText().toString();
                        Sreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference=database.getReference("Application");

                                DownHelper downHelper=new DownHelper(name,description,uri.toString());
                                databaseReference.child(name).setValue(downHelper);

                                ApplicationName1.setText("");
                                ApplicationDescription1.setText("");
                                ApplicationImage.setImageResource(R.drawable.application);
                                proDialog.dismiss();


                            }
                        });








                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                proDialog.setMessage("Uploaded : "+(int)percent+" %");

            }
        });






    }




   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        MenuItem menuItem=menu.findItem(R.id.searchmenu);

        SearchView searchView= (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                filter(newText);

                //adapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    private  void filter(String str){

        ArrayList<DownModel> temp=new ArrayList<>();
        try {


        for (DownModel obj:list){
            System.out.println("Name"+obj.getDescription());

            if (obj.getName().toLowerCase().contains(str.toLowerCase())){

               temp.add(obj);
           }
        }
        }catch (Exception e){
            System.out.println("error"+e);

        }
        adapter.updatelist(temp);


    }
}