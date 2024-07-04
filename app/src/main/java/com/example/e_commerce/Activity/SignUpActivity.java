package com.example.e_commerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Database.Database;
import com.example.e_commerce.Model.User;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IUserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class SignUpActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText txt_username, txt_email, txt_password, txt_fullname, txt_phone_number;
    TextView tv_login;
    Button btn_signup;
    IUserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();
        userService = RepositoryBase.getUserService();

        txt_username = findViewById(R.id.signup_txt_username);
        txt_email = findViewById(R.id.signup_txt_email);
        txt_password = findViewById(R.id.signup_txt_password);
        txt_fullname = findViewById(R.id.signup_txt_fullname);
        txt_phone_number = findViewById(R.id.signup_txt_phone_number);

        tv_login = (TextView) findViewById(R.id.signup_tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        btn_signup = (Button) findViewById(R.id.signup_btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: SignUp

                String username = txt_username.getText().toString().trim(),
                        email = txt_email.getText().toString().trim().toLowerCase(),
                        password = txt_password.getText().toString(),
                        fullname = txt_fullname.getText().toString(),
                        phone_number = txt_phone_number.getText().toString();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || fullname.isEmpty() || phone_number.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Hãy điền hết thông tin!", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập email hợp lệ!", Toast.LENGTH_SHORT).show();
                } else if (!phone_number.matches("^0\\d{9}$")) {
                    Toast.makeText(getApplicationContext(), "Số điện thoại phải bắt đầu bằng số 0 và có độ dài là 10 chữ số!", Toast.LENGTH_SHORT).show();
                } else if (!(password.length() >= 6 && password.matches(".*[A-Z].*") && password.matches(".*\\d.*") && password.matches(".*\\W.*"))) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu phải có độ dài tối thiểu là 6 ký tự, chứa ít nhất 1 chữ cái hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("IUserService", "IUserService");
                    checkEmailAndUsernameAvailability(username, email, password, fullname, phone_number);
                }
            }
        });
    }

    private void checkEmailAndUsernameAvailability(String username, String email, String password, String fullname, String phone_number) {

        Call<List<User>> emailCall = userService.checkEmailAvailability(email);
        Call<List<User>> usernameCall = userService.findUsersByUsername(username);

        emailCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    if (users == null || users.size() == 0) {   //list trung email null
                        usernameCall.enqueue(new Callback<List<User>>() { // check trung username
                            @Override
                            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                if (response.isSuccessful()) {
                                    List<User> users = response.body();
                                    if (users == null || users.size() == 0) {//list trung username null

                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        auth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener((task) -> {

                                                    if (task.isSuccessful()) {
                                                        //send verification link
                                                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
//                                                Cả email và username đều khả dụng, tiến hành đăng ký
                                                                    User inputuser = User.getInstance();
                                                                    inputuser.setFullname(fullname);
                                                                    inputuser.setEmail(email);
                                                                    inputuser.setPhone_number(phone_number);
                                                                    inputuser.setUsername(username);
                                                                    inputuser.setPassword(password);
                                                                    inputuser.setStatus(1);
                                                                    inputuser.setRole("Customer");
                                                                    inputuser.setCreateAt(Calendar.getInstance().getTime());
                                                                    addUser(inputuser);

                                                                } else {
                                                                    Toast.makeText(SignUpActivity.this, "Lỗi đăng ký tài khoản!",
                                                                            Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Username đã tồn tại!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<List<User>> call, Throwable t) {

                            }
                        });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Email đã tồn tại!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    private void addUser(User user) {

        try {
            Call<User> call = userService.create(user);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body() != null) {
                        Toast.makeText(SignUpActivity.this, "Đăng ký thành công. Vui lòng kiểm tra email của bạn."
                                , Toast.LENGTH_LONG).show();
                       Intent intent = new Intent(SignUpActivity.this
                                , LoginActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Save fail"
                            , Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }
}


