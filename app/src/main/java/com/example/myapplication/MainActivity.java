package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private  Button submitButtn;
    private EditText PhoneNumberEdit;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submitButtn=findViewById(R.id.submitButton);
        PhoneNumberEdit=findViewById(R.id.phoneNumberEditText);
        mAuth=FirebaseAuth.getInstance();

        submitButtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber=PhoneNumberEdit.getText().toString();

                if (PhoneNumberEdit.getText().toString().isEmpty()){
                    PhoneNumberEdit.setError("ഡേയ്  എനാ തെപ്പടെ ");
                    PhoneNumberEdit.requestFocus();
                }else if (PhoneNumberEdit.getText().toString().length()!=10){

                    PhoneNumberEdit.setError("അറിയോങ്കിൽ നമ്പർ അടി ഇല്ലേൽ എന്നീട്പോ ");
                    PhoneNumberEdit.requestFocus();
                }
                else{
                    PhoneAuthOptions options=PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber("+91"+phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(mCallBacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });

        mCallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {


                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);


                //Some time the code didn't detected automatically
                //so user has to manually enter the code

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(getApplicationContext(),OTPPage.class);
                        intent.putExtra("auth",s);
                        startActivity(intent);
                    }
                },10000);


                Toast.makeText(MainActivity.this, "OTP Has Been Sent..", Toast.LENGTH_SHORT).show();




            }
        };




    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            SendToMain();

        }
    }
    private void SendToMain(){
        Intent intent=new Intent(getApplicationContext(),Category.class);
        startActivity(intent);
        finish();

    }
    private void signIn(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    SendToMain();
                }else{
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}