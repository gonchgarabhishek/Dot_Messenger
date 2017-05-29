package com.example.android.chathack;

/**
 * Created by Abhishek on 03-03-2017.
 */

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String URL_REGISTER_DEVICE = "http://192.168.100.6//DOT/RegisterDevice.php";
    private static final String TAG = "MyFirebaseIIDService";
    String email;
    FirebaseAuth mAuth;
    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        //calling the method store token and passing token
     //  sendtoken(refreshedToken);

    }

//    private void sendtoken(final String refreshedToken) {
//        mAuth=  FirebaseAuth.getInstance();
//        if(mAuth.getCurrentUser()!=null) {
//            String m = mAuth.getCurrentUser().getEmail();
//            String[] parts = m.split("@");
//            email = parts[0];
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_DEVICE,
//                new com.android.volley.Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            //Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new com.android.volley.Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        //Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("email", email);
//                params.put("token", refreshedToken);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);}
//    }


}