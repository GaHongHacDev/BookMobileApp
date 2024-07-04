package com.example.e_commerce.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerce.Model.Book;
import com.example.e_commerce.Model.BookType;
import com.example.e_commerce.Model.User;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IBookService;
import com.example.e_commerce.Service.IUserService;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    EditText tv_fullname,tv_address, tv_phone_number;
    TextView tv_username, tv_email;
    Spinner spinner_gender;
    Button btn_edit;
    IUserService userService = RepositoryBase.getUserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        User user = User.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("current_user", null);


        Gson gson = new Gson();
        user = gson.fromJson(userJson, User.class);
        btn_edit = findViewById(R.id.edit_profile_btn_edit);
        tv_fullname = findViewById(R.id.edit_profile_fullname);
        tv_address = findViewById(R.id.edit_profile_address);
        tv_phone_number = findViewById(R.id.edit_profile_phone);
        tv_username = findViewById(R.id.profile_tv_username);
        tv_email = findViewById(R.id.edit_profile_email);
        spinner_gender = findViewById(R.id.profile_edt_gender);

        getUser(user.getId());

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(UserProfileActivity.this, EditProfileActivity.class));
                User user = User.getInstance();
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                String userJson = sharedPreferences.getString("current_user", null);

                Gson gson = new Gson();
                user = gson.fromJson(userJson, User.class);

                String fullName = tv_fullname.getText().toString(),
                        address = tv_address.getText().toString(),
                        phone_number = tv_phone_number.getText().toString(),
                        gender = spinner_gender.getSelectedItem().toString();

                User editUser = new User(fullName, user.getEmail(), user.getPassword(),phone_number, user.getUsername(), "USER", address, "avatar", gender, user.getCreateAt(), 1 );

                try{
                    Call<User> call = userService.update(user.getId(), editUser);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.body() != null){
                                Intent myIntent = new Intent(EditProfileActivity.this, UserProfileActivity.class);
                                EditProfileActivity.this.startActivity(myIntent);
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }

                    });
                } catch (Exception e){
                    Log.d("Error", e.getMessage());
                }
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
                    tv_address.setText(user.getAddress());
                    ArrayList<String> gender_list = new ArrayList<String>();
                    gender_list.add("Nam");
                    gender_list.add("Nữ");
                    gender_list.add("Khác");



                    ArrayAdapter adapter = new ArrayAdapter(EditProfileActivity.this,android.R.layout.simple_spinner_item,gender_list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_gender.setAdapter(adapter);

                    if ("Nam".equals(user.getGender())) {
                        spinner_gender.setSelection(0);
                    } else if ("Nữ".equals(user.getGender())) {
                        spinner_gender.setSelection(1);
                    } else {
                        spinner_gender.setSelection(2);
                    }

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
