package com.example.e_commerce.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commerce.Model.BookType;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IBookTypeService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCategoryActivity extends AppCompatActivity {
    EditText txt_name, txt_image;
    Button btn_show, btn_add;
    ImageView iv_image;

    IBookTypeService bookTypeService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        bookTypeService = RepositoryBase.getBookTypeService();

        txt_name = findViewById(R.id.add_category_txt_name);
        txt_image = findViewById(R.id.add_category_txt_image);

        iv_image = findViewById(R.id.add_category_iv_image);

        btn_show = findViewById(R.id.add_category_btn_show_image);
        btn_add = findViewById(R.id.add_category_btn_add);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txt_image.getText().toString().isEmpty()){
                    String url = txt_image.getText().toString();
                    Glide.with(getApplicationContext()).load(url).into(iv_image);
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add categoty code
                String name = txt_name.getText().toString(),
                        image = txt_image.getText().toString();

                if(!name.isEmpty() && !image.isEmpty()){
                    BookType bookType = BookType.getInstance();
                    bookType.setType_name(name);
                    bookType.setImage_url(image);
                    addBookType(bookType);

                    txt_name.setText("");
                    txt_image.setText("");
                    iv_image.setImageResource(R.drawable.image_placeholder);

                    Intent myIntent = new Intent(CreateCategoryActivity.this,AdminActivity.class);
                    myIntent.putExtra("adminGate",3);
                    CreateCategoryActivity.this.startActivity(myIntent);

                }else{
                    Toast.makeText(CreateCategoryActivity.this, "Fill all data first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CreateCategoryActivity.this, AdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("adminGate", 7);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void addBookType(BookType bookType){
        try{
            Call<BookType> call = bookTypeService.create(bookType);

            call.enqueue(new Callback<BookType>() {
                @Override
                public void onResponse(Call<BookType> call, Response<BookType> response) {
                    if (response.body() != null){
                        /*Toast.makeText(MainActivity.this, "Save successfully"
                                , Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this
                                , TraineeListActivity.class);
                        startActivity(intent);*/
                    }
                }
                @Override
                public void onFailure(Call<BookType> call, Throwable t) {
//                    Toast.makeText(AddProductFragment.this, "Save fail"
//                            , Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }
}