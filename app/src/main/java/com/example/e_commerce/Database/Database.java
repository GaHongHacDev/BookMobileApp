package com.example.e_commerce.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.e_commerce.Model.Book;
import com.example.e_commerce.Model.Cart;
import com.example.e_commerce.Model.Category;
import com.example.e_commerce.Model.Order;
import com.example.e_commerce.Model.OrderItem;
import com.example.e_commerce.Model.User;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    final static String dataName = "DataBase";
    SQLiteDatabase database;

    public Database(@Nullable Context context) {
        super(context, dataName, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table  user (id integer primary key  autoincrement , " +
                "fullname text not null, " +
                "email text not null," +
                "password text not null, " +
                "phone_number text not null, " +
                "username text not null)");

        db.execSQL("create table category (id integer primary key  autoincrement , " +
                "name text not null," +
                "image text not null)");

        db.execSQL("create table product (id integer primary key autoincrement, " +
                "title text not null ," +
                "author text not null ," +
                "description text not null ," +
                "image_url text ," +
                "price real not null , " +
                "stock_quantity integer not null , " +
                "book_type_id integer not null ," +
                "foreign key (cate_id)references category (id))");

        db.execSQL("create table cart (product_id integer primary key, " +
                "user_id integer, " +
                "product_name text not null ," +
                "product_image text ," +
                "product_price real not null , " +
                "product_quantity integer not null , " +
                "product_cat_id integer not null ," +
                "foreign key (product_cat_id)references category (id)," +
                "foreign key (product_id)references product (id)," +
                "foreign key (user_id)references user (id))");

        db.execSQL("create table orders (id integer primary key autoincrement, " +
                "date text not null ," +
                "user_id integer not null ," +
                "address text not null , " +
                "feedback text not null , " +
                "rate real," +
                "foreign key (user_id)references user (id))");

        db.execSQL("create table order_details (order_id integer not null, " +
                "product_id integer not null," +
                "product_name text," +
                "quantity integer not null ," +
                "foreign key (order_id)references orders (id), " +
                "foreign key (product_id)references product (id)," +
                "PRIMARY KEY (order_id, product_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists category");
        db.execSQL("drop table if exists product");
        db.execSQL("drop table if exists orders");
        db.execSQL("drop table if exists cart");
        db.execSQL("drop table if exists order_details");
        onCreate(db);
    }

    public void insert_user(User user) {

        database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullname", user.getFullname());
        values.put("email", user.getEmail());
        values.put("phone_number", user.getPhone_number());
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());

        database.insert("user", null, values);
        database.close();
    }

    public boolean checkEmailExistence(String email) {
        database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM user WHERE email=?", new String[]{email});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


    public Cursor user_login(String name, String pass) {
        database = getReadableDatabase();
        String[] args = {name, pass};
        Cursor cursor = database.rawQuery("select * from user where name =? and  password =? ", args);

        if (cursor != null)
            cursor.moveToFirst();

        database.close();
        return cursor;
    }

    public void some_category() {
        Category category = new Category();
        category.setImage("https://m.media-amazon.com/images/W/WEBP_402378-T1/images/I/41n7TZpQuvL._AC_UY327_FMwebp_QL65_.jpg");
        category.setName("PlayStation");
        add_category(category);

        category.setImage("https://m.media-amazon.com/images/W/WEBP_402378-T1/images/I/71FHsBXbiVL._AC_UL480_FMwebp_QL65_.jpg");
        category.setName("PC");
        add_category(category);

        category.setImage("https://m.media-amazon.com/images/W/WEBP_402378-T1/images/I/411vPjx7yWL._AC_UL480_FMwebp_QL65_.jpg");
        category.setName("Clothes ");
        add_category(category);
    }

    public void some_product() {
        Book product = new Book();
        product.setImage_url("https://m.media-amazon.com/images/W/WEBP_402378-T1/images/I/51tbWVPtckL._AC_UY327_FMwebp_QL65_.jpg");
        product.setTitle("Sony PlayStation");
        product.setPrice(4000);
        product.setStock_quantity(2);
        product.setBook_type_id(1);
        add_product(product);

        product = new Book();
        product.setImage_url("https://m.media-amazon.com/images/W/WEBP_402378-T1/images/I/71rbwmEdSSL._AC_UY327_FMwebp_QL65_.jpg");
        product.setTitle("Lenovo laptop");
        product.setPrice(15000);
        product.setStock_quantity(5);
        product.setBook_type_id(2);
        add_product(product);

        product = new Book();
        product.setImage_url("https://m.media-amazon.com/images/W/WEBP_402378-T1/images/I/811wZEP2oxL._AC_UL480_FMwebp_QL65_.jpg");
        product.setTitle("Sweatshirt");
        product.setPrice(500);
        product.setStock_quantity(10);
        product.setBook_type_id(3);
        add_product(product);
    }

    public void add_category(Category category) {

        database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", category.getName());
        values.put("image", category.getImage());

        database.insert("category", null, values);
        database.close();
    }

    public void add_product(Book product) {

        database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", product.getTitle());
        values.put("image", product.getImage_url());
        values.put("price", product.getPrice());
        values.put("selled", 0);
        values.put("quantity", product.getStock_quantity());
        values.put("cate_id", product.getBook_type_id());

        database.insert("product", null, values);
        database.close();
    }

    public ArrayList<Book> get_products() {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from product", null);
        ArrayList<Book> arrayList = new ArrayList<>();

        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
//            (id "name "image "price "selled "quantity "cate_id integer
// int id, int stock_quantity, int book_type_id, String title, String author, String image_url, String description, double price
            /*arrayList.add(new Book(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getDouble(7)));*/
            cursor.moveToNext();
        }
        database.close();
        return arrayList;
    }

    public ArrayList<Book> get_category_products(int id) {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from product where id = '" + id + "'", null);
        ArrayList<Book> arrayList = new ArrayList<>();

        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
//            (id "name "image "price "selled "quantity "cate_id integer
            /*arrayList.add(new Book(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getDouble(7)));*/
            cursor.moveToNext();
        }
        database.close();
        return arrayList;
    }

    public ArrayList<Category> get_categories() {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from category", null);

        ArrayList<Category> arrayList = new ArrayList<>();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            arrayList.add(new Category(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)));
            cursor.moveToNext();
        }
        database.close();
        return arrayList;
    }

    public ArrayList<Book> search_product(String name) {
        database = getReadableDatabase();
        String[] args = {"%" + name + "%"};
        Cursor cursor = database.rawQuery("select * from Product where name like?", args);
        ArrayList<Book> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            /*arrayList.add(new Book(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getDouble(7)));*/
            cursor.moveToNext();
        }
        database.close();
        return arrayList;
    }

    public String frgot_password(String email) {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select password from user where email = '" + email + "'", null);
        cursor.moveToFirst();
        String password;
        if (cursor.getCount() > 0)
            password = cursor.getString(0);
        else
            password = null;
        database.close();
        return password;
    }

    public void add_to_cart(Book product, int user_id) {

        database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("product_id", product.getId());
        values.put("user_id", user_id);
        values.put("product_name", product.getTitle());
        values.put("product_image", product.getImage_url());
        values.put("product_price", product.getPrice());
        values.put("product_quantity", product.getStock_quantity());
        values.put("product_cat_id", product.getBook_type_id());

        database.insert("cart", null, values);
        database.close();
    }

    public ArrayList<Cart> get_cart(int user_id) {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from cart where user_id = '" + user_id + "'", null);
        ArrayList<Cart> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
//            arrayList.add(new Cart(
//                    cursor.getInt(0),
//                    cursor.getString(2),
//                    cursor.getString(3),
//                    cursor.getInt(4),
//                    cursor.getInt(1),
//                    cursor.getInt(5),
//                    cursor.getInt(6)));
//            cursor.moveToNext();
        }
        database.close();
        return arrayList;
    }

    public void delete_from_cart(int product_id, int user_id) {
        database = getReadableDatabase();
        database.delete("cart", "user_id = '" + user_id + "' and product_id = '" + product_id + "'", null);
        database.close();
    }

    public void change_cart_quantity(int product_id, int user_id, int quantity) {
        database = getReadableDatabase();
        String strSQL = "UPDATE cart SET product_quantity = '" + quantity + "' WHERE product_id = '" + product_id + "' and user_id = '" + user_id + "'";
        database.execSQL(strSQL);
        database.close();
    }

    public void delete_product(int product_id) {
        database = getReadableDatabase();
        database.delete("product", "id = '" + product_id + "'", null);
        database.close();
    }

    public void delete_category(int category_id) {
        database = getReadableDatabase();
        database.delete("category", "id = '" + category_id + "'", null);
        database.close();
    }

    public void edit_product(Book product) {
        database = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("id", product.getId());
        row.put("name", product.getTitle());
        row.put("image", product.getImage_url());
        row.put("price", product.getPrice());
        row.put("quantity", product.getStock_quantity());
        row.put("cate_id", product.getBook_type_id());

        database.update("product", row, "id = '" + product.getId() + "'", null);
        database.close();
    }

    public void edit_category(Category category) {
        database = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("id", category.getId());
        row.put("name", category.getName());
        row.put("image", category.getImage());

        database.update("category", row, "id = '" + category.getId() + "'", null);
        database.close();
    }

    public void increase_selled(int product_id, int quantity) {
        SQLiteDatabase database1 = getWritableDatabase();
        int selled = -1;
        Cursor cursor = database1.rawQuery("SELECT * FROM  product WHERE  id = '" + product_id + "'", null);
        cursor.moveToFirst();
        selled = cursor.getInt(4);
        selled += quantity;
        ContentValues values = new ContentValues();
        values.put("selled", selled);
        database1.update("product ", values, "id = '" + product_id + "'", null);
        database1.close();
    }

    public void confirm_order(Order order, ArrayList<Cart> carts) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("date", order.getDate());
