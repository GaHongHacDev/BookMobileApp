package com.example.e_commerce.Service;
import com.example.e_commerce.Model.BookType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IBookTypeService {

    String resource = "book_type";


    @GET(resource)
    Call<List<BookType>> getAll();

    @GET(resource + "/{id}")
    Call<BookType> getById(@Path("id") Object id);

    @POST(resource)
    Call<BookType> create(@Body BookType item);

    @PUT(resource + "/{id}")
    Call<BookType> update(@Path("id") Object id, @Body BookType item);

    @DELETE(resource + "/{id}")
    Call<BookType> delete(@Path("id") Object id);
}
