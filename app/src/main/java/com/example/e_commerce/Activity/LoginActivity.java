package com.example.e_commerce.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Common.ApplicationUser;
import com.example.e_commerce.Model.User;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IUserService;
import com.google.gson.Gson;

import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText txt_email, txt_password;
    TextView textView_forgot_password, textView_signup;
    IUserService userService;
    SharedPreferences.Editor myEdit;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        startActivity(new Intent(LoginActivity.this, AdminActivity.class));//comment me after finish task
        //Hide ActionBar
        getSupportActionBar().hide();


        userService = RepositoryBase.getUserService();
        txt_email = findViewById(R.id.login_txt_email);
        txt_password = findViewById(R.id.login_txt_password);
        btn_login = (Button) findViewById(R.id.login_btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                myEdit = sharedPreferences.edit();
                myEdit.commit();

                // TODO: login
                String email = txt_email.getText().toString().trim().toLowerCase(),
                        password = txt_password.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){
                    authenticate(email, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Hãy điền đầy đủ thông tin!"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

        textView_signup = (TextView) findViewById(R.id.login_tv_signup);
        textView_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate to signup screen
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    public void authenticate(String email, String password) {
        if (email.equals("admin") && password.equals("admin")) {

            User admin = ApplicationUser.parseUserFromJsonInAsset(this, "appsettings.json");
            ApplicationUser.saveCurrentUser(this, admin);
            Intent myIntent = new Intent(LoginActivity.this, AdminActivity.class);
                            myIntent.putExtra("adminGate",1);
                            ApplicationUser.registerUserToFireBase(admin);
                            startActivity(myIntent);
        }else {
            Call<List<User>> call = userService.getUserByEmailAndPassword(email, password);

            call.enqueue(new Callback<List<User>>() {
                             @Override
                             public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                 if (response.isSuccessful()) {
                                     List<User> users = response.body();
                                     if(users != null && users.size() > 0) {
                                         if (users.get(0).getEmail().equals(email) && users.get(0).getPassword().equals(password)) {
                                             User user = users.get(0);
                                             myEdit.putInt("id", user.getId());
                                             myEdit.putString("username", user.getUsername());
                                             myEdit.putString("email", user.getEmail());
                                             myEdit.putString("phone_number", user.getPhone_number());
                                             myEdit.putString("password", user.getPassword());
                                             Gson gson = new Gson();
                                             String userJson = gson.toJson(user);
                                             myEdit.putString("current_user", userJson);
                                             myEdit.apply();

//                                             ApplicationUser.saveCurrentUser(LoginActivity.this, user);
//                                                         ApplicationUser.registerUserToFireBase(user);
//                                                         startActivity(new Intent(LoginActivity.this
//                                                                 , UserActivity.class));
//

                                             FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                             firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
                                                 if (task.isSuccessful()) {
                                                     if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                                         ApplicationUser.saveCurrentUser(LoginActivity.this, user);
                                                         ApplicationUser.registerUserToFireBase(user);
                                                         startActivity(new Intent(LoginActivity.this
                                                                 , UserActivity.class));

                                                     } else {
                                                         Toast.makeText(LoginActivity.this, "Xin hãy xác thực email trước khi đăng nhập.",
                                                                 Toast.LENGTH_SHORT).show();
                                                     }

                                                 } else {
                                                     Toast.makeText(getApplicationContext(), "Đã có lỗi ở FireBase!"
                                                             , Toast.LENGTH_SHORT).show();
                                                 }
                                             });
                                         } else {
                                             Toast.makeText(getApplicationContext(), "Thông tin đăng nhập không chính xác!"
                                                     , Toast.LENGTH_SHORT).show();
                                         }
                                     }else {
                                         Toast.makeText(getApplicationContext(), "Thông tin đăng nhập không chính xác!"
                                                 , Toast.LENGTH_SHORT).show();
                                     }
                                 }else {
                                     Toast.makeText(getApplicationContext(), "Đã có lỗi ở server!"
                                             , Toast.LENGTH_SHORT).show();
                                 }
                             }

                             @Override
                             public void onFailure(Call<List<User>> call, Throwable t) {

                             }
                         }
            );
        }
    }




}