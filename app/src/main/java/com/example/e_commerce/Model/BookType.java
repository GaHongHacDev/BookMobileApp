package com.example.e_commerce.Model;

import java.io.Serializable;
import java.util.Date;

public class BookType implements Serializable {

    private int id, quantity;
    private String type_name;
    private String image_url;
    private static BookType bookType = null;

    public BookType(){}
    public BookType(int id, String type_name, String image_url) {
        this.id = id;
        this.type_name = type_name;
        this.image_url = image_url;
        this.quantity = 0;
    }

    public BookType(String type_name, String image_url) {
        this.type_name = type_name;
        this.image_url = image_url;
        this.quantity = 0;
    }

    public BookType(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public static BookType getInstance(){
        if(bookType == null)
            bookType = new BookType();
        return bookType;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
