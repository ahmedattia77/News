package com.example.news.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.listener.ListenerNewsOnClick;
import com.example.news.R;
import com.example.news.databinding.ActivityMainBinding;
import com.example.news.model.Articles;
import com.example.news.ui.adapter.NewsAdapter;
import com.example.news.ui.viewModel.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListenerNewsOnClick {

    public static final String API_KEY = "dfa5f02ac57741e6a6dd779b8c89a57c" ;
    private ActivityMainBinding binding;
    private List<Articles> list ;
    private NewsAdapter adapter;
    private NewsViewModel newsViewModel;
    private Boolean isOn = false;
    private Boolean searchIsOn = false;
    private AlertDialog alertDialogFilter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        getData("us");
        filter();

    }

    private void initialization (){

        if (list != null){
            list = null;
            adapter = null;
        }

        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        binding.mainRecycle.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new NewsAdapter(list ,this);
        binding.mainRecycle.setAdapter(adapter);
        binding.shimmerLayout.startShimmerAnimation();


    }

    private void getData(String country) {
        initialization();

        newsViewModel.getLiveData(country , "general", null
                , API_KEY).observe(this, newsResponse -> {

            if (newsResponse != null){
                binding.setIsLoading(false);
                if (newsResponse.getArticles() != null){
                    binding.shimmerLayout.stopShimmerAnimation();
                    binding.mainRecycle.setVisibility(ViewGroup.VISIBLE);
                    binding.shimmerLayout.setVisibility(View.GONE);
                    list.addAll(newsResponse.getArticles());
                    adapter.notifyDataSetChanged();
                }
            }

        });

    }

    private void filter(){

        binding.filter.setOnClickListener( v -> setupFilter());


        binding.search.setOnClickListener( v -> startActivity(new Intent(getApplicationContext() ,
                SearchActivity.class)));

    }


    private void setupFilter (){

        if (alertDialogFilter == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.filter_layout
                    , (ViewGroup) findViewById(R.id.filterRoot)
            );

            builder.setView(view);
            alertDialogFilter = builder.create();

            if (alertDialogFilter.getWindow() != null) {
                alertDialogFilter.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                RadioGroup radioGroup = view.findViewById(R.id.radioGroup1);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton checkedButton = view.findViewById(checkedId);
                        String country = checkedButton.getText().toString().toLowerCase();
                        getData(country);
                    }
                });


            }

            alertDialogFilter.show();
            alertDialogFilter = null;
        }


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