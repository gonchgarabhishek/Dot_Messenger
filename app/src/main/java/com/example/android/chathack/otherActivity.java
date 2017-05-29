package com.example.android.chathack;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class otherActivity extends AppCompatActivity {
  ListView lstNames;
    String phone = "";
    DatabaseReference mref2;
    String name;
    ArrayList l=new ArrayList();

    DatabaseReference  mref1;

String s;
    String p1;
  DatabaseReference mref;

    ArrayList b=new ArrayList();
    FirebaseAuth mauth;
    ArrayList<String> q=new ArrayList<>();
    List<String> contacts;
    List p;

    String mynum;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.contactlistview);
        mauth = FirebaseAuth.getInstance();

mref1=FirebaseDatabase.getInstance().getReference().child("data");
        lstNames = (ListView) findViewById(R.id.lstNames);

        mref2 = FirebaseDatabase.getInstance().getReference().child("users").child(mauth.getCurrentUser().getUid()).child("Phone");

        mref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mynum = dataSnapshot.getValue().toString();
                mynum.trim();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mref2.keepSynced(true);
        // Read and show the contacts


        try {
            showContacts();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (int i = 0; i<contacts.size(); i++) {
                    try{
                        if (dataSnapshot.hasChild(contacts.get(i))) {
                            mref.keepSynced(true);
                            DatabaseReference mref3 = mref.child(contacts.get(i));
                            mref3.keepSynced(true);

                            final int finalI = i;
                            mref3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String,String> map=(Map)dataSnapshot.getValue();
                                    try {
                                        s = map.get("name");
                                        int d = finalI;
                                        p1 = map.get("phone");
                                    }catch (Exception e){e.printStackTrace();}


                                    q.add(s);




                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        lstNames.setAdapter(new base(otherActivity.this,contacts,q));
        lstNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in=new Intent(otherActivity.this,ChatRoom.class);
                   in.putExtra("Phone",contacts.get(position));
                in.putExtra("mynum",mynum);
              startActivity(in);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();






    }


    private void showContacts() throws ExecutionException, InterruptedException {
        // Check the SDK version and whether the permission is already granted or not.

            // Android version is lesser than 6.0 or the permission is already granted.
            ascyn p=new ascyn();
             p.execute();
              contacts=p.get();
            Set<String> hs = new HashSet<>();
            hs.addAll(contacts);
            contacts.clear();
            contacts.addAll(hs);
             mref=FirebaseDatabase.getInstance().getReference();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                try {
                    showContacts();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {

                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Read the name of all the contacts.
     *
     * @return a list of names.
     *
     *
     */

    public  class ascyn extends AsyncTask<Void,Void,List>{
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            pd =new ProgressDialog(otherActivity.this);
            pd.setMessage("Loading....");
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            contacts = new ArrayList<>();
            p = new ArrayList();
            // Get the ContentResolver
            ContentResolver cr = getContentResolver();
            // Get the Cursor of all the contacts
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            // Move the cursor to first. Also check whether the cursor is empty or not.
            if (cursor.moveToFirst()) {
                // Iterate through the cursor
                do {
                    // Get the contacts name
                    String id = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    if (Integer.parseInt(cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        if (pCur.moveToFirst()) {
                            do {

                                // Do something with phones
                                phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                p.add(phone);
                                Collections.sort(p);

                                if (phone != null) {
                                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                                }
                            } while (pCur.moveToNext());
                        }
                        pCur.close();
                    }
                    if (phone != null) {
                              if(phone.length()==10){
                                  phone="+91"+phone;
                                  phone.trim();
                              }
if(phone.length()==13){
    DatabaseReference d=mref1;





                        contacts.add(phone);}
                        Collections.sort(contacts);
                    }
                } while (cursor.moveToNext());
            }

            cursor.close();

            return contacts;
        }

        @Override
        protected void onPostExecute(List list) {

            super.onPostExecute(list);
            pd.dismiss();


        }
    }


}
