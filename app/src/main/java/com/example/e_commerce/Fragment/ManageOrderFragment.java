package com.example.e_commerce.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Activity.AdminDetailOrderItemActivity;
import com.example.e_commerce.Common.ParseHelper;
import com.example.e_commerce.Model.Order;
import com.example.e_commerce.R;
import com.example.e_commerce.Repository.RepositoryBase;
import com.example.e_commerce.Service.IOrderService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageOrderFragment extends Fragment {
    IOrderService orderService  = RepositoryBase.getOrderService();
    ArrayList<Order> orders = new ArrayList<Order>();
    Spinner spinner_order_status;
    ListView list_order;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageOrderFragment newInstance(String param1, String param2) {
        ManageOrderFragment fragment = new ManageOrderFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_manage_order, container, false);
        spinner_order_status = v.findViewById(R.id.spinner_order_status);
        list_order = v.findViewById(R.id.manage_order_list);
        spinner_order_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                if (i == 1) {
                    getAllOrder("Chờ xác nhận");
                } else if(i == 2) {
                    getAllOrder("Đã xác nhận");
                } else if(i == 3) {
                    getAllOrder("Đang giao hàng");
                } else if(i == 4) {
                    getAllOrder("Giao hàng thành công");
                } else if(i == 5) {
                    getAllOrder("Giao hàng không thành công");
                } else if(i == 6) {
                    getAllOrder("Hủy đơn");
                } else {
                    getAllOrder("Tất cả");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });
        return v;
    }
    private void getAllOrder(String status){
        orders.clear();
        try{
            Call<List<Order>> call = orderService.getAll();
            call.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    List<Order> resList = response.body();
                    if (resList == null){
                        return;
                    }
                    for (Order item : resList){
                        if (status.equals("Tất cả"))
                            orders.add(item);
                        else if(item.getStatus().equals(status))
                            orders.add(item);
                    }
                    ManageOrderAdapter adapter = new ManageOrderAdapter(orders);
                    list_order.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {

                }
            });

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }
    class ManageOrderAdapter extends BaseAdapter {

        ArrayList<Order> orders = new ArrayList<>();

        public ManageOrderAdapter (ArrayList<Order> orders) {
            this.orders = orders;
        }

        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return orders.get(i).getOrder_id();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View item = layoutInflater.inflate(R.layout.admin_manage_order_item, null);

            TextView product_status = (TextView) item.findViewById(R.id.admin_manage_order_status);
            TextView product_price = (TextView) item.findViewById(R.id.admin_manage_order_price);
            TextView product_create_at = (TextView) item.findViewById(R.id.admin_manage_order_date);
            TextView product_address= (TextView) item.findViewById(R.id.admin_manage_order_address);
            Button btn_detail = (Button) item.findViewById(R.id.btn_detail_order);

            String createdAt = ParseHelper.dateTimeToString(orders.get(i).getCreated_at());
            product_create_at.setText(createdAt);
            String price = ParseHelper.intToString(orders.get(i).getTotal_amount());
            product_price.setText(price);
            //product_create_at.setText(orders.get(i).getCreated_at() + "");
            product_status.setText(orders.get(i).getStatus());
            product_address.setText(orders.get(i).getAddress());
            //product_price.setText(orders.get(i).getTotal_amount() + "");

           if (orders.get(i).getStatus().equals("Chờ xác nhận")){
                product_status.setTextColor(Color.rgb(189, 132, 0));
            } else if (orders.get(i).getStatus().equals("Đã xác nhận")){
                product_status.setTextColor(Color.rgb(26, 171, 152));
            } else if (orders.get(i).getStatus().equals("Đang giao hàng")){
               product_status.setTextColor(Color.rgb(12, 69, 62));
           }
            btn_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getBaseContext(), AdminDetailOrderItemActivity.class);
                    intent = new Intent(getActivity(), AdminDetailOrderItemActivity.class);
                    intent.putExtra("id",orders.get(i).getOrder_id());
                    intent.putExtra("price",orders.get(i).getTotal_amount());
                    intent.putExtra("create_at",orders.get(i).getCreated_at());
                    intent.putExtra("status",orders.get(i).getStatus());
                    intent.putExtra("address",orders.get(i).getAddress());
                    getActivity().startActivity(intent);
//
                    Toast.makeText(getContext(), "CHAY", Toast.LENGTH_SHORT).show();
                }
            });

            return item;
        }
    }
}