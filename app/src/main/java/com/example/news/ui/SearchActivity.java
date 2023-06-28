package com.example.news.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.listener.ListenerNewsOnClick;
import com.example.news.R;
import com.example.news.databinding.ActivitySearchBinding;
import com.example.news.model.Articles;
import com.example.news.response.NewsResponse;
import com.example.news.ui.adapter.NewsAdapter;
import com.example.news.ui.viewModel.NewsViewModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity  implements ListenerNewsOnClick {
    private NewsViewModel newsViewModel;
    private ArrayList <Articles> articlesArrayList;
    private NewsAdapter newsAdapter;
    private Timer timer;

    private ActivitySearchBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_search);


        binding.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null)
                    timer.cancel();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()){
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                getData(s.toString());
                            });
                        }
                    }, 800);
                }
                else {
                    articlesArrayList.clear();
                    newsAdapter.notifyDataSetChanged();
                }
            }
        });

        binding.backSpace.setOnClickListener( v -> onBackPressed());

    }

    private void initialData() {
        binding.searchInput.requestFocus();
        binding.searchRecycle.setHasFixedSize(true);
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        articlesArrayList = new ArrayList<>();
        newsAdapter = new NewsAdapter(articlesArrayList, this);
        binding.searchRecycle.setAdapter(newsAdapter);
    }

    private void getData(String searchQuery){
        initialData();

        newsViewModel.getLiveData("us", "general" ,null ,"dfa5f02ac57741e6a6dd779b8c89a57c")
                .observe(this, new Observer<NewsResponse>() {
                    @Override
                    public void onChanged(NewsResponse newsResponse) {
                        if (newsResponse != null){
                            if (newsResponse.getArticles() != null){
                                articlesArrayList.addAll(newsResponse.getArticles());
                                newsAdapter.notifyDataSetChanged();
                                binding.animationSearch.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    @Override
    public void OnNewsClick(Articles articles) {
        Intent intent = new Intent(getApplicationContext() , NewsDetailsActivity.class);

        intent.putExtra("image" , articles.getUrlToImage());
        intent.putExtra("name" , articles.getSource().getName());
        intent.putExtra("description" , articles.getDescription());
        intent.putExtra("title" , articles.getTitle());
        intent.putExtra("content" , articles.getContent());
        intent.putExtra("publishedAt" , articles.getPublishedAt());
        intent.putExtra("url" , articles.getUrl());

        startActivity(intent);
    }

    @Override
    public void onShareClick(Articles articles) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        share.putExtra(Intent.EXTRA_SUBJECT, articles.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, articles.getUrl());

        startActivity(Intent.createChooser(share, "Share link!"));

    }
}