package com.example.e_commerce.Model;

import java.io.Serializable;

public class Book implements Serializable {

    private int id;
    private int stock_quantity;
    private int book_type_id;
    private String title;
    private String author;
    private String image_url;
    private String description;
    private int price;
    private int status;

    public Book(int id, int stock_quantity, int book_type_id, String title, String author, String image_url, String description, int price, int status) {
        this.id = id;
        this.stock_quantity = stock_quantity;
        this.book_type_id = book_type_id;
        this.title = title;
        this.author = author;
        this.image_url = image_url;
        this.description = description;
        this.price = price;
        this.status = status;
    }

    public Book(int stock_quantity, int book_type_id, String title, String author
            , String image_url, String description, int price) {
        this.stock_quantity = stock_quantity;
        this.book_type_id = book_type_id;
        this.title = title;
        this.author = author;
        this.image_url = image_url;
        this.description = description;
        this.price = price;
    }

    public Book() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public int getBook_type_id() {
        return book_type_id;
    }

    public void setBook_type_id(int book_type_id) {
        this.book_type_id = book_type_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Book(int stock_quantity, String title) {
        this.stock_quantity = stock_quantity;
        this.title = title;
    }




}
