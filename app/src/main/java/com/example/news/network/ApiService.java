package com.example.news.network;

import com.example.news.response.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("top-headlines")
    Call <NewsResponse> getNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("q") String q,
            @Query("apiKey") String api_key
    );

}