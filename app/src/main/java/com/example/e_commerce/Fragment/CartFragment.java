package com.example.e_commerce.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commerce.Activity.AdminActivity;
import com.example.e_commerce.Activity.OrderActivity;
import com.example.e_commerce.Activity.OrderPlaceActivity;
import com.example.e_commerce.Activity.ProductActivity;
import com.example.e_commerce.Activity.UserActivity;
import com.example.e_commerce.Database.Database;
import com.example.e_commerce.Model.Book;
import com.example.e_commerce.Model.Cart;
import com.example.e_commerce.Model.User;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.ICartService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.internal.concurrent.Task;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    Button btn_order;
    ListView list_cart;
    ArrayList<Cart> cart_products = new ArrayList<>();

    ICartService cartService = RepositoryBase.getCartService();


    LinearLayout empty, cart;
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        list_cart = v.findViewById(R.id.cart_list_user_cart);
        empty = v.findViewById(R.id.embty);
        cart = v.findViewById(R.id.cart);

        getAllCart();

        btn_order = v.findViewById(R.id.cart_btn_confirm_order);

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra("savedLocation","");
                startActivity(intent);
            }
        });
        return v;
    }

    public void getAllCart(){
        try{

            Call<List<Cart>> call = cartService.getAll();
            call.enqueue(new Callback<List<Cart>>() {
                @Override
                public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                    cart_products.clear();
                    List<Cart> list_carts = response.body();
                    if (list_carts == null){
                        return;
                    }
                    User user = User.getInstance();
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    String userJson = sharedPreferences.getString("current_user", null);
                    Gson gson = new Gson();
                    user = gson.fromJson(userJson, User.class);

                    Collections.reverse(list_carts);
                    for (Cart item : list_carts) {
                        if (item.getUser_id() == user.getId())
                            cart_products.add(item);
                    }
                    if (cart_products.size() == 0) {
                        empty.setVisibility(View.VISIBLE);
                        cart.setVisibility(View.GONE);
                    } else {
                        empty.setVisibility(View.GONE);
                        cart.setVisibility(View.VISIBLE);

                        CartFragment.UserCartAdapter userCartAdapter = new CartFragment.UserCartAdapter(cart_products);
                        list_cart.setAdapter(userCartAdapter);
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

    class UserCartAdapter extends BaseAdapter {

        ArrayList<Cart> cart_books = new ArrayList<>();

        public UserCartAdapter(ArrayList<Cart> cart_books) {
            this.cart_books = cart_books;
        }

        @Override
        public int getCount() {
            return cart_books.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return cart_books.get(i).getName();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View item = layoutInflater.inflate(R.layout.cart_product_item, null);

            ImageView product_image = (ImageView) item.findViewById(R.id.user_cart_iv_product_image);
            TextView product_name = (TextView) item.findViewById(R.id.user_cart_tv_product_name);
            TextView product_price = (TextView) item.findViewById(R.id.user_cart_tv_product_price);
            TextView product_quantity= (TextView) item.findViewById(R.id.user_cart_tv_product_amount);
            ImageButton btn_del = (ImageButton) item.findViewById(R.id.user_cart_btn_delete);

            ImageButton btn_increment = (ImageButton) item.findViewById(R.id.user_cart_btn_increment_amount);
            ImageButton btn_decrement = (ImageButton) item.findViewById(R.id.user_cart_btn_decrement_amount);

            product_name.setText(cart_products.get(i).getName());

            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Call<Cart> call = cartService.delete(cart_products.get(i).getId());
                        call.enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                if (response.body() != null) {
                                    Toast.makeText(getActivity(), "Xóa sách thành công", Toast.LENGTH_SHORT).show();
                                    getAllCart();
                                    if (cart_products.size() == 0) {
                                        empty.setVisibility(View.VISIBLE);
                                        cart.setVisibility(View.GONE);
                                    } else {
                                        empty.setVisibility(View.GONE);
                                        cart.setVisibility(View.VISIBLE);

                                        CartFragment.UserCartAdapter userCartAdapter = new CartFragment.UserCartAdapter(cart_products);
                                        list_cart.setAdapter(userCartAdapter);
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {
//                    Toast.makeText(AddProductFragment.this, "Save fail"
//                            , Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                }
            });

            btn_increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user = User.getInstance();
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    String userJson = sharedPreferences.getString("current_user", null);
                    Gson gson = new Gson();
                    user = gson.fromJson(userJson, User.class);

                    int quantity = Integer.parseInt(product_quantity.getText().toString());
                    quantity++;
                    product_quantity.setText(quantity+"");
                    Cart item = cart_products.get(i);
                    item.setQuantity(quantity);
                    updateCart(item);
                }
            });

            btn_decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user = User.getInstance();
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    String userJson = sharedPreferences.getString("current_user", null);
                    Gson gson = new Gson();
                    user = gson.fromJson(userJson, User.class);
                    int quantity = Integer.parseInt(product_quantity.getText().toString());

                    if(quantity != 1){
                        quantity--;
                        product_quantity.setText(quantity+"");
                        Cart item = cart_products.get(i);
                        item.setQuantity(quantity);
                        updateCart(item);
                    }

                }
            });

            product_name.setText(cart_products.get(i).getName());
            product_price.setText(cart_products.get(i).getPrice()+"");
            product_quantity.setText(cart_products.get(i).getQuantity()+"");
            String url = cart_products.get(i).getImage();
            Glide.with(getContext()).load(url).into(product_image);

            return item;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllCart();
    }
    public void updateCart(Cart cart){
            int id = cart.getId();
                try {
                    Call<Cart> call = cartService.update(id, cart);
                    call.enqueue(new Callback<Cart>() {
                        @Override
                        public void onResponse(Call<Cart> call, Response<Cart> response) {
                            if (response.body() != null) {
                                getAllCart();
                            }
                        }
                        @Override
                        public void onFailure(Call<Cart> call, Throwable t) {
                        }
                    });
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
        }
}