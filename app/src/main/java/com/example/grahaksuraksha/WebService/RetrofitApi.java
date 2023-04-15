package com.example.grahaksuraksha.WebService;

import com.example.grahaksuraksha.Models.FraudCheckRequest;
import com.example.grahaksuraksha.Models.FraudCheckResponse;
import com.example.grahaksuraksha.Models.ReportModel;
import com.example.grahaksuraksha.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApi {
        @POST("/auth/signup")
        Call<User> register(@Body User user);

        @POST("/auth/signin")
        Call<User> login(@Body User user);

        @POST("/fraud")
        Call<FraudCheckResponse> fraudCheck(@Body FraudCheckRequest req);

        @POST("/report")
        Call<Void> submitReport(@Body ReportModel reportModel);

}
