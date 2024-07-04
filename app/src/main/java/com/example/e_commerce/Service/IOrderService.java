package com.example.e_commerce.Service;

import com.example.e_commerce.Model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IOrderService {
    String resource = "order";


    @GET(resource)
    Call<List<Order>> getAll();

    @GET(resource + "/{id}")
    Call<Order> getById(@Path("id") Object id);

    @POST(resource)
    Call<Order> create(@Body Order item);

    @PUT(resource + "/{id}")
    Call<Order> update(@Path("id") Object id, @Body Order item);

    @DELETE(resource + "/{id}")
    Call<Order> delete(@Path("id") Object id);
}
