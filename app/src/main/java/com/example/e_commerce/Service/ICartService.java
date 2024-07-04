package com.example.e_commerce.Service;

import com.example.e_commerce.Model.Cart;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ICartService {

    String resource = "cart";


    @GET(resource)
    Call<List<Cart>> getAll();

    @GET(resource + "/{id}")
    Call<Cart> getById(@Path("id") Object id);

    @POST(resource)
    Call<Cart> create(@Body Cart item);

    @PUT(resource + "/{id}")
    Call<Cart> update(@Path("id") Object id, @Body Cart item);

    @DELETE(resource + "/{id}")
    Call<Cart> delete(@Path("id") Object id);

}
