package com.example.e_commerce.Activity;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Common.ApplicationUser;
import com.example.e_commerce.Helper.AppInfo;
import com.example.e_commerce.Helper.CreateOrder;
import com.example.e_commerce.Model.Cart;
import com.example.e_commerce.Model.Order;
import com.example.e_commerce.Model.OrderItem;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.ICartService;
import com.example.e_commerce.Service.IOrderItemService;
import com.example.e_commerce.Service.IOrderService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentActivity extends AppCompatActivity {
    ImageButton btnZaloPay;
    TextView tvTotal;
    String token = "";
    IOrderItemService orderItemService;
    IOrderService orderService;
    ICartService cartService;
    int cost;
    List<Cart> carts = new ArrayList<Cart>();
    Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        init();
        btnZaloPay.setOnClickListener(v -> {

            ZaloPaySDK.getInstance().payOrder(PaymentActivity.this, token, "goodtome://app", new PayOrderListener() {
                @Override
                public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            saveOrder();
                        }

                    });

                }

                @Override
                public void onPaymentCanceled(String zpTransToken, String appTransID) {

//                    new AlertDialog.Builder(PaymentActivity.this)
//                            .setTitle("User Cancel Payment")
//                            .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            })
//                            .setNegativeButton("Cancel", null).show();
//                    saveOrder();
//                    Intent intent = new Intent(PaymentActivity.this, UserActivity.class);
//                    startActivity(intent);
                }

                @Override
                public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                    new AlertDialog.Builder(PaymentActivity.this)
                            .setTitle("Thanh toán thất bại")
                            .setMessage("Đơn hàng của bạn thanh toán không thành công!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setNegativeButton("Cancel", null).show();
                }
            });
        });
    }

    private void init() {
        ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX);
        btnZaloPay = findViewById(R.id.btnZaloPay);
        tvTotal = findViewById(R.id.textView_total);
        cost = getIntent().getIntExtra("total", 50_000);
        tvTotal.setText(cost + "");
        CreateOrder orderApi = new CreateOrder();
        JSONObject data;
        try {
            data = orderApi.createOrder(cost+"");
            token = data.getString("zp_trans_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        order = (Order) getIntent().getSerializableExtra("order");
//        carts= (List<Cart>) getIntent().getSerializableExtra("carts");
        order = ApplicationUser.getOrder(PaymentActivity.this);
//        carts = ApplicationUser.getCarts(PaymentActivity.this);

        orderService = RepositoryBase.getOrderService();
        cartService = RepositoryBase.getCartService();
        orderItemService = RepositoryBase.getOrderItemService();

    }

    private void saveOrder() {
        try {
            Call<Order> callO = orderService.create(order);
            callO.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> callO, Response<Order> response) {
                    if (response.body() != null) {
//                                            Toast.makeText(OrderActivity.this, "Sách đã được thêm vào giỏ hàng"
//                                                    , Toast.LENGTH_LONG).show();
                        Order savedOrder = (Order) response.body();
                        //----GET ALL USER CART
                            try {
                                Call<List<Cart>> callC = cartService.getAll();
                                callC.enqueue(new Callback<List<Cart>>() {
                                    @Override
                                    public void onResponse(Call<List<Cart>> callC, Response<List<Cart>> response) {
                                        if (response.body() != null) {
                                            List<Cart> resCart = response.body();
                                           for ( Cart item : resCart ) {
                                               if (item.getUser_id() == savedOrder.getUser_id())
                                                carts.add(item);
                                            }
                                            saveOrderItem(savedOrder, carts);
                                        }
                                     }
                                    @Override
                                    public void onFailure(Call<List<Cart>> call, Throwable t) {
                                        Toast.makeText(PaymentActivity.this, t.getMessage()
                                                , Toast.LENGTH_LONG).show();
                                    }
                                });
                            } catch (Exception e) {
                                Log.d("Error", e.getMessage());
                            }
                        //------END GET ALL USER CART
                    }
                }
                @Override
                public void onFailure(Call<Order> callO, Throwable t) {
                    Toast.makeText(PaymentActivity.this, t.getMessage()
                            , Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    private void saveOrderItem(Order order, List<Cart> carts) {
        for (int i = 0; i < carts.size(); i++) {
            Cart cart = carts.get(i);
            OrderItem orderItem = new OrderItem(cart.getBook_id(), cart.getBook_type_id(), cart.getUser_id(), order.getId(), cart.getQuantity(), cart.getPrice(), cart.getName(), cart.getImage(), "Đã xác nhận");
            try {
                Call<OrderItem> callO = orderItemService.create(orderItem);
                System.out.println(orderItem);
                callO.enqueue(new Callback<OrderItem>() {
                    @Override
                    public void onResponse(Call<OrderItem> callO, Response<OrderItem> response) {
                        if (response.body() != null) {
//                                            Toast.makeText(OrderActivity.this, "Sách đã được thêm vào giỏ hàng"
//                                                    , Toast.LENGTH_LONG).show();
                            Call<Cart> call2 = cartService.delete(cart.getId());
                            call2.enqueue(new Callback<Cart>() {
                                @Override
                                public void onResponse(Call<Cart> call2, Response<Cart> response) {
                                    if (response.body() != null) {
//                                            Toast.makeText(OrderActivity.this, "Sách đã được thêm vào giỏ hàng"
//                                                    , Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(PaymentActivity.this, UserActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(PaymentActivity.this, "Thanh toán đơn hàng thành công", LENGTH_LONG).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Cart> call2, Throwable t) {
                                    Toast.makeText(PaymentActivity.this, "Save cart fail"
                                            , Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                    @Override
                    public void onFailure(Call<OrderItem> callO, Throwable t) {
                        Toast.makeText(PaymentActivity.this, "Save fail"
                                , Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                Log.d("Error", e.getMessage());
            }
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CreateOrder orderApi = new CreateOrder();
        JSONObject data;
        try {
            data = orderApi.createOrder(cost+"");
            token = data.getString("zp_trans_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}