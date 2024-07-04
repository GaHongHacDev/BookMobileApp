package com.example.e_commerce.Fragment;

import android.content.Intent;
import android.os.Bundle;

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
import com.example.e_commerce.Activity.CreateCategoryActivity;
import com.example.e_commerce.Activity.EditCategoryActivity;
import com.example.e_commerce.Model.BookType;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IBookTypeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageCategoryFragment extends Fragment {

    ListView manage_category_list;
    ArrayList<BookType> bookTypes = new ArrayList<>();
    Button btn_create_category;

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private IBookTypeService bookTypeService = RepositoryBase.getBookTypeService();

    public ManageCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageCategoryFragment.
     */
    // Rename and change types and number of parameters
    public static ManageCategoryFragment newInstance(String param1, String param2) {
        ManageCategoryFragment fragment = new ManageCategoryFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manage_category, container, false);

        manage_category_list = v.findViewById(R.id.manage_category_list);
        btn_create_category = v.findViewById(R.id.btn_create_category);
        // TODO: get categories from database and show it in listView
        getAllBookType();
        btn_create_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateCategoryActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

    private void getAllBookType(){

        bookTypes.clear();

        try{
            Call<List<BookType>> call = bookTypeService.getAll();
            call.enqueue(new Callback<List<BookType>>() {
                @Override
                public void onResponse(Call<List<BookType>> call, Response<List<BookType>> response) {
                    List<BookType> resList = response.body();
                    if (resList == null){
                        return;
                    }
                    Collections.reverse(resList);
                    for (BookType bookType : resList){
                        bookTypes.add(bookType);
                    }

                    ManageCategoryFragment.AdminCategoryAdapter adminCategoryAdapter
                            = new ManageCategoryFragment.AdminCategoryAdapter(bookTypes);
                    manage_category_list.setAdapter(adminCategoryAdapter);
                }
                @Override
                public void onFailure(Call<List<BookType>> call, Throwable t) {

                }
            });

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }

    class AdminCategoryAdapter extends BaseAdapter {

        // This adapter will used in user home and search.

        ArrayList<BookType> bookTypes = new ArrayList<>();

        public AdminCategoryAdapter(ArrayList<BookType> bookTypes) {
            this.bookTypes = bookTypes;
        }

        @Override
        public int getCount() {
            return bookTypes.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return bookTypes.get(i).getType_name();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View item = layoutInflater.inflate(R.layout.admin_category_item, null);

            ImageView category_image = (ImageView) item.findViewById(R.id.admin_category_iv_image);
            TextView category_name = (TextView) item.findViewById(R.id.admin_category_tv_name);

            Button btn_edit = item.findViewById(R.id.admin_category_btn_edit);
            ImageButton btn_del= item.findViewById(R.id.admin_category_btn_delete);


            category_name.setText(bookTypes.get(i).getType_name());
            String url = bookTypes.get(i).getImage_url();
            Glide.with(getContext()).load(url).into(category_image);

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getBaseContext(), EditCategoryActivity.class);
                    intent = new Intent(getActivity(),EditCategoryActivity.class);

                    intent.putExtra("id",bookTypes.get(i).getId());
                    intent.putExtra("name",bookTypes.get(i).getType_name());
                    intent.putExtra("image",bookTypes.get(i).getImage_url());
                    getActivity().startActivity(intent);
                }
            });

            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bookTypes.get(i).getQuantity() == 0) {
                        try {
                            Call<BookType> call = bookTypeService.delete(bookTypes.get(i).getId());
                            call.enqueue(new Callback<BookType>() {
                                @Override
                                public void onResponse(Call<BookType> call, Response<BookType> response) {
                                    if (response.body() != null) {
                                        Toast.makeText(getActivity(), "Xóa thể loại thành công", Toast.LENGTH_SHORT).show();
                                        getAllBookType();
                                    }
                                }
                                @Override
                                public void onFailure(Call<BookType> call, Throwable t) {
                                    //                    Toast.makeText(AddProductFragment.this, "Save fail"
                                    //                            , Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (Exception e) {
                            Log.d("Error", e.getMessage());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Không thể xóa! Có sách thuộc thể loại này", Toast.LENGTH_LONG).show();
                        getAllBookType();
                    }
                }
            });

            return item;
        }
    }
}