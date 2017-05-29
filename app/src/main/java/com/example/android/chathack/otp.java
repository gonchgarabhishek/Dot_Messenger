package com.example.android.chathack;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class otp extends AppCompatActivity implements VerificationListener{
    String mynum;
    EditText e;
    Verification mve;
    final SmsManager s=SmsManager.getDefault();
    String idNumber;
    String ranPass;
    Button verify;
        String token;
    FirebaseAuth mAuth;
    private static final String URL_REGISTER_DEVICE = "http://192.168.100.6//DOT/RegisterDevice.php";
    ProgressDialog pd;
    int REQUEST_CODE =1;

BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_confirmation);
        pd =new ProgressDialog(this);
        token= FirebaseInstanceId.getInstance().getToken();
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_SMS},REQUEST_CODE);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},REQUEST_CODE);




        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED){

// permission is already granted
// here you can directly access contacts

        }else{

//persmission is not granted yet
//Asking for permission


        }
        Intent in =getIntent();
        mAuth=FirebaseAuth.getInstance();
       mynum= in.getStringExtra("phone");
        e=(EditText)findViewById(R.id.otp_textBox);
        verify=(Button)findViewById(R.id.verify);

        idNumber = mynum + "@mychat.com";
        ranPass = "myPassword";

         mve= SendOtpVerification.createSmsVerification(this,mynum,this,"91");
        Toast.makeText(otp.this,mynum,Toast.LENGTH_SHORT).show();

          receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase("otp")) {
                    final String message = intent.getStringExtra("message");
                    //Do whatever you want with the code here
                    e.setText(message);
                }
            }
        };

        mve.initiate();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mve.verify(e.getText().toString());
            }
        });


    }

    @Override
    public void onInitiated(String response) {
        Toast.makeText(otp.this,response,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitiationFailed(Exception paramException) {
        Toast.makeText(otp.this,paramException.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVerified(String response) {
        Toast.makeText(otp.this,response,Toast.LENGTH_SHORT).show();
        registry();

    }

    @Override
    public void onVerificationFailed(Exception paramException) {

    }
    public void registry(){

        pd.setMessage("loading...");
        pd.show();
        mAuth.createUserWithEmailAndPassword(idNumber, ranPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    DatabaseReference user;
                    user= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                    user.child("Phone").setValue("+91"+mynum);
                    Toast.makeText(otp.this, "Reg Succesful", Toast.LENGTH_SHORT).show();
                    final DatabaseReference mref3;
                    mref3= FirebaseDatabase.getInstance().getReference().child("messages").child("+91"+mynum).child("DOT").push().child("msg");
                    final DatabaseReference mref5= FirebaseDatabase.getInstance().getReference().child("+91"+mynum).child("DOT");
                    mref5.child("msg").setValue("Hi").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mref5.child("phone").setValue("DOT").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            mref3.setValue("Hi, Welcome to DOT Messenger ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        pd.dismiss();
                                                        Intent in = new Intent(otp.this, EnterDetails.class);
                                                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        in.putExtra("mynum","+91"+mynum);
                                                        startActivity(in);

                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });



                }
                else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    mAuth.signInWithEmailAndPassword(idNumber,ranPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                              //  sendtoken();
                                DatabaseReference user;
                                user= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                                user.child("Phone").setValue("+91"+mynum);
                                Toast.makeText(otp.this, "Reg Succesful", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(otp.this, MainActivity.class);
                                in.putExtra("mynum","+91"+mynum);
                                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(in);
                                pd.dismiss();


                            }
                        }
                    });
                }
            }
        });
    }


}

