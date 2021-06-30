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

    //Variables for buttons and edit text
    private  Button submitButtn;
    private EditText PhoneNumberEdit;

    //variables for firebase authentication
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connecting the variables with the ID
        submitButtn=findViewById(R.id.submitButton);
        PhoneNumberEdit=findViewById(R.id.phoneNumberEditText);

        //getting the firebase authentication
        mAuth=FirebaseAuth.getInstance();

        //adding action to the submit button
        submitButtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                //getting the phone number in string from the edit text
                String phoneNumber=PhoneNumberEdit.getText().toString();

                //if you don't write anything on pone number
                if (PhoneNumberEdit.getText().toString().isEmpty())
                {
                    //setting error
                    PhoneNumberEdit.setError("Phone number is EMPTY ");
                    PhoneNumberEdit.requestFocus();

                }
                //if the phone number length didn't match with indian std
                else if (PhoneNumberEdit.getText().toString().length()!=10)
                {
                    //setting the error
                    PhoneNumberEdit.setError("The number you entered didn't have 10 digit ");
                    PhoneNumberEdit.requestFocus();
                }
                else
                    {

                        //if those two are ok then this happen
                        //phone authentication in authentication firebase
                        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(mAuth)
                                //giving the phone number to send otp
                                .setPhoneNumber("+91"+phoneNumber)
                                //setting the timer
                                .setTimeout(60L, TimeUnit.SECONDS)
                                //setting activity
                                .setActivity(MainActivity.this)
                                //callback function to know we get the response
                                .setCallbacks(mCallBacks)
                                //build it
                                .build();

                        //passing the object of phone auth options
                        PhoneAuthProvider.verifyPhoneNumber(options);

                }

            }

        });


        //when you get the response from the  firebase
        mCallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            //when it verified
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                //calling the function signIn
                signIn(phoneAuthCredential);

            }

            //when it failed
            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                //toast to now the error
                Toast.makeText(MainActivity.this, "error >>>"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);


                //Some time the code didn't detected automatically
                //so user has to manually enter the code
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        //Intent to next page
                        Intent intent=new Intent(getApplicationContext(),OTPPage.class);

                        //passing the data
                        intent.putExtra("auth",s);

                        //start the intent
                        startActivity(intent);

                    }

                    //waiting time set
                },10000);

                Toast.makeText(MainActivity.this, "OTP Has Been Sent..", Toast.LENGTH_SHORT).show();

            }

        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        //checing the current user
        FirebaseUser user=mAuth.getCurrentUser();

        //if there is a user
        if(user!=null){

            //then call the function
            SendToMain();

        }
    }


    private void SendToMain(){

        //this function is mainly for going to the categorypage
        Intent intent=new Intent(getApplicationContext(),Category.class);
        startActivity(intent);

        //by using this keyword every thing in this page is finished
        finish();

    }


    private void signIn(PhoneAuthCredential credential){

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    SendToMain();

                }
                else{

                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}