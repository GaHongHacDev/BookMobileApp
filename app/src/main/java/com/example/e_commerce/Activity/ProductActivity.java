package com.example.e_commerce.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commerce.Database.Database;
import com.example.e_commerce.Fragment.ManageProductFragment;
import com.example.e_commerce.Model.Book;
import com.example.e_commerce.Model.Cart;
import com.example.e_commerce.Model.User;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IBookTypeService;
import com.example.e_commerce.Service.ICartService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    ImageButton imageButton_increment_amount, imageButton_decrement_amount;
    ArrayList<Cart> carts = new ArrayList<>();
    TextView textView_product_amount;
    int amount = 1;
    ImageView product_image;
    TextView tv_title, tv_price, tv_description, tv_author;
    Button btn_add;
    Intent n;
    ICartService cartService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        cartService = RepositoryBase.getCartService();

        n = getIntent();
        int book_id =  n.getExtras().getInt("id") ;
        int stock_quantity = n.getExtras().getInt("stock_quantity") ;
        int book_type_id = n.getExtras().getInt("book_type_id") ;
        String title =  n.getExtras().getString("title") ;
        int price =  n.getExtras().getInt("price") ;
        String description =  n.getExtras().getString("description") ;
        String image_url =  n.getExtras().getString("image_url") ;
        String author =  n.getExtras().getString("author") ;

        User user = User.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("current_user", null);
        Gson gson = new Gson();
        user = gson.fromJson(userJson, User.class);
        int user_id = user.getId();
        getAllCart();

        imageButton_increment_amount = findViewById(R.id.btn_increment_amount);
        imageButton_decrement_amount = findViewById(R.id.btn_decrement_amount);
        textView_product_amount = findViewById(R.id.textView_product_amount);

        product_image = findViewById(R.id.imageView_product_image);
        tv_title = findViewById(R.id.textView_product_title);
        tv_author = findViewById(R.id.textView_product_author);
        tv_price = findViewById(R.id.textView_product_price);
        tv_description = findViewById(R.id.textView_product_description);
        btn_add = findViewById(R.id.user_home_btn_add_to_cart);

        String url = image_url;
        Glide.with(getApplicationContext()).load(url).into(product_image);

        tv_title.setText(title);
        tv_price.setText(price+"");
        tv_description.setText(description);
        tv_author.setText(author);


        imageButton_increment_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(textView_product_amount.getText().toString());
                amount++;
                textView_product_amount.setText(amount + "");
            }
        });

        imageButton_decrement_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Integer.parseInt(textView_product_amount.getText().toString());
                if(amount > 1){
                    amount--;
                    textView_product_amount.setText(amount + "");
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Cart> carts = new ArrayList<>();

                Call<List<Cart>> call = cartService.getAll();
                call.enqueue(new Callback<List<Cart>>() {
                    @Override
                    public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                        List<Cart> list_carts = response.body();
                        if (list_carts == null){
                            return;
                        }
                        Collections.reverse(list_carts);
                        for (Cart item : list_carts) {
                            carts.add(item);
                        }

                        Cart cart = new Cart(book_id,book_type_id, user_id, amount, title, price * amount, image_url);
                        int id = -1;
                        for (Cart item : carts) {
                            if (item.getUser_id() == user_id && item.getBook_id() == book_id) {
                                id = item.getId();
                                cart.setQuantity(amount + item.getQuantity());
                                break;
                            }
                        }
                        if (id == -1) {
                            try {
                                Call<Cart> callC = cartService.create(cart);
                                callC.enqueue(new Callback<Cart>() {
                                    @Override
                                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                                        if (response.body() != null) {
                                            Toast.makeText(ProductActivity.this, "Sách đã được thêm vào giỏ hàng"
                                                    , Toast.LENGTH_LONG).show();
                                            amount = 1;
                                            textView_product_amount.setText(amount + "");
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Cart> call, Throwable t) {
                                        Toast.makeText(ProductActivity.this, "Save fail"
                                                , Toast.LENGTH_LONG).show();
                                    }
                                });
                            } catch (Exception e) {
                                Log.d("Error", e.getMessage());
                            }
                        } else {
                            try {
                                Call<Cart> callc = cartService.update(id, cart);
                                callc.enqueue(new Callback<Cart>() {
                                    @Override
                                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                                        if (response.body() != null) {
                                            Toast.makeText(ProductActivity.this, "Sách đã được thêm vào giỏ hàng"
                                                    , Toast.LENGTH_LONG).show();
                                            amount = 1;
                                            textView_product_amount.setText(amount + "");
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Cart> call, Throwable t) {
                                        Toast.makeText(ProductActivity.this, "Save fail"
                                                , Toast.LENGTH_LONG).show();
                                    }
                                });
                            } catch (Exception e) {
                                Log.d("Error", e.getMessage());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Cart>> call, Throwable t) {

                    }
                });


            }
        });
    }
    private void getAllCart(){
        carts.clear();
        try{
            Call<List<Cart>> call = cartService.getAll();
            call.enqueue(new Callback<List<Cart>>() {
                @Override
                public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                    List<Cart> list_carts = response.body();
                    if (list_carts == null){
                        return;
                    }
                    Collections.reverse(list_carts);
                    for (Cart item : list_carts) {
                        carts.add(item);
                    }
                }
                @Override
                public void onFailure(Call<List<Cart>> call, Throwable t) {

                }
            });

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }
}