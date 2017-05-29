package com.example.android.chathack;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Abhishek on 26-02-2017.
 */

public class base extends BaseAdapter {
    List cont3;
    List status2;
    Context d;

    base(Context cs, List cont, List satus){
        cont3=cont;
        status2=satus;
        d=cs;


    }


    @Override
    public int getCount() {
        return cont3.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder vh;
        if (convertView == null) {
//            Set<String> hs = new HashSet<>();
//            hs.addAll(hs);
//            status2.clear();
//            status2.addAll(hs);
            v = View.inflate(d, R.layout.cont, null);
            vh = new ViewHolder();
            vh.text1 = (TextView) v.findViewById(R.id.contact_name);
            vh.text2 = (TextView) v.findViewById(R.id.latest_message);




            v.setTag(vh);

        } else {


            vh = (ViewHolder) v.getTag();
        }
        try {
            vh.text1.setText(cont3.get(position).toString());
            if(!TextUtils.isEmpty(status2.get(position).toString())){
                Toast.makeText(d,position+" "+status2.get(position).toString(),Toast.LENGTH_SHORT).show();
            vh.text2.setText(status2.get(position).toString());}
            else{Toast.makeText(d,position+" "+status2.get(position).toString(),Toast.LENGTH_SHORT).show();}


        } catch (NullPointerException | IndexOutOfBoundsException e) {

            e.printStackTrace();
        }


        return v;





    }
    static class ViewHolder {
        TextView text1;
        TextView text2;


    }
}
