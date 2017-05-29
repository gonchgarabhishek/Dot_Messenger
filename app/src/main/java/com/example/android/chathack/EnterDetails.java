package com.example.android.chathack;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vamshi on 2/14/2017.
 */

public class EnterDetails extends AppCompatActivity {

    private EditText user_name;
    private CircleImageView user_photo;
    DatabaseReference mref;
    Button set;
    Uri image;
    FirebaseAuth mauth;
    StorageReference msto;
    String mynum;
    private static final int code =1;
    private DatabaseReference mRef;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_details);
        mauth=FirebaseAuth.getInstance();
        msto= FirebaseStorage.getInstance().getReference();
Intent in=getIntent();
       mynum= in.getStringExtra("mynum");
        user_name = (EditText)findViewById(R.id.user_name);
        user_photo = (CircleImageView)findViewById(R.id.user_image);
        set=(Button)findViewById(R.id.Setup);
//        String name = user_name.getText().toString().trim();
      mref = FirebaseDatabase.getInstance().getReference();
      user_photo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent in = new Intent(Intent.ACTION_GET_CONTENT);
        in.setType("image/=");
        startActivityForResult(in,1);
    }
});
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mref.child("data").child("name").setValue(user_name.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                           StorageReference file=msto.child(mauth.getCurrentUser().getUid());
file.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
if(task.isSuccessful()){
    Intent in = new Intent(EnterDetails.this,MainActivity.class);
    in.putExtra("mynum",mynum);
    startActivity(in);
    finish();

}
    }
});
                        }
                    }
                });
                Intent in = new Intent(EnterDetails.this,MainActivity.class);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==code && resultCode==RESULT_OK){
             image=data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                // Log.d(TAG, String.valueOf(bitmap));


                user_photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
