package com.example.android.chathack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class launcher extends AppCompatActivity {
FirebaseAuth mAuth;
    String mynum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_launcher);
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null) {
            Intent in = new Intent(launcher.this, LoginActivity.class);
            startActivity(in);}
        if(mAuth.getCurrentUser()!=null) {
            String m = mAuth.getCurrentUser().getEmail();
            String[] parts = m.split("@");
            mynum = parts[0];
            Toast.makeText(this, mynum, Toast.LENGTH_SHORT).show();
            if(!TextUtils.isEmpty(mynum)) {
                Intent in = new Intent(launcher.this, MainActivity.class);
                in.putExtra("mynum","+91"+ mynum);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
            }
        }
    }


}
