package com.example.grahaksuraksha.WebService;

import com.example.grahaksuraksha.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApi {
        @POST("/register")
        Call<User> register(@Body User user);

        @POST("/login")
        Call<User> login(@Query("email") String email, @Query("password") String password);

        @POST("/checkUpi")
        Call<Boolean> fraudCheck(@Body String upiid);

}
