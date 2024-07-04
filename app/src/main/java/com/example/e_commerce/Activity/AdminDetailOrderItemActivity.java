package com.example.e_commerce.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commerce.Common.GlobalConfig;
import com.example.e_commerce.Common.ParseHelper;
import com.example.e_commerce.Model.Order;
import com.example.e_commerce.Model.OrderItem;
import com.example.e_commerce.Network.FCMSend;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IOrderItemService;
import com.example.e_commerce.Service.IOrderService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDetailOrderItemActivity extends AppCompatActivity {
    ListView list_order_item;
    IOrderItemService orderItemService  = RepositoryBase.getOrderItemService();
    IOrderService orderService  = RepositoryBase.getOrderService();
    ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_order_item);
        Intent n = getIntent();
        int id =  n.getExtras().getInt("id");
        String status =  n.getExtras().getString("status");
        String address =  n.getExtras().getString("address");
        int price =  n.getExtras().getInt("price");
        Date create_at =  (Date) n.getSerializableExtra("create_at");


        TextView product_status = (TextView) findViewById(R.id.admin_manage_order_item_status);
        TextView product_price = (TextView) findViewById(R.id.admin_manage_order_item_price);
        TextView product_create_at = (TextView) findViewById(R.id.admin_manage_order_item_date);
        TextView product_address= (TextView) findViewById(R.id.admin_manage_order_item_address);
        Button btn_confirm_order = (Button) findViewById(R.id.btn_confirm_order);
        if (status.equals("Chờ xác nhận")){
            product_status.setTextColor(Color.rgb(40, 150, 136));
            btn_confirm_order.setText("Nhận đơn");
        } else if (status.equals("Đã xác nhận")) {
            btn_confirm_order.setText("Bắt đầu giao hàng");
        } else if (status.equals("Đang giao hàng")) {
            btn_confirm_order.setText("Giao hàng thành công");
        }
        list_order_item = findViewById(R.id.admin_manage_order_item_list);

        product_address.setText(address);
        product_status.setText(status);
        String strPrice = ParseHelper.intToString(price);
        product_price.setText(strPrice);
        String strCreateAt = ParseHelper.dateTimeToString(create_at);
        product_create_at.setText(strCreateAt);
        getAllOrderItem(id);
        btn_confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("Chờ xác nhận")){
                    updateStatusOrder(id, "Đã xác nhận");
                    pushNotification("Thông báo từ GoodTome", "Đơn hàng đã xác nhận");

                } else if (status.equals("Đã xác nhận")){
                    updateStatusOrder(id, "Đang giao hàng");
                    pushNotification("Thông báo từ GoodTome", "Đơn hàng đang giao");
                } else if (status.equals("Đang giao hàng")){
                    updateStatusOrder(id, "Giao hàng thành công");
                    pushNotification("Thông báo từ GoodTome", "Đơn hàng giao thành công");
                }
            }
        });

    }

    private void pushNotification(String title, String content){

        String token = GlobalConfig.RECEIVE_TOKEN;

        FCMSend.pushNotification(AdminDetailOrderItemActivity.this
                ,token
                , title
                , content);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(AdminDetailOrderItemActivity.this, AdminActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("adminGate", 6);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void getAllOrderItem(int id){
        orderItems.clear();
        try{
            Call<List<OrderItem>> call = orderItemService.getAll();
            call.enqueue(new Callback<List<OrderItem>>() {
                @Override
                public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                    List<OrderItem> resList = response.body();
                    if (resList == null){
                        return;
                    }
                    for (OrderItem item : resList){
                       if(item.getOrder_id() == id)
                            orderItems.add(item);
                    }
                    DetailOrderItemAdapter adapter = new DetailOrderItemAdapter(orderItems);
                    list_order_item.setAdapter(adapter);
                }
                @Override
                public void onFailure(Call<List<OrderItem>> call, Throwable t) {

                }
            });

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }

    private void updateStatusOrder(int id, String status){
        Order tempOrder = new Order(status);
        try{
            Call<Order> call = orderService.update(id, tempOrder);
            call.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    Toast.makeText(AdminDetailOrderItemActivity.this, "Thay đổi trạng thái thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    intent.putExtra("status", status);
                    finish();
                    startActivity(intent);
                }
                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                }
            });

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }
    class DetailOrderItemAdapter extends BaseAdapter {
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        public DetailOrderItemAdapter(ArrayList<OrderItem> orderItems) {
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View item = layoutInflater.inflate(R.layout.admin_manage_order_product_item, null);

            ImageView item_image = (ImageView) item.findViewById(R.id.admin_manage_order_product_image);
            TextView item_name = (TextView) item.findViewById(R.id.admin_manage_order_product_name);
            TextView item_quantity = (TextView) item.findViewById(R.id.admin_manage_order_product_quantity);

            item_name.setText(orderItems.get(i).getName());
            item_quantity.setText(orderItems.get(i).getQuantity() + "");
            String url = orderItems.get(i).getImage();
            Glide.with(getApplicationContext()).load(url).into(item_image);

            return item;
        }
    }
}
