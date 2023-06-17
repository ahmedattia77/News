package com.example.news.network;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClint {
    private Context context;
    private static String BASE_URL = "https://newsapi.org/v2/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofit (){
        if (retrofit == null){

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
