package com.example.e_commerce.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.e_commerce.Model.Book;
import com.example.e_commerce.R;

import java.util.ArrayList;
import java.util.List;

public class UserHomeCategoryBooksAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Book> bookList = new ArrayList<>();
    @Override
    public int getCount() {
        //return 0;
        return bookList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View item = inflater.inflate(R.layout.user_home_product_item, null);

        ImageView product_image = (ImageView) item.findViewById(R.id.user_home_iv_product_image);
        TextView product_name = (TextView) item.findViewById(R.id.user_home_tv_product_name);
        TextView product_price = (TextView) item.findViewById(R.id.user_home_tv_product_price);

        product_name.setText(bookList.get(i).getTitle());
        product_price.setText(bookList.get(i).getPrice());
        String url = bookList.get(i).getImage_url();
        Glide.with(context.getApplicationContext()).load(url).into(product_image);

        return item;
    }
}
