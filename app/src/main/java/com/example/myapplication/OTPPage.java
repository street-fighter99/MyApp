package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPPage extends AppCompatActivity {


    EditText OtpEditText;
    Button CheckButton;
    private FirebaseAuth firebaseAuth;
    String OTP;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_page);

        OtpEditText=findViewById(R.id.OTPEdit);
        CheckButton=findViewById(R.id.CheckBttn);
        OTP=getIntent().getStringExtra("auth");
        firebaseAuth=FirebaseAuth.getInstance();

        CheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verification_code=OtpEditText.getText().toString();

                if (!verification_code.isEmpty()){
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(OTP,verification_code);
                    signIn(credential);
                }else{
                    Toast.makeText(OTPPage.this, "Please Enter The OTP", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    private void signIn(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    SendToMain();
                }else{
                    Toast.makeText(OTPPage.this, "Verification Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=firebaseAuth.getCurrentUser();
        if(currentUser!=null){
            SendToMain();
        }
    }

    private void SendToMain(){
        startActivity(new Intent(getApplicationContext(),Category.class));
        finish();
}
}