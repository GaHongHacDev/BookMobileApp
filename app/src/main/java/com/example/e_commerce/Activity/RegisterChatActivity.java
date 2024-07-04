package com.example.e_commerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_commerce.Activity.message.MainChatActivity;
import com.example.e_commerce.Common.GlobalConfig;
import com.example.e_commerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterChatActivity extends AppCompatActivity {

    EditText name, mobile, email;
    Button btnRegister;

    DatabaseReference databaseReference = FirebaseDatabase
            .getInstance()
            .getReferenceFromUrl(GlobalConfig.REFERENCE_FROM_URL);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_chat);

        binding();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTxt = name.getText().toString();
                String mobileTxt = mobile.getText().toString();
                String emailTxt = email.getText().toString();

                try{

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("users-chat").hasChild(mobileTxt)){
                                Toast.makeText(RegisterChatActivity.this
                                        , "Mobile already exists"
                                        , Toast.LENGTH_LONG).show();

                            }
                            else{
                                databaseReference.child("users-chat").child(mobileTxt)
                                        .child("email").setValue(emailTxt);

                                databaseReference.child("users-chat").child(mobileTxt)
                                        .child("name").setValue(nameTxt);

                                databaseReference.child("users-chat").child(mobileTxt)
                                        .child("profile_pic").setValue("https://cdn.chanhtuoi.com/uploads/2022/01/hinh-avatar-nam-deo-kinh.jpg");

                                Toast.makeText(RegisterChatActivity.this
                                        , "Successfully"
                                        , Toast.LENGTH_LONG).show();



                            }

                            Intent intent = new Intent(RegisterChatActivity.this
                                    , MainChatActivity.class);

                            intent.putExtra("mobile", mobileTxt);
                            intent.putExtra("name", nameTxt);
                            intent.putExtra("email", emailTxt);

                            SharedPreferences preferences = getSharedPreferences("MyPreferencesChat", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("emailChat", emailTxt);
                            editor.putString("mobileChat", mobileTxt);
                            editor.apply();

                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }catch (Exception e){
                    Log.d("DatabaseError", e.getMessage());
                }


            }
        });


    }

    private void binding(){
        name = (EditText) findViewById(R.id.editTextName);
        mobile = (EditText) findViewById(R.id.editTextMobile);
        email = (EditText) findViewById(R.id.editTextEmail);
        btnRegister = (Button) findViewById(R.id.buttonRegister);
    }
}