package com.example.e_commerce.Service;

import com.example.e_commerce.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IUserService {

    String resource = "user";


    @GET(resource)
    Call<List<User>> getAll();

    @GET(resource + "/{id}")
    Call<User> getById(@Path("id") Object id);

    @POST(resource)
    Call<User> create(@Body User item);

    @PUT(resource + "/{id}")
    Call<User> update(@Path("id") Object id, @Body User item);

    @DELETE(resource + "/{id}")
    Call<User> delete(@Path("id") Object id);

    @GET(resource)
    Call<List<User>> getUserByEmailAndPassword(@Query("email") String email
            , @Query("password") String password);
    @GET(resource)
    Call<List<User>>  checkEmailAvailability(@Query("email") String email);

    @GET(resource)
    Call<List<User>> findUsersByUsername(@Query("username") String username);
}
