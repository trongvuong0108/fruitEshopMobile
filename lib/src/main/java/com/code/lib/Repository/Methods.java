package com.code.lib.Repository;


import com.code.lib.Model.Invoice;
import com.code.lib.Model.bill_model;
import com.code.lib.Model.category;
import com.code.lib.Model.detail_BillModel;
import com.code.lib.Model.googleAccount;
import com.code.lib.Model.jwt;
import com.code.lib.Model.loginRequest;
import com.code.lib.Model.product;
import com.code.lib.Model.userResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Methods {
    @GET("/home/getAll")
    Call<product[]> getProduct();

    @GET("/home/getImgByProduct")
    Call<String[]> getImgs(@Query("name")String name);

    @GET("home/getCate")
    Call<category[]> getCate();

    @GET("home/getByCate")
    Call<product[]> getByCate(@Query("cate") String name);

    @GET("home/find")
    Call<product[]> find(@Query("kw") String kw);

    @POST("home/getSameCate")
    Call<product[]> getSameCate(@Body product product);

    @GET("/account/save")
    Call<String> signup(
            @Query("username") String username,
            @Query("password") String password,
            @Query("fullName") String fullName,
            @Query("email") String email,
            @Query("address") String address,
            @Query("phone") String phone
    );
    @GET("/account/confirmSignUpToken")
    Call<String> confirmToken(
            @Query("username") String username,
            @Query("token") String token
    );

    @POST("/auth")
    Call<jwt> login(@Body loginRequest loginRequest);

    @GET("/account/getUser")
    Call<userResponse> getUser(
            @Query("jwt") String token
    );


    @POST("/bill/checkout")
    Call<String> checkout(
            @Body bill_model bill_model
    );

    @GET("/googleAccount/get")
    Call<googleAccount> googleGet(
            @Query("email") String email
    );

    @GET("/googleAccount/update")
    Call<String> googleUpdate(
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("address") String address
    );

    @GET("/bill/getByEmail")
    Call<Invoice[]> getBillByEmail(
            @Query("name")String name,
            @Query("email")String email);

    @GET("/bill/getById")
    Call<detail_BillModel[]> getBillById(@Query("id")int id);
}
