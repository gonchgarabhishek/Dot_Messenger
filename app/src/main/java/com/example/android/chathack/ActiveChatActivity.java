package com.example.android.chathack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vamshi on 2/14/2017.
 */

public class ActiveChatActivity extends AppCompatActivity {
ListView msg;
    DatabaseReference mRef;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);
        mRef= FirebaseDatabase.getInstance().getReference().child("Number").child("Message");


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseListAdapter<String> firebaseListAdapter=new FirebaseListAdapter<String>(this,String.class,android.R.layout.simple_list_item_1,mRef) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView Txt=(TextView)v.findViewById(android.R.id.text1);
                Txt.setText(model);
            }
        };
        msg.setAdapter(firebaseListAdapter);
    }


}
