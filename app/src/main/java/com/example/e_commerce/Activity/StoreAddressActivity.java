package com.example.e_commerce.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerce.Model.User;
import com.example.e_commerce.R;
import com.google.gson.Gson;

public class StoreAddressActivity  extends AppCompatActivity  {
    Button btn_address ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_address);

        btn_address  = findViewById(R.id.store_map_button);

//        link button đến trang ,ap chỉ đường đến cửa hàng
        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StoreAddressActivity.this, OrderPlaceActivity.class));
            }
        });

    }
}
