package com.example.e_commerce.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commerce.Fragment.CartFragment;
import com.example.e_commerce.Fragment.ManageCategoryFragment;
import com.example.e_commerce.Fragment.ManageProductFragment;
import com.example.e_commerce.Fragment.ProfileFragment;
import com.example.e_commerce.Fragment.SearchFragment;
import com.example.e_commerce.Fragment.UserCategoryFragment;
import com.example.e_commerce.Fragment.UserHomeFragment;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IBookTypeService;
import com.example.e_commerce.Model.BookType;
import com.example.e_commerce.Model.Category;
import com.example.e_commerce.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCategoryActivity extends AppCompatActivity {

    EditText txt_name, txt_image;
    Button btn_show, btn_edit;
    ImageView iv_image;
    IBookTypeService bookTypeService = RepositoryBase.getBookTypeService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        Intent n = getIntent();
        int id =  n.getExtras().getInt("id") ;
        String name =  n.getExtras().getString("name") ;
        String image =  n.getExtras().getString("image") ;

        txt_name = findViewById(R.id.edit_category_txt_name);
        txt_image = findViewById(R.id.edit_category_txt_image);

        btn_show = findViewById(R.id.edit_category_btn_show_image);
        btn_edit = findViewById(R.id.edit_category_btn_edit_category);
        iv_image = findViewById(R.id.edit_category_iv_image);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = txt_image.getText().toString();
                Glide.with(getApplicationContext()).load(url).into(iv_image);
            }
        });

        // TODO: show category data in txt fildes
        Glide.with(getApplicationContext()).load(image).into(iv_image);
        txt_image.setText(image);
        txt_name.setText(name);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: edit category
                String name = txt_name.getText().toString(),
                        image = txt_image.getText().toString();
                BookType bookType = new BookType(id, name, image);

                try{
                    Call<BookType> call = bookTypeService.update(id, bookType);
                    call.enqueue(new Callback<BookType>() {
                        @Override
                        public void onResponse(Call<BookType> call, Response<BookType> response) {
                            if (response.body() != null){
                                Intent myIntent = new Intent(EditCategoryActivity.this, AdminActivity.class);
                                myIntent.putExtra("adminGate",2);
                                EditCategoryActivity.this.startActivity(myIntent);
                            }
                        }
                        @Override
                        public void onFailure(Call<BookType> call, Throwable t) {
                            Toast.makeText(EditCategoryActivity.this, "Save fail"
                            , Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e){
                    Log.d("Error", e.getMessage());
                }
            }
        });

    }
}