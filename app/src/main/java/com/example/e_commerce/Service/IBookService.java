package com.example.e_commerce.Service;

import com.example.e_commerce.Model.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IBookService {


    String resource = "book";


    @GET(resource)
    Call<List<Book>> getAll();

    @GET(resource + "/{id}")
    Call<Book> getById(@Path("id") Object id);

    @POST(resource)
    Call<Book> create(@Body Book item);

    @PUT(resource + "/{id}")
    Call<Book> update(@Path("id") Object id, @Body Book item);

    @DELETE(resource + "/{id}")
    Call<Book> delete(@Path("id") Object id);

}
