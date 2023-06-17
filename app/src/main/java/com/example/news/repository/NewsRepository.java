package com.example.news.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.news.network.ApiClint;
import com.example.news.network.ApiService;
import com.example.news.response.NewsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {

    private ApiService apiService;
    private MutableLiveData<NewsResponse> mutableLiveData = new MutableLiveData<>();


    public NewsRepository (){
        apiService = ApiClint.getRetrofit().create(ApiService.class);
    }

    public LiveData<NewsResponse> newResponseLiveData (
            String country , String category ,String searchQuery ,String api_key) {


            apiService.getNews(country, category
                    , searchQuery, api_key).enqueue(new Callback<NewsResponse>() {
                @Override
                public void onResponse(@NonNull Call<NewsResponse> call,@NonNull Response<NewsResponse> response) {
                    mutableLiveData.setValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<NewsResponse> call, Throwable t) {
                    mutableLiveData.setValue(null);
                }
            });

        return mutableLiveData;
    }

}
