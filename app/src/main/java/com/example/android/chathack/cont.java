package com.example.android.chathack;

/**
 * Created by vamshi on 23/2/17.
 */

public class cont {
    private  String phone;
    private  String msg1;

    public cont(){


    }

    public cont(String phone1, String msg1) {
        this.phone = phone1;
        this.msg1 = msg1;
    }

    public String getPhone() {
        return phone;
    }

    public String getMsg() {
        return msg1;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMsg(String msg) {
        this.msg1 = msg;
    }




}
