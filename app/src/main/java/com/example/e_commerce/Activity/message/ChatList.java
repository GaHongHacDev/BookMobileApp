package com.example.e_commerce.Activity.message;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatList {

    private String mobile, name, message, date, time;

    public ChatList(String mobile, String name, String message, String date, String time) {
        this.mobile = mobile;
        this.name = name;
        this.message = message;

        Date currentDate = new Date();

        // Định dạng ngày
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(currentDate);

        // Định dạng thời gian
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        time = timeFormat.format(currentDate);
        this.date = date;
        this.time = time;
        //this.date = "";
        //this.time = "";
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
