package com.example.e_commerce.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.e_commerce.Activity.CategoryProductsActivity;
import com.example.e_commerce.Database.Database;
import com.example.e_commerce.Model.Book;
import com.example.e_commerce.Model.BookType;
import com.example.e_commerce.Model.Category;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IBookTypeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserCategoryFragment extends Fragment {

    GridView list_categories;

    ArrayList<BookType> bookTypes = new ArrayList<>();
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private IBookTypeService bookTypeService = RepositoryBase.getBookTypeService();

    public UserCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // Rename and change types and number of parameters
    public static UserCategoryFragment newInstance(String param1, String param2) {
        UserCategoryFragment fragment = new UserCategoryFragment();
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
        View v = inflater.inflate(R.layout.fragment_user_category, container, false);

        list_categories = v.findViewById(R.id.user_category_list_categories);

        // TODO: get categories from database and show it in listView
        getAllBookType();

        list_categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity().getBaseContext(), CategoryProductsActivity.class);
                intent = new Intent(getActivity(), CategoryProductsActivity.class);
                intent.putExtra("id",bookTypes.get(i).getId());
                getActivity().startActivity(intent);
            }
        });

        return v;
    }

    private void getAllBookType(){
        try{
            Call<List<BookType>> call = bookTypeService.getAll();
            call.enqueue(new Callback<List<BookType>>() {
                @Override
                public void onResponse(Call<List<BookType>> call, Response<List<BookType>> response) {
                    List<BookType> resList = response.body();
                    if (resList == null){
                        return;
                    }

                    for (BookType bookType : resList){
                        if (bookType.getQuantity() > 0)
                        bookTypes.add(bookType);
                    }
                    UserCategoryFragment.UserCategoryAdapter userCategoryAdapter
                            = new UserCategoryFragment.UserCategoryAdapter(bookTypes);
                    list_categories.setAdapter(userCategoryAdapter);
                }

                @Override
                public void onFailure(Call<List<BookType>> call, Throwable t) {
                }
            });

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }
    class UserCategoryAdapter extends BaseAdapter {

        // This adapter will used in user home and search.

        ArrayList<BookType> bookTypes = new ArrayList<>();

        public UserCategoryAdapter(ArrayList<BookType> bookType) {
            this.bookTypes = bookType;
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
            View item = layoutInflater.inflate(R.layout.user_category_item, null);

            ImageView category_image = (ImageView) item.findViewById(R.id.user_category_iv_category_image);
            TextView category_name = (TextView) item.findViewById(R.id.user_category_tv_category_name);
            TextView category_quantity = (TextView) item.findViewById(R.id.user_category_tv_category_quantity);

            category_name.setText(bookTypes.get(i).getType_name());
            category_quantity.setText(bookTypes.get(i).getQuantity() + " sản phẩm");
            String url = bookTypes.get(i).getImage_url();
            Glide.with(getContext()).load(url).into(category_image);

            return item;
        }
    }

}