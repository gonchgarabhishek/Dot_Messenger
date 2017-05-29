package com.example.android.chathack;

import android.app.Application;
import android.content.Context;
import android.util.Base64;

import com.google.firebase.database.FirebaseDatabase;
import android.util.Base64;
import android.util.Log;import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Created by vamshi on 23/2/17.
 */

public class ChatHack extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
