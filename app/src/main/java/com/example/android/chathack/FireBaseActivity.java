package com.example.android.chathack;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class FireBaseActivity extends AppCompatActivity {
    private ListView lstNames;
    String phone = "";
    DatabaseReference mref2;
    String name;

    ArrayList l=new ArrayList();
    DatabaseReference  mref1;
String s;
    String p1;
  DatabaseReference mref;

    List b=new ArrayList();
    FirebaseAuth mauth;
    List<String> contacts;
    List p;
    List<String> w=new ArrayList<String>();
    DatabaseHandler db;

    String mynum;

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent g=getIntent();
        mynum=g.getStringExtra("mynum");
        db = new DatabaseHandler(this);
        final List<Contact> contacts1 = db.getAllContacts();
        setContentView(R.layout.contactlistview);
        mauth = FirebaseAuth.getInstance();

        SQLiteDatabase s1=db.getWritableDatabase();
       Cursor todoCursor = s1.rawQuery("SELECT * FROM contacts", null);

        mref1=FirebaseDatabase.getInstance().getReference().child("data");
        lstNames = (ListView) findViewById(R.id.lstNames);
        cursor todoAdapter = new cursor(this, todoCursor);
lstNames.setAdapter(todoAdapter);
        lstNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String m=contacts1.get(position).getName().toString();

             //  Toast.makeText(FireBaseActivity.this,m,Toast.LENGTH_SHORT).show();
                Intent in=new Intent(FireBaseActivity.this,ChatRoom.class);
                in.putExtra("Phone",m);
                in.putExtra("mynum",mynum);
                startActivity(in);
            }
        });


        for (Contact cn : contacts1) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " +
                    cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
        mref2 = FirebaseDatabase.getInstance().getReference().child("users").child(mauth.getCurrentUser().getUid()).child("Phone");

        mref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                mynum = dataSnapshot.getValue().toString();
  //              mynum.trim();

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
        Set<String> hs = new HashSet<>();
        hs.addAll(w);
        w.clear();
        w.addAll(hs);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final String i : contacts) {

                    try {
                        if (dataSnapshot.hasChild(i)) {
                            mref.keepSynced(true);
                            DatabaseReference mref3 = mref.child(i);
                            mref3.keepSynced(true);

                            mref3.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, String> map = (Map) dataSnapshot.getValue();
                                    s = map.get("name");
                                    db.addContact(new Contact(i, s));


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });

                        } else {
                            db.addContact(new Contact(i, "invite"));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();




    }


    private void showContacts() throws ExecutionException, InterruptedException {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
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
            pd =new ProgressDialog(FireBaseActivity.this);
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
                        phone.replace(" ","");
                              if(phone.length()==10){
                                  phone="+91"+phone;
                                  phone.trim();
                              }

            if(phone.length()==13) {

                if (name != null) {
                       phone.trim();
                    phone.replace(" ","");

                    contacts.add(phone);

                } else {
                    phone.trim();
                    phone.replace(" ","");
                    contacts.add(phone);
                }
            }
                        Collections.sort(contacts);
                    }
                } while (cursor.moveToNext());
            }
            // Close the curosor
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
