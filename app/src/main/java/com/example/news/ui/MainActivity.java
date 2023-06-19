package com.example.news.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.listener.ListenerNewsOnClick;
import com.example.news.R;
import com.example.news.databinding.ActivityMainBinding;
import com.example.news.model.Articles;
import com.example.news.ui.adapter.NewsAdapter;
import com.example.news.ui.viewModel.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListenerNewsOnClick {

    private static final String API_KEY = "dfa5f02ac57741e6a6dd779b8c89a57c" ;
    private ActivityMainBinding binding;
    private List<Articles> list = new ArrayList<>();
    private NewsAdapter adapter;
    private NewsViewModel newsViewModel;
    private Context context ;
    private Boolean isOn = false;
    private Boolean searchIsOn = false;
    private String category = "general";
    private String country = "us";
    private String searchQuery = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

       initialization();
    }

    private void initialization() {

        filter();
        setCategory();
        setCountry();
        searchSetup();

        list.clear();
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        binding.mainRecycle.setHasFixedSize(true);
        adapter = new NewsAdapter(list ,this);
        binding.mainRecycle.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        binding.setIsLoading(true);
        newsViewModel.getLiveData(country , category, searchQuery
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

    private void filter(){

        binding.filter.setImageResource(R.drawable.baseline_filter_list_off_24);

        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOn){
                    binding.filter.setImageResource(R.drawable.filter_list_24);
                    binding.listView.setVisibility(View.VISIBLE);
                    binding.countryList.setVisibility(View.VISIBLE);
                    isOn = true;
                }else{
                    binding.filter.setImageResource(R.drawable.baseline_filter_list_off_24);
                    binding.listView.setVisibility(View.GONE);
                    binding.countryList.setVisibility(View.GONE);
                    isOn = false;
                }
            }
        });


        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchIsOn)
                    binding.searchInput.setVisibility(View.VISIBLE);
                else
                    binding.searchInput.setVisibility(View.GONE);
            }
        });



    }

    private void setCategory (){

            binding.businessBt.setOnClickListener(v -> {
                category = "business";
                initialization();
            });
            binding.entertainmentBt.setOnClickListener(v -> {
                category = "entertainment";
                initialization();
            });

            binding.generalBt.setOnClickListener(v -> {
                category = "general";
                initialization();
            });
            binding.healthBt.setOnClickListener(v -> {
                category = "health";
                initialization();
            });
            binding.scienceBt.setOnClickListener(v -> {
                category = "science";
                initialization();
            });
            binding.sportsBt.setOnClickListener(v -> {
                category = "sports";
                initialization();
            });
            binding.technologyBt.setOnClickListener(v -> {
                category = "technology";
                initialization();
            });

    }


    private void setCountry(){

        binding.usBt.setOnClickListener(v -> {
            country = "us";
            initialization();
        });
        binding.brBt.setOnClickListener(v -> {
            country = "br";
            initialization();
        });
        binding.itBt.setOnClickListener(v -> {
            country = "it";
            initialization();
        });
        binding.uaBt.setOnClickListener(v -> {
            country = "ua";
            initialization();
        });

    }

    private void searchSetup(){
        binding.searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText.toString().toLowerCase();
                initialization();
                return false;
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
//        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, articles.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, articles.getUrl());

        startActivity(Intent.createChooser(share, "Share link!"));

    }
}