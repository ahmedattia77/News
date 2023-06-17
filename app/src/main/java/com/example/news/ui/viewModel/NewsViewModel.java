package com.example.news.ui.viewModel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.news.repository.NewsRepository;
import com.example.news.response.NewsResponse;


public class NewsViewModel extends ViewModel {

    private NewsRepository newsRepository;


    public NewsViewModel() {
        newsRepository = new NewsRepository();
    }

    public LiveData<NewsResponse> getLiveData (String country , String category
                    , String searchQuery , String api_key){

        return newsRepository.newResponseLiveData(country, category
                , searchQuery, api_key);
    }

}
