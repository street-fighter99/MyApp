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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    FloatingActionButton AddForms,searchFloat;

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

        //setting up the variables
        AddForms=findViewById(R.id.AddDownload);
       // searchFloat=findViewById(R.id.searchDown);
        searchbar=findViewById(R.id.searchBar);

        //RecyclerView intialization
        DRcl=findViewById(R.id.downrecycler);

        DRcl.setHasFixedSize(true);

        //seeting the layout manager as grid layout
        DRcl.setLayoutManager(new LinearLayoutManager(this));

        //initialize the arraylist
        list=new ArrayList<>();

        //sending data to the adapter
        adapter=new DownAdapter(list,this);

        //set the adapter
        DRcl.setAdapter(adapter);

        //using the data reference add value
        dReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                //using the for each loop for getting the data from the fire base
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    String key=dataSnapshot.getKey();

                    //we copy the data from the snapshot to the model class
                    DownModel model=dataSnapshot.getValue(DownModel.class);

                    model.setKey(key);

                    //copy the data from the model to the list
                    list.add(model);

                }

                //notifying the adapter
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        AddForms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //calling the function
                PopUpUpload();
            }
        });


       //by using this on every letter you type it add automatically pass it
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //calling the function and passing the data
                filter(s.toString());

            }

        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Category.class));
        finish();
        super.onBackPressed();
    }


    //Function for uploading the data through a popup
    private void PopUpUpload(){

        //initialize the dialog
        final Dialog dialog=new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //setting the layout to the popup
        dialog.setContentView(R.layout.down_pop);

        //initialize the variables with the id
        UploadBttn=(Button) dialog.findViewById(R.id.UploadButton);
        ApplicationImage=(ImageView) dialog.findViewById(R.id.Application);
        ApplicationName1=(EditText) dialog.findViewById(R.id.ApplicationName);
        ApplicationDescription1=(EditText) dialog.findViewById(R.id.ApplicationDescription);

        //setting the imageview to upload image to the data base and
        //convert image to bitmap
        ApplicationImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Dexter is implementation it help with getting the image view
                //from the files
                Dexter.withActivity(Downlad.this)
                        //getting the permission to read file
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        //after getting the permission
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {


                                //if permission is given then start picking the file
                                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);

                                //set the type image to get the image only
                                intent.setType("application/pdf");

                                //get the result for the intent
                                startActivityForResult(Intent.createChooser(intent,"select The Form You Want To Upload"),1);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                //if the permission is denied

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                //to ask permission continuesly until it given
                                token.continuePermissionRequest();
                            }

                        }).check();

            }

        });

        //setting the uppload button
        UploadBttn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //calling the function
                UploadToFirebase();

            }

        });

        //setting the bacground
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCancelable(true);

        dialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //if the results are ok
        if (requestCode==1 && resultCode==RESULT_OK)
        {
            //getting the file path
            filePath=data.getData();

            try {
                //InputStream
                InputStream inputStream=getContentResolver().openInputStream(filePath);

                //convert the img to bitmap
                bitmap= BitmapFactory.decodeStream(inputStream);

                //setting the image view
                ApplicationImage.setImageBitmap(bitmap);


            }catch(Exception ex){

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void UploadToFirebase() {

        //dialog
        ProgressDialog proDialog=new ProgressDialog(this);

        //setting text to the dialog
        proDialog.setTitle("File Uploaded");

        //to show the dialog
        proDialog.show();

        //getting the database
        FirebaseStorage Storage=FirebaseStorage.getInstance();

        //getting the reference to the storage
        StorageReference Sreference=Storage.getReference("Application1"+new Random().nextInt(50));

        //uploading the file
        Sreference.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    //if the file upload successfully
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //convert text to string
                        String name=ApplicationName1.getText().toString();
                        String description=ApplicationDescription1.getText().toString();


                        //storage reference to get the url for the image uploaded recently
                        Sreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {


                            @Override
                            public void onSuccess(Uri uri) {

                                //realtime database
                                FirebaseDatabase database=FirebaseDatabase.getInstance();

                                //reference
                                DatabaseReference databaseReference=database.getReference("Application");

                                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                String user=currentFirebaseUser.getUid().toString();

                                //helper class
                                DownHelper downHelper=new DownHelper(name,description,uri.toString(),user);

                                //putting the data
                                databaseReference.push().setValue(downHelper);

                                //reset the editText
                                ApplicationName1.setText("");
                                ApplicationDescription1.setText("");

                                //reset the image view
                                ApplicationImage.setImageResource(R.drawable.application);

                                //dissmiss the dialog
                                proDialog.dismiss();

                            }

                        });

                    }

                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                    //while uploading the data
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                //calculating the percentage of data upload
                //(100*how much trancferred)/total
                float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();

                //setting dialog
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

        //temp array list
        ArrayList<DownModel> temp=new ArrayList<>();

        try {

            //for each loop for run through the list array
        for (DownModel obj:list){

            //checking the name with the name from the list
            if (obj.getName().toLowerCase().contains(str.toLowerCase())){

                //if the condition matches copy the data
               temp.add(obj);
           }

        }

        }

        catch (Exception e){

            System.out.println("error"+e);

        }

        //passing the data to update function in adapter
        //and calling it
        adapter.updatelist(temp);

    }

}