package com.example.android.chathack;



/**
 * Created by Abhishek on 24-02-2017.
 */

public class Contact {
    int id;
    String name;
    String phone;
    public Contact(){   }
    public Contact(int id, String name, String _phone_number){
        this.id = id;
        this.name = name;
        this.phone = _phone_number;
    }

    public Contact(String name, String _phone_number){
        this.name = name;
        this.phone = _phone_number;
    }
    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPhoneNumber(){
        return this.phone;
    }

    public void setPhoneNumber(String phone_number){
        this.phone = phone_number;
    }
}
