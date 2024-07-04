package com.example.e_commerce.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerce.Fragment.UserHomeFragment;
import com.example.e_commerce.Model.OrderItem;
import com.example.e_commerce.Model.User;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IOrderItemService;
import com.example.e_commerce.Service.IUserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    TextView tv_username, tv_email, tv_gender, tv_fullname, tv_phone_number, tv_address;

    Button btn_edit;
    SharedPreferences.Editor myEdit;
    SharedPreferences sharedPreferences;
    IUserService userService = RepositoryBase.getUserService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        User user = User.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        String userJson = sharedPreferences.getString("current_user", null);

        Gson gson = new Gson();
        user = gson.fromJson(userJson, User.class);

        tv_username = findViewById(R.id.profile_tv_username);
        tv_email = findViewById(R.id.profile_tv_email);
        tv_phone_number =findViewById(R.id.profile_tv_phone_number);
        tv_fullname = findViewById(R.id.profile_tv_fullname);
        tv_gender = findViewById(R.id.profile_tv_gender);
        tv_address = findViewById(R.id.profile_tv_address);
        btn_edit = findViewById(R.id.profile_btn_edit);

        getUser(user.getId());

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this, EditProfileActivity.class));
            }
        });

    }
    public void getUser(int userId){
        try{
            Call<User> call = userService.getById(userId);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    tv_username.setText(user.getUsername());
                    tv_email.setText(user.getEmail());
                    tv_phone_number.setText(user.getPhone_number());
                    tv_fullname.setText(user.getFullname());
                    tv_gender.setText(user.getGender());
                    tv_address.setText(user.getAddress());

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }

            });

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }
}
