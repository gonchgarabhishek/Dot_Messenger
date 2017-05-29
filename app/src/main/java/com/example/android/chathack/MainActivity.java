package com.example.android.chathack;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sinch.android.rtc.SinchClient;

import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends BaseActivity implements SinchService.StartFailedListener {
RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private Call call;
    String token;
    private FirebaseAuth.AuthStateListener mAuthListener;
   // private EditText mEmail;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    SinchClient sinchClient;
    FirebaseRecyclerAdapter<cont, viewholder> firebaseRecyclerAdapter;
    ArrayList<String> l;
   private DatabaseReference mRef1;
    String mynum;
    private DatabaseReference mref2;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final String URL_REGISTER_DEVICE = "https://dot.fwd.wf/DOT/RegisterDevice.php";
   // private Button chatInit;
    FloatingActionButton contact_list;



    @Override
    protected void onStop() {
        super.onStop();

        if(mAuthListener!= null){
            //removing authentication listener when not in use
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {

                getSinchServiceInterface().startClient(mynum);


        }catch (Exception e){e.printStackTrace();}

    }

    @Override
    protected void onStart() {
        super.onStart();


            FirebaseRecyclerAdapter<cont, viewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<cont, viewholder>(
                    cont.class, R.layout.contact_layout, viewholder.class, mRef1


            ) {
                @Override
                protected void populateViewHolder(viewholder viewHolder, final cont model, int position) {
                    final String key=getRef(position).getKey();
                    viewHolder.phone(model.getPhone());
                    viewHolder.msg1(model.getMsg());
                    //Toast.makeText(MainActivity.this, model.getMsg(), Toast.LENGTH_SHORT).show();
                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent in3=new Intent(MainActivity.this,ChatRoom.class);
                            in3.putExtra("Phone",model.getPhone());
                            in3.putExtra("mynum",mynum);
                            startActivity(in3);

                        }
                    });


                }
            };
            recyclerView.setAdapter(firebaseRecyclerAdapter);
        sendtoken();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()==null){

            Intent in=new Intent(MainActivity.this,LoginActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(in);
            finish();
        }
        if(mAuth.getCurrentUser()!=null) {
            Intent g = getIntent();
            mynum = g.getStringExtra("mynum");
            setContentView(R.layout.activity_main);

            token = FirebaseInstanceId.getInstance().getToken();

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        recyclerView=(RecyclerView)findViewById(R.id.cont);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
if(mynum!=null) {
    mRef1 = FirebaseDatabase.getInstance().getReference().child(mynum);
    mRef1.keepSynced(true);

}

        mDatabase = FirebaseDatabase.getInstance();

                contact_list = (FloatingActionButton)findViewById(R.id.contacts_button);

contact_list.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent contacts = new Intent(MainActivity.this, FireBaseActivity.class);
        contacts.putExtra("mynum",mynum);
//        ProgressDialog pd=new ProgressDialog(MainActivity.this);
//        pd.setMessage("Loading....");
//        pd.show();

        startActivity(contacts);
    }
});







    }

    @Override
    public void onStartFailed(SinchError error) {
Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStarted() {
        Toast.makeText(MainActivity.this,"Statred",Toast.LENGTH_SHORT).show();
    }

    public static class viewholder extends RecyclerView.ViewHolder{

        View view;


        public viewholder(View itemView) {
            super(itemView);
            view=itemView;
        }

        public void phone(String phone){
            TextView t=(TextView)view.findViewById(R.id.contact_name);
            t.setText(phone);



        }
        public void msg1(String msg1){
            TextView t1=(TextView)view.findViewById(R.id.latest_message);

            t1.setText(msg1);



        }




    }
    private void sendtoken() {
        String m = mAuth.getCurrentUser().getEmail();
        String[] parts = m.split("@");
       final String email = parts[0];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_DEVICE,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", mynum);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    protected void onServiceConnected() {

        getSinchServiceInterface().setStartListener(this);
    }


}