//        values.put("user_id", order.getUser_id());
//        values.put("address", order.getAddress());
//        values.put("feedback", order.getFeedback());
//        values.put("rate", order.getRate());
//        db.insert("orders", null, values);
//        db.close();

        database = getWritableDatabase();
        int order_id = -1;
        Cursor cursor = database.rawQuery("SELECT * FROM  orders WHERE  id = (SELECT MAX(id) FROM orders)", null);
        cursor.moveToFirst();
        order_id = cursor.getInt(0);
        database.close();

        for (int i = 0; i < carts.size(); i++) {
//            SQLiteDatabase database2 = getWritableDatabase();
//            ContentValues values2 = new ContentValues();
//            values2.put("order_id", order_id);
//            values2.put("product_id", carts.get(i).getProduct_id());
//            values2.put("product_name", carts.get(i).getName());
//            values2.put("quantity", carts.get(i).getQuantity());
//            database2.insert("order_details", null, values2);
//            increase_selled(carts.get(i).getProduct_id(), carts.get(i).getQuantity());
//            database2.close();
        }


        SQLiteDatabase database3 = getReadableDatabase();
        database3.delete("cart", "user_id = '" + order.getUser_id() + "'", null);
        database3.close();
    }

    public String get_most_seeled() {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  product WHERE  selled = (SELECT MAX(selled) FROM product)", null);
        cursor.moveToFirst();
        String product_name = cursor.getString(1);
        database.close();
        return product_name;
    }

    public ArrayList<Order> get_orders() {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from orders", null);
        ArrayList<Order> arrayList = new ArrayList<>();

        cursor.moveToFirst();

//        while (cursor.isAfterLast() == false) {
//            arrayList.add(new Order(
//                    cursor.getInt(0),
//                    cursor.getInt(2),
//                    cursor.getString(1),
//                    cursor.getString(3),
//                    cursor.getString(4),
//                    cursor.getDouble(5)));
//            cursor.moveToNext();
//        }
        database.close();
        return arrayList;
    }

    public ArrayList<OrderItem> get_report(String date) {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from orders where date = '" + date + "'", null);
        ArrayList<OrderItem> arrayList = new ArrayList<>();
        cursor.moveToFirst();

//        while (cursor.isAfterLast() == false) {
//            arrayList.add(new OrderItem(cursor.getInt(0)));
//            cursor.moveToNext();
//        }
        database.close();
        return arrayList;
    }

//    public ArrayList<OrderItem> get_report(String date, int user_id){
////        database = getReadableDatabase();
////        Cursor cursor = database.rawQuery("select * from orders where date = '" + date + "' and user_id = '" + user_id + "'", null);
////        ArrayList<OrderItem> arrayList = new ArrayList<>();
////        cursor.moveToFirst();
////
////        while (cursor.isAfterLast() == false) {
////            arrayList.add(new OrderItem(cursor.getInt(0)));
////            cursor.moveToNext();
////        }
////        database.close();
////        return arrayList;
//    }

    public ArrayList<Book> get_order_details(int order_id){
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from order_details where order_id = '" + order_id + "'", null);
        ArrayList<Book> arrayList = new ArrayList<>();
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            arrayList.add(new Book(cursor.getInt(3), cursor.getString(2)));
            cursor.moveToNext();
        }
        database.close();
        return arrayList;
    }

    public ArrayList<String> getUsersIds() {
        database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from user", null);
        ArrayList<String> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            arrayList.add(cursor.getString(0));
            cursor.moveToNext();
        }
        database.close();
        return arrayList;
    }
}