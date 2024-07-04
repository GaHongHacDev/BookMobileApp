package com.example.e_commerce.Service;

import com.example.e_commerce.Model.OrderItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IOrderItemService {
    String resource = "order_item";


    @GET(resource)
    Call<List<OrderItem>> getAll();

    @GET(resource + "/{id}")
    Call<OrderItem> getById(@Path("id") Object id);

    @POST(resource)
    Call<OrderItem> create(@Body OrderItem item);

    @PUT(resource + "/{id}")
    Call<OrderItem> update(@Path("id") Object id, @Body OrderItem item);

    @DELETE(resource + "/{id}")
    Call<OrderItem> delete(@Path("id") Object id);
}
