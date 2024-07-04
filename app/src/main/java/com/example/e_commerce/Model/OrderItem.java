package com.example.e_commerce.Model;

import java.util.ArrayList;
import java.util.Date;

public class OrderItem {
    private int id, book_id, book_type_id, user_id, order_id, quantity, price;
    private String name, image, status;
    public OrderItem(){

    }

    public OrderItem(int id, int book_id, int book_type_id, int user_id, int order_id, int quantity, int price, String name, String image, String status) {
        this.id = id;
        this.book_id = book_id;
        this.book_type_id = book_type_id;
        this.user_id = user_id;
        this.order_id = order_id;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.image = image;
        this.status = status;
    }

    public OrderItem(int book_id, int book_type_id, int user_id, int order_id, int quantity, int price, String name, String image, String status) {
        this.book_id = book_id;
        this.book_type_id = book_type_id;
        this.user_id = user_id;
        this.order_id = order_id;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.image = image;
        this.status = status;
    }

    public OrderItem( String status) {
        this.status = status;
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

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //    private int id;
//    private int order_id;
//    private int book_id;
//    private int quantity;
//    private int price;
//    private Date created_at;
//    private int status;
//
//    ArrayList<Book> products;
//
//    public OrderItem() {
//    }
//
//    public OrderItem(int id, int order_id, int book_id, int quantity
//            , int price, Date created_at, int status, ArrayList<Book> products) {
//        this.id = id;
//        this.order_id = order_id;
//        this.book_id = book_id;
//        this.quantity = quantity;
//        this.price = price;
//        this.created_at = created_at;
//        this.status = status;
//        this.products = products;
//    }
//
//    public OrderItem(int order_id, int book_id, int quantity, int price
//            , Date created_at, int status, ArrayList<Book> products) {
//        this.order_id = order_id;
//        this.book_id = book_id;
//        this.quantity = quantity;
//        this.price = price;
//        this.created_at = created_at;
//        this.status = status;
//        this.products = products;
//    }
//
//    public OrderItem(int order_id) {
//        this.order_id = order_id;
////        this.product_name = new ArrayList<String>();
////        this.product_quantity = new ArrayList<Integer>();
//        this.products = new ArrayList<Book>();
//    }
//
//    public int getOrder_id() {
//        return order_id;
//    }
//
//    public void setOrder_id(int order_id) {
//        this.order_id = order_id;
//    }
//
//
//    public ArrayList<Book> getProducts() {
//        return products;
//    }
//
//    public void setProducts(ArrayList<Book> products) {
//        this.products = products;
//    }
}
