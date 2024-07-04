package com.example.e_commerce.Model;

import java.io.Serializable;

public class Cart implements Serializable {
    private int id, book_id, book_type_id, user_id, quantity, price;
    private String name, image;
    public Cart(){

    }
    public Cart(int id, int book_id, int book_type_id, int user_id, int quantity, String name, int price, String image) {
        this.id = id;
        this.book_id = book_id;
        this.book_type_id = book_type_id;
        this.user_id = user_id;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Cart(int book_id, int book_type_id, int user_id, int quantity, String name, int price, String image) {
        this.book_id = book_id;
        this.book_type_id = book_type_id;
        this.user_id = user_id;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Cart(int bookTypeId, int userId, int amount, String title, int i, String imageUrl) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getBook_type_id() {
        return book_type_id;
    }

    public void setBook_type_id(int book_type_id) {
        this.book_type_id = book_type_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
