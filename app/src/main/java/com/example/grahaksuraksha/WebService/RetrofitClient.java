package com.example.grahaksuraksha.WebService;

import com.example.grahaksuraksha.Utility.UtilService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
  private static RetrofitApi retrofitApi;
    //   private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .connectTimeout(1, TimeUnit.MINUTES)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(25, TimeUnit.SECONDS)
//            .build();
    public static RetrofitApi getRetrofitApiService() {
        if(retrofitApi == null ) {
           // this is the retrofit client
           Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(UtilService.BASE_URL)
                    // .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
           retrofitApi = retrofit.create(RetrofitApi.class);
        }
        return retrofitApi;
    }

}
