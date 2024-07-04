package com.example.e_commerce.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.e_commerce.Fragment.CartFragment;
import com.example.e_commerce.Model.Book;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IBookService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryProductsActivity extends AppCompatActivity {
    ListView user_list_category_products;
    LinearLayout embty;
    ArrayList<Book> products = new ArrayList<>();
    Intent n;
    IBookService bookService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        bookService = RepositoryBase.getBookService();
        user_list_category_products = findViewById(R.id.user_category_list_products);
        embty = findViewById(R.id.embty);
        getAllBook();


        user_list_category_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);

                intent.putExtra("id",products.get(i).getId());
                intent.putExtra("stock_quantity",products.get(i).getStock_quantity());
                intent.putExtra("book_type_id",products.get(i).getBook_type_id());
                intent.putExtra("title",products.get(i).getTitle());
                intent.putExtra("author",products.get(i).getAuthor());
                intent.putExtra("image_url",products.get(i).getImage_url());
                intent.putExtra("description",products.get(i).getDescription());
                intent.putExtra("price",products.get(i).getPrice());
                startActivity(intent);
            }
        });
    }

    private void getAllBook(){
        try{
            Call<List<Book>> call = bookService.getAll();
            call.enqueue(new Callback<List<Book>>() {
                @Override
                public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                    List<Book> books = response.body();
                    if (books == null){
                        return;
                    }
                    n = getIntent();
                    int id =  n.getExtras().getInt("id") ;

                    for (Book book : books){
                        if (book.getBook_type_id() == id)  products.add(book);
                    }
                    if (products.size() == 0) {
                        embty.setVisibility(View.VISIBLE);
                        user_list_category_products.setVisibility(View.GONE);
                    } else {
                        embty.setVisibility(View.GONE);
                        user_list_category_products.setVisibility(View.VISIBLE);

                        CategoryProductsActivity.UserHomeCategoryProductsAdapter userHomeCategoryProductsAdapter
                                = new CategoryProductsActivity.UserHomeCategoryProductsAdapter(products);
                        user_list_category_products.setAdapter(userHomeCategoryProductsAdapter);
                    }
                }
                @Override
                public void onFailure(Call<List<Book>> call, Throwable t) {

                }
            });

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }

    class UserHomeCategoryProductsAdapter extends BaseAdapter {

        ArrayList<Book> products = new ArrayList<>();

        public UserHomeCategoryProductsAdapter(ArrayList<Book> products) {
            this.products = products;
        }

        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return products.get(i).getTitle();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View item = layoutInflater.inflate(R.layout.user_home_product_item, null);

            ImageView product_image = (ImageView) item.findViewById(R.id.user_home_iv_product_image);
            TextView product_name = (TextView) item.findViewById(R.id.user_home_tv_product_name);
            TextView product_price = (TextView) item.findViewById(R.id.user_home_tv_product_price);

            product_name.setText(products.get(i).getTitle());
            product_price.setText(products.get(i).getPrice()+"");
            String url = products.get(i).getImage_url();
            Glide.with(getApplicationContext()).load(url).into(product_image);

            return item;
        }
    }
}