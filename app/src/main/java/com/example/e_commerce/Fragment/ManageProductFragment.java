package com.example.e_commerce.Fragment;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commerce.Activity.CreateProductActivity;
import com.example.e_commerce.Activity.EditProductActivity;
import com.example.e_commerce.Common.ParseHelper;
import com.example.e_commerce.Model.Book;
import com.example.e_commerce.Model.BookType;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IBookService;
import com.example.e_commerce.Service.IBookTypeService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageProductFragment extends Fragment {

    ListView list_products;
    FloatingActionButton btn_manage_product_add;
    ArrayList<Book> products = new ArrayList<>();
    ArrayList<BookType> bookTypes = new ArrayList<>();
    Button btn_create_product;

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private IBookService bookService = RepositoryBase.getBookService();
    private IBookTypeService bookTypeService = RepositoryBase.getBookTypeService();

    public ManageProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageProductFragment.
     */
    // Rename and change types and number of parameters
    public static ManageProductFragment newInstance(String param1, String param2) {
        ManageProductFragment fragment = new ManageProductFragment();
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
        View v = inflater.inflate(R.layout.fragment_manage_product, container, false);

        list_products = v.findViewById(R.id.manage_product_list);
        btn_create_product = v.findViewById(R.id.btn_create_product);
        btn_create_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateProductActivity.class);
                startActivity(intent);
            }
        });

        //fill_list();
        getAllBook();
        return v;
    }

    private void getAllBook(){
        products.clear();
        try{
            Call<List<Book>> call = bookService.getAll();
            call.enqueue(new Callback<List<Book>>() {
                @Override
                public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                    List<Book> books = response.body();
                    if (books == null){
                        return;
                    }
                    Collections.reverse(books);
                    for (Book book : books){
                        products.add(book);
                    }
                    ManageProductFragment.AdminManageProductAdapter adminManageProductAdapter
                            = new ManageProductFragment.AdminManageProductAdapter(products);
                    list_products.setAdapter(adminManageProductAdapter);
                }

                @Override
                public void onFailure(Call<List<Book>> call, Throwable t) {

                }
            });

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }

    class AdminManageProductAdapter extends BaseAdapter {

        ArrayList<Book> products = new ArrayList<>();

        public AdminManageProductAdapter(ArrayList<Book> products) {
            this.products = products;
        }

        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return products.get(i).getTitle();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View item = layoutInflater.inflate(R.layout.admin_product_item, null);

            ImageView product_image = (ImageView) item.findViewById(R.id.admin_product_iv_product_image);
            TextView product_name = (TextView) item.findViewById(R.id.admin_tv_product_name);
            TextView product_price = (TextView) item.findViewById(R.id.admin_product_tv_product_price);
            ImageButton btn_del = item.findViewById(R.id.admin_product_btn_delete);
            Button btn_edit = item.findViewById(R.id.admin_product_btn_edit);

            String strPrice = ParseHelper.intToString(products.get(i).getPrice());
            product_price.setText("Giá: " + strPrice);
            product_name.setText(products.get(i).getTitle());
            //product_price.setText("Giá: " + products.get(i).getPrice() + "");
            String url = products.get(i).getImage_url();
            Glide.with(getContext()).load(url).into(product_image);

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getBaseContext(), EditProductActivity.class);
                    intent = new Intent(getActivity(), EditProductActivity.class);

                    intent.putExtra("id", products.get(i).getId());
                    intent.putExtra("stock_quantity", products.get(i).getStock_quantity());
                    intent.putExtra("book_type_id", products.get(i).getBook_type_id());
                    intent.putExtra("title", products.get(i).getTitle());
                    intent.putExtra("price", products.get(i).getPrice());
                    intent.putExtra("description", products.get(i).getDescription());
                    intent.putExtra("author", products.get(i).getAuthor());
                    intent.putExtra("image_url", products.get(i).getImage_url());
                    intent.putExtra("book_type_id", products.get(i).getBook_type_id());

                    getActivity().startActivity(intent);
                }
            });

            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //--------DELETE book------------
                    try{
                        Call<Book> call = bookService.delete(products.get(i).getId());
                        call.enqueue(new Callback<Book>() {
                            @Override
                            public void onResponse(Call<Book> call, Response<Book> response) {
                                if (response.body() != null){
                                    Toast.makeText(getActivity(),"Xóa sách thành công",Toast.LENGTH_SHORT).show();
                                    getAllBook();
                                }
                            }
                            @Override
                            public void onFailure(Call<Book> call, Throwable t) {
//                    Toast.makeText(AddProductFragment.this, "Save fail"
//                            , Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e){
                        Log.d("Error", e.getMessage());
                    }

                    //------GET quantity BookType--------------
                    try{
                        Call<List<BookType>> callBT = bookTypeService.getAll();
                        callBT.enqueue(new Callback<List<BookType>>() {
                            @Override
                            public void onResponse(Call<List<BookType>> call, Response<List<BookType>> response) {
                                List<BookType> resList = response.body();
                                if (resList == null){
                                    return;
                                }
                                int bookType_Quantity = 0;
                                for (BookType item : resList){
                                    if(item.getId() == products.get(i).getId()){
                                        bookType_Quantity = item.getQuantity();
                                        break;
                                    }
                                }
                                //--------Decrease quantity bookType------------
                                BookType bookType = new BookType(products.get(i).getId(), bookType_Quantity);
                                try{
                                    Call<BookType> callBTU = bookTypeService.update(products.get(i).getId(), bookType );
                                    callBTU.enqueue(new Callback<BookType>() {
                                        @Override
                                        public void onResponse(Call<BookType> call, Response<BookType> response) {
                                            BookType resList = response.body();
                                            if (resList == null){
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<BookType> call, Throwable t) {
                                        }
                                    });
                                } catch (Exception e){
                                    Log.d("Error", e.getMessage());
                                }
                                //--------END decrease quantity bookType------------
                            }
                            @Override
                            public void onFailure(Call<List<BookType>> call, Throwable t) {
                            }
                        });

                    } catch (Exception e){
                        Log.d("Error", e.getMessage());
                    }
                    //------END get quantity BookType--------------
                }
                //--------END delete book------------
            });
            return item;
        }
    }

}