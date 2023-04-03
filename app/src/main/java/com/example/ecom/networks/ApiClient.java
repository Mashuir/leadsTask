package com.example.ecom.networks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static Retrofit retrofit = null;

    public static ApiService getClient(){

        if (retrofit == null){

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();

            Gson gson = new GsonBuilder().create();

            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://fakestoreapi.com/products/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        }

        return retrofit.create(ApiService.class);
    }

}
