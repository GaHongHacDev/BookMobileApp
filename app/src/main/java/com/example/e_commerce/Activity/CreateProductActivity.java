package com.example.e_commerce.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IBookService;
import com.example.e_commerce.Service.IBookTypeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProductActivity extends AppCompatActivity {
    EditText txt_image_url, txt_title, txt_price, txt_stock_quantity, txt_description, txt_author ;
    Button btn_show, btn_add;
    Spinner spinner_category;
    ImageView iv_product_image;
    ArrayList<BookType> bookTypes = new ArrayList<BookType>();
    IBookTypeService bookTypeService = RepositoryBase.getBookTypeService();
    IBookService bookService = RepositoryBase.getBookService();
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CreateProductActivity.this, AdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("adminGate", 8);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        spinner_category = findViewById(R.id.add_product_spinner_category);
        txt_title = findViewById(R.id.add_product_txt_title);
        txt_image_url = findViewById(R.id.add_product_txt_image_url);
        txt_price = findViewById(R.id.add_product_txt_price);
        txt_author = findViewById(R.id.add_product_txt_author);
        txt_description = findViewById(R.id.add_product_txt_description);
        txt_stock_quantity = findViewById(R.id.add_product_txt_stock_quantity);
        iv_product_image = findViewById(R.id.add_product_iv_image);
        btn_show = findViewById(R.id.add_product_btn_show_image);
        btn_add = findViewById(R.id.add_product_btn_add);

        bookService = RepositoryBase.getBookService();
        bookTypeService = RepositoryBase.getBookTypeService();

        showNameBookType(); // sprint

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_image_url.getText().toString().isEmpty()) {
                    String url = txt_image_url.getText().toString();
                    Glide.with(CreateProductActivity.this).load(url).into(iv_product_image);
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add product
                String image_url = txt_image_url.getText().toString();
                String title = txt_title.getText().toString();
                String author = txt_author.getText().toString();
                String description = txt_description.getText().toString();
                int price = -1;
                price = Integer.parseInt(txt_price.getText().toString());
                int stock_quantity = -1;
                stock_quantity = Integer.parseInt(txt_stock_quantity.getText().toString());
                String book_type_name = spinner_category.getSelectedItem().toString();
                int bookType_id = 0;
                int bookType_quantity = 0;
                if(!image_url.isEmpty() && !title.isEmpty() && !author.isEmpty() && !description.isEmpty() && price!=-1 && stock_quantity!=-1){
                    for(int i = 0 ; i < bookTypes.size() ; i++){
                        if(bookTypes.get(i).getType_name().equals(book_type_name)){
                            bookType_id = bookTypes.get(i).getId();
                            bookType_quantity = bookTypes.get(i).getQuantity();
                            break;
                        }
                    }
                    BookType temp = new BookType(bookType_id, bookType_quantity + 1);
                    Book product = new Book();
                    product.setTitle(title);
                    product.setPrice(price);
                    product.setStock_quantity(stock_quantity);
                    product.setImage_url(image_url);
                    product.setBook_type_id(bookType_id);
                    product.setAuthor(author);
                    product.setDescription(description);
                    product.setStatus(1);

                    createBook(product, temp);

                    txt_image_url.setText("");
                    txt_title.setText("");
                    txt_price.setText("");
                    txt_stock_quantity.setText("");
                    txt_author.setText("");
                    txt_description.setText("");
                    iv_product_image.setImageResource(R.drawable.image_placeholder);

                    Intent myIntent = new Intent(CreateProductActivity.this, AdminActivity.class);
                    myIntent.putExtra("adminGate",5);
                    CreateProductActivity.this.startActivity(myIntent);

                }else{
                    Toast.makeText(CreateProductActivity.this, "Fill all data first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void createBook(Book book, BookType temp){
        try{
            Call<Book> call = bookService.create(book);

            call.enqueue(new Callback<Book>() {
                @Override
                public void onResponse(Call<Book> call, Response<Book> response) {
                    if (response.body() != null){
                        //Update bookTyep quantity
                        try{
                            Call<BookType> callBT = bookTypeService.update(temp.getId(), temp);
                            callBT.enqueue(new Callback<BookType>() {
                                @Override
                                public void onResponse(Call<BookType> call, Response<BookType> response) {
                                    if (response.body() != null){
                                    }
                                }
                                @Override
                                public void onFailure(Call<BookType> call, Throwable t) {

                                }
                            });
                        } catch (Exception e){
                            Log.d("Error", e.getMessage());
                        }
                    }
                }
                @Override
                public void onFailure(Call<Book> call, Throwable t) {

                }
            });
        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }
    private void showNameBookType() {
        try {
            Call<List<BookType>> call = bookTypeService.getAll();
            call.enqueue(new Callback<List<BookType>>() {
                @Override
                public void onResponse(Call<List<BookType>> call, Response<List<BookType>> response) {
                    List<BookType> resList = response.body();
                    if (resList == null) {
                        return;
                    }

                    for (BookType bookType : resList) {
                        bookTypes.add(bookType);
                    }

                    ArrayList<String> book_type_name = new ArrayList<String>();
                    for (int i = 0; i < bookTypes.size(); i++) {
                        book_type_name.add(bookTypes.get(i).getType_name());
                    }

                    ArrayAdapter aa = new ArrayAdapter(CreateProductActivity.this, android.R.layout.simple_spinner_item, book_type_name);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_category.setAdapter(aa);


                }

                @Override
                public void onFailure(Call<List<BookType>> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }
}