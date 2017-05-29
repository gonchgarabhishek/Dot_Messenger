package com.example.android.chathack;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Abhishek on 27-02-2017.
 */


public class cursor extends CursorAdapter {
    Cursor c1;
    public cursor(Context context, Cursor c) {

        super(context, c);
        c1=c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return  LayoutInflater.from(context).inflate(R.layout.cont, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        c1=cursor;
        TextView name = (TextView) view.findViewById(R.id.contact_name);
        TextView phone = (TextView) view.findViewById(R.id.latest_message);
        String na = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String ph = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
name.setText(na);
        phone.setText(ph);
    }
}
