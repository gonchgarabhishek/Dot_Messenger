package com.example.android.chathack;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vamshi on 2/14/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private EditText mPhoneNum;
    private Button mRegister;
    private String mNumber;
    private FirebaseAuth mAuth;
    private String idNumber;
    private String ranPass = "mypassword";
    int REQUEST_CODE =1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_main);
        mPhoneNum = (EditText)findViewById(R.id.phoneNum);
        mNumber = mPhoneNum.getText().toString();
        mRegister = (Button)findViewById(R.id.register);
        mAuth = FirebaseAuth.getInstance();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS},REQUEST_CODE);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mPhoneNum.getText().toString())){
                    if(mPhoneNum.getText().toString().length()==10){

                        registry();

                        // Activate when OTP balance is recharged!
                        /*Intent in=new Intent(LoginActivity.this,otp.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        in.putExtra("phone",mPhoneNum.getText().toString());
                        startActivity(in);*/


                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Please Enter Valid Number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Please Enter Your Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void registry(){

        String mEmail = mPhoneNum.getText().toString() + "@ChatHack.com";
        mAuth.createUserWithEmailAndPassword(mEmail, ranPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    DatabaseReference user;
                    user= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                    user.child("Phone").setValue(mPhoneNum.getText().toString());
                    Toast.makeText(LoginActivity.this, "Reg Succesful", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    finish();
                }
                if(!task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Unable To Register!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}

