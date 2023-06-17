package com.example.news.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.news.R;
import com.example.news.databinding.ActivityMainBinding;
import com.example.news.model.Articles;
import com.example.news.model.Source;
import com.example.news.response.NewsResponse;
import com.example.news.ui.adapter.NewsAdapter;
import com.example.news.ui.viewModel.NewsViewModel;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "dfa5f02ac57741e6a6dd779b8c89a57c" ;
    private ActivityMainBinding binding;
    private List<Articles> list = new ArrayList<>();
    private NewsAdapter adapter;
    private NewsViewModel newsViewModel;
    private Context context ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);


        initialization();

    }

    private void initialization() {
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        binding.mainRecycle.setHasFixedSize(true);
        adapter = new NewsAdapter(list);
        binding.mainRecycle.setAdapter(adapter);


        binding.setIsLoading(true);
        newsViewModel.getLiveData("us" , "business", null
                , API_KEY).observe(this, newsResponse -> {

            if (newsResponse != null){
                if (newsResponse.getArticles() != null){
                    binding.setIsLoading(false);
                    list.addAll(newsResponse.getArticles());
                    adapter.notifyDataSetChanged();
                }
            }

        });

    }



}