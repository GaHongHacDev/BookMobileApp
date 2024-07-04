package com.example.e_commerce.Repository;

import com.example.e_commerce.Network.ApiClient;
import com.example.e_commerce.Service.IBookService;
import com.example.e_commerce.Service.IBookTypeService;
import com.example.e_commerce.Service.ICartService;
import com.example.e_commerce.Service.IOrderItemService;
import com.example.e_commerce.Service.IOrderService;
import com.example.e_commerce.Service.IUserService;

public class RepositoryBase {
    public static IBookService getBookService(){
        return ApiClient.getClient().create(IBookService.class);
    }

    public static IUserService getUserService(){
        return ApiClient.getClient().create(IUserService.class);
    }

    public static IBookTypeService getBookTypeService(){
        return ApiClient.getClient().create(IBookTypeService.class);
    }

    public static ICartService getCartService(){
        return ApiClient.getClient().create(ICartService.class);
    }
    public static IOrderService getOrderService(){
        return ApiClient.getClient().create(IOrderService.class);
    }
    public static IOrderItemService getOrderItemService(){
        return ApiClient.getClient().create(IOrderItemService.class);
    }

}
