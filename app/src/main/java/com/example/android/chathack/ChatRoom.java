package com.example.android.chathack;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.MissingPermissionException;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.icu.text.Normalizer.NO;

public class ChatRoom extends BaseActivity {
String phone;
    DatabaseReference mref;
    DatabaseReference mref2;
    String mynum;
    EditText msg2;
    private Call call;
    TextClock time;
    private static final String URL = "https://dot.fwd.wf/DOT/sendSinglePush.php";
    FirebaseRecyclerAdapter<message, viewholder> firebaseRecyclerAdapter;
    int a;
    FirebaseAuth mauth;
    DatabaseReference mref3;
    DatabaseReference mref4;
    Random r;
    String s ="123";
    RecyclerView msg;
private ImageView mSend;
    String rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in=getIntent();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},1);

        phone=in.getStringExtra("Phone");
        mynum=in.getStringExtra("mynum");
        Toast.makeText(ChatRoom.this,phone,Toast.LENGTH_SHORT).show();
        setContentView(R.layout.chatlayout);




        msg2=(EditText)findViewById(R.id.enter_text);

        r=new Random();
        a=r.nextInt(2566);
        rand=Integer.toString(a);
        mauth=FirebaseAuth.getInstance();
        rand=Integer.toString(a);
        mSend = (ImageView)findViewById(R.id.send);
        msg = (RecyclerView)findViewById(R.id.msg_List);
        msg.setHasFixedSize(true);
        msg.setLayoutManager(new LinearLayoutManager(this));

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mref3= FirebaseDatabase.getInstance().getReference().child("messages").child(mynum).child(phone).push().child("msg");
                DatabaseReference mref5= FirebaseDatabase.getInstance().getReference().child(mynum).child(phone);
                mref5.child("msg").setValue(msg2.getText().toString());
                mref5.child("phone").setValue(phone);

                mref3.setValue(msg2.getText().toString());
                mref4= FirebaseDatabase.getInstance().getReference().child("messages").child(phone).child(mynum).push().child("msg");
                DatabaseReference mref6= FirebaseDatabase.getInstance().getReference().child(phone).child(mynum);
                mref6.child("msg").setValue(msg2.getText().toString());
                mref6.child("phone").setValue(mynum);
                sendnoti(msg2.getText().toString());
                mref4.setValue(msg2.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            a=r.nextInt(2566);

                            rand=Integer.toString(a);
                        msg2.setText("");}
                        else{

                            Toast.makeText(ChatRoom.this,"Problem brooo",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        try {
            mref = FirebaseDatabase.getInstance().getReference().child("messages").child(mynum).child(phone);
            mref.keepSynced(true);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override

    protected void onStart() {
        super.onStart();

        try {
            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<message, viewholder>(
                    message.class, R.layout.messagelayout, viewholder.class, mref

            ) {
                @Override
                protected void populateViewHolder(viewholder viewHolder, final message model, int position) {
                    viewHolder.msg(model.getMsg());



                }
            };
                 msg.setAdapter(firebaseRecyclerAdapter);

            firebaseRecyclerAdapter.notifyDataSetChanged();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class viewholder extends RecyclerView.ViewHolder{

View view;
        public viewholder(View itemView) {
            super(itemView);
           view=itemView;
        }

        public void msg(String msg){
            TextView t=(TextView)view.findViewById(R.id.msg);
            t.setText(msg);


        }




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int a=item.getItemId();
        if(a==R.id.options){
            try {

                Call call = getSinchServiceInterface().callUser(phone);
                if (call == null) {
                    // Service failed for some reason, show a Toast and abort
                    Toast.makeText(this, "Service is not started. Try stopping the service and starting it again before "
                            + "placing a call.", Toast.LENGTH_LONG).show();

                }
                String callId = call.getCallId();
                Intent callScreen = new Intent(this, CallScreenActivity.class);
                callScreen.putExtra(SinchService.CALL_ID, callId);
                startActivity(callScreen);
            } catch (MissingPermissionException e) {
                ActivityCompat.requestPermissions(this, new String[]{e.getRequiredPermission()}, 0);
            }

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;

    }
    public void sendnoti(final String msg){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Toast.makeText(ChatRoom.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatRoom.this, error.toString(), Toast.LENGTH_LONG).show();
                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", "Dot");
                params.put("message", msg);



                params.put("email", phone);
                return params;
            }
        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);

        


    }

}
