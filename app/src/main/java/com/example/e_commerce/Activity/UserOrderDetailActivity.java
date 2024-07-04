package com.example.e_commerce.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerce.Common.ParseHelper;
import com.example.e_commerce.Model.Cart;
import com.example.e_commerce.Model.OrderItem;
import com.example.e_commerce.Model.User;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IOrderItemService;
import com.example.e_commerce.Service.IOrderService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserOrderDetailActivity extends AppCompatActivity {
    ListView user_order_detail_list;
    TextView txt_total;
    ArrayList<OrderItem> order_items = new ArrayList<>();
    IOrderItemService orderItemService = RepositoryBase.getOrderItemService();
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_detail);

        Intent intent = getIntent();
        id = intent.getExtras().getInt("id") ;
        user_order_detail_list = findViewById(R.id.user_order_list);
        txt_total = findViewById(R.id.txt_total);
        getAllOrderItem();
    }

    class OrderItemAdapter extends BaseAdapter {

        ArrayList<OrderItem> orderItems = new ArrayList<>();

        public OrderItemAdapter(ArrayList<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }

        @Override
        public int getCount() {
            return orderItems.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return orderItems.get(i).getName();
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View item = layoutInflater.inflate(R.layout.activity_user_order_detail_item, null);

            TextView product_name = (TextView) item.findViewById(R.id.order_product_name);
            TextView product_price = (TextView) item.findViewById(R.id.order_product_price);
            TextView product_quantity= (TextView) item.findViewById(R.id.order_product_quantity);

            String price = ParseHelper.intToString(order_items.get(i).getPrice());
            product_price.setText(price);

            product_name.setText(order_items.get(i).getName());
            //product_price.setText(order_items.get(i).getPrice()+"");
            product_quantity.setText(order_items.get(i).getQuantity()+"");

            return item;
        }
    }

    public void getAllOrderItem(){
        try{
            Call<List<OrderItem>> call = orderItemService.getAll();
            call.enqueue(new Callback<List<OrderItem>>() {
                @Override
                public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                    order_items.clear();
                    List<OrderItem> list_order_items = response.body();
                    if (list_order_items == null){
                        return;
                    }
                    User user = User.getInstance();
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    String userJson = sharedPreferences.getString("current_user", null);
                    Gson gson = new Gson();
                    user = gson.fromJson(userJson, User.class);
                    for (OrderItem item : list_order_items) {
                        if (item.getOrder_id() == id)

                            order_items.add(item);
                    }
                    UserOrderDetailActivity.OrderItemAdapter orderItemAdapter = new UserOrderDetailActivity.OrderItemAdapter(order_items);
                    user_order_detail_list.setAdapter(orderItemAdapter);
                    int total = 0;
                    for (int i = 0 ; i < order_items.size() ; i++){
                        total += order_items.get(i).getPrice() * order_items.get(i).getQuantity();
                    }

                    String strTotal = ParseHelper.intToString(total);
                    txt_total.setText(strTotal);
                }

                @Override
                public void onFailure(Call<List<OrderItem>> call, Throwable t) {

                }

            });

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }


}
