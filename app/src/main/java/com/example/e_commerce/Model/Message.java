package com.example.e_commerce.Model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private int id;
    private String content;
    private boolean isShop;
    private int user_id;
    private Date created_at;

    private int status;

    public Message(int id, String content, boolean isShop, int user_id
            , Date created_at, int status) {
        this.id = id;
        this.content = content;
        this.isShop = isShop;
        this.user_id = user_id;
        this.created_at = created_at;
        this.status = status;
    }

    public Message(String content, boolean isShop, int user_id, Date created_at, int status) {
        this.content = content;
        this.isShop = isShop;
        this.user_id = user_id;
        this.created_at = created_at;
        this.status = status;
    }

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isShop() {
        return isShop;
    }

    public void setShop(boolean shop) {
        isShop = shop;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
