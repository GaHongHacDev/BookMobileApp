package com.example.e_commerce.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commerce.Model.Book;
import com.example.e_commerce.Model.BookType;
import com.example.e_commerce.Model.Cart;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IBookService;
import com.example.e_commerce.Service.IBookTypeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductActivity extends AppCompatActivity {

    EditText txt_image_url, txt_title, txt_author, txt_price, txt_stock_quantity, txt_description;
    Button btn_show, btn_edit;
    Spinner spinner_book_type;
    ImageView iv_product_image;
    IBookTypeService bookTypeService = RepositoryBase.getBookTypeService();
    IBookService bookService = RepositoryBase.getBookService();
    ArrayList<BookType> bookTypes = new ArrayList<BookType>();
    Spinner spinner_categoty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

//        getSupportActionBar().hide();

        txt_image_url = findViewById(R.id.edit_product_txt_image_url);
        txt_title = findViewById(R.id.edit_product_txt_title);
        txt_price = findViewById(R.id.edit_product_txt_price);
        txt_author = findViewById(R.id.edit_product_txt_author);
        txt_description = findViewById(R.id.edit_product_txt_description);
        txt_stock_quantity = findViewById(R.id.edit_product_txt_stock_quantity);
        iv_product_image = findViewById(R.id.edit_product_iv_image);
        btn_show = findViewById(R.id.edit_product_btn_show_image);
        btn_edit = findViewById(R.id.edit_product_btn_edit);
        spinner_book_type = findViewById(R.id.edit_product_spinner_category);

        Intent n = getIntent();
        int id =  n.getExtras().getInt("id") ;
        int stock_quantity = n.getExtras().getInt("stock_quantity") ;
        int old_book_type_id = n.getExtras().getInt("book_type_id") ;
        String title =  n.getExtras().getString("title") ;
        int price =  n.getExtras().getInt("price") ;
        String description =  n.getExtras().getString("description") ;
        String image_url =  n.getExtras().getString("image_url") ;
        String author =  n.getExtras().getString("author") ;
        showNameBookType(old_book_type_id);


        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = txt_image_url.getText().toString();
                Glide.with(getApplicationContext()).load(url).into(iv_product_image);
            }
        });

        // TODO: show product data in txt fildes
        Glide.with(getApplicationContext()).load(image_url).into(iv_product_image);
        txt_image_url.setText(image_url);
        txt_title.setText(title);
        txt_author.setText(author);
        txt_description.setText(description);
        txt_price.setText(price+"");
        txt_stock_quantity.setText(stock_quantity+"");


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: edit product
                String title = txt_title.getText().toString();
                int price = Integer.parseInt(txt_price.getText().toString());
                int stock_quantity = Integer.parseInt(txt_stock_quantity.getText().toString());
                String new_book_type_name = spinner_book_type.getSelectedItem().toString();
                String image_url = txt_image_url.getText().toString();
                String author = txt_author.getText().toString();
                String description = txt_description.getText().toString();

                int new_book_type_id = 0;
                int new_book_type_quantity = 0;
                int old_book_type_quantity = 0;

                for(int i = 0 ; i < bookTypes.size() ; i++){
                   if(bookTypes.get(i).getType_name() == new_book_type_name){
                       new_book_type_quantity = bookTypes.get(i).getQuantity();
                       new_book_type_id =  bookTypes.get(i).getId();
                   }

                    if(bookTypes.get(i).getId() == old_book_type_id){
                       old_book_type_quantity = bookTypes.get(i).getQuantity();
                    }
                }
                //-----INCREASE QUANTITY BOOK TYPE ---------------
                BookType bookTypeNew = new BookType(new_book_type_id,new_book_type_quantity + 1);
                try{
                    Call<BookType> callBTN = bookTypeService.update(new_book_type_id, bookTypeNew);
                    callBTN.enqueue(new Callback<BookType>() {
                        @Override
                        public void onResponse(Call<BookType> call, Response<BookType> response) {
                            if (response.body() != null){

                            }
                        }
                        @Override
                        public void onFailure(Call<BookType> call, Throwable t) {
                            Toast.makeText(EditProductActivity.this, "Save fail"
                                    , Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e){
                    Log.d("Error", e.getMessage());
                }

                //-----DECREASE QUANTITY BOOK TYPE ---------------
                BookType bookTypeOld= new BookType(old_book_type_id, old_book_type_quantity - 1);
                try{
                    Call<BookType> callBTO = bookTypeService.update(old_book_type_id, bookTypeOld);
                    callBTO.enqueue(new Callback<BookType>() {
                        @Override
                        public void onResponse(Call<BookType> call, Response<BookType> response) {
                            if (response.body() != null){

                            }
                        }
                        @Override
                        public void onFailure(Call<BookType> call, Throwable t) {
                            Toast.makeText(EditProductActivity.this, "Save fail"
                                    , Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e){
                    Log.d("Error", e.getMessage());
                }

                //-----UPDATE BOOK ---------------
                Book book = new Book(stock_quantity, new_book_type_id, title, author, image_url, description, price);
                try{
                    Call<Book> call = bookService.update(id, book);
                    call.enqueue(new Callback<Book>() {
                        @Override
                        public void onResponse(Call<Book> call, Response<Book> response) {
                            if (response.body() != null){
                                Intent myIntent = new Intent(EditProductActivity.this, AdminActivity.class);
                                myIntent.putExtra("adminGate",4);
                                EditProductActivity.this.startActivity(myIntent);
                            }
                        }
                        @Override
                        public void onFailure(Call<Book> call, Throwable t) {
                            Toast.makeText(EditProductActivity.this, "Save fail"
                                    , Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e){
                    Log.d("Error", e.getMessage());
                }
                //-----END CREATE BOOK ---------------

            }
        });
    }
    private void showNameBookType(int book_type_id){
        try{
            Call<List<BookType>> call = bookTypeService.getAll();
            call.enqueue(new Callback<List<BookType>>() {
                @Override
                public void onResponse(Call<List<BookType>> call, Response<List<BookType>> response) {
                    List<BookType> resList = response.body();
                    if (resList == null){
                        return;
                    }

                    for (BookType bookType : resList){
                        bookTypes.add(bookType);
                    }
                    ArrayList<String> category_name = new ArrayList<String>();
                    for(int i = 0 ; i < bookTypes.size() ; i++){
                        category_name.add(bookTypes.get(i).getType_name());
                    }
                    String cat_name;
                    int i = 0;
                    for(i = 0 ; i < bookTypes.size() ; i++){
                        if(bookTypes.get(i).getId() == book_type_id){
                            cat_name = bookTypes.get(i).getType_name();
                            break;
                        }
                    }
                    ArrayAdapter aa = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,category_name);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_book_type.setAdapter(aa);
                    spinner_book_type.setSelection(i);

                }
                @Override
                public void onFailure(Call<List<BookType>> call, Throwable t) {

                }
            });

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }
}