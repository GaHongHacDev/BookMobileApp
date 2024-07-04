package com.example.e_commerce.Model;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

    private int id, user_id, total_amount;
    private String address, feedback, payment_method;
    private double rate;
    private Date created_at, date;
    private String status;
    public Order() {
    }
    public Order(String status) {
        this.status = status;
    }
    public Order(int id) {
        this.id = id;
    }
    public Order(int id, int user_id, int total_amount, String payment_method
            , Date created_at, String status) {
        this.id = id;
        this.user_id = user_id;
        this.total_amount = total_amount;
        this.payment_method = payment_method;
        this.created_at = created_at;
        this.status = status;
    }

    public Order(int id, int user_id, int total_amount, String address, String feedback, String payment_method, double rate, Date created_at, Date date, String status) {
        this.id = id;
        this.user_id = user_id;
        this.total_amount = total_amount;
        this.address = address;
        this.feedback = feedback;
        this.payment_method = payment_method;
        this.rate = rate;
        this.created_at = created_at;
        this.date = date;
        this.status = status;
    }

    public Order(int user_id, int total_amount, String address, String feedback, String payment_method, double rate, Date created_at, String status) {
        this.user_id = user_id;
        this.total_amount = total_amount;
        this.address = address;
        this.feedback = feedback;
        this.payment_method = payment_method;
        this.rate = rate;
        this.created_at = created_at;
        this.status = status;
    }

    public Order(int user_id, Date date, String address, String feedback, double rate) {
        this.user_id = user_id;
        this.date = date;
        this.address = address;
        this.feedback = feedback;
        this.rate = rate;
    }

    public Order(int order_id, int user_id,  Date date, String address, String feedback, double rate) {
        this.id = order_id;
        this.user_id = user_id;
        this.date = date;
        this.address = address;
        this.feedback = feedback;
        this.rate = rate;
    }

    public int getOrder_id() {
        return id;
    }

    public void setOrder_id(int order_id) {
        this.id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public  Date getDate() {
        return date;
    }

    public void setDate( Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }
}
