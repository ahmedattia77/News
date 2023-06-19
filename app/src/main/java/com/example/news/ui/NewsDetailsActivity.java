package com.example.news.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.system.Os;
import android.view.View;

import com.example.news.R;
import com.example.news.databinding.ActivityNewsBinding;
import com.example.news.model.Articles;

public class NewsDetailsActivity extends AppCompatActivity {

    private ActivityNewsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this  ,R.layout.activity_news);

        initial();


    }

    private void initial (){
//        Articles articles = (Articles) getIntent().getSerializableExtra("articles");
//        binding.setArticles(articles);

        binding.setImage(getIntent().getStringExtra("image"));
        binding.setAuthor(getIntent().getStringExtra("name"));
        binding.setTitle(getIntent().getStringExtra("title"));
        binding.setDes(getIntent().getStringExtra("description"));
        binding.setContent(getIntent().getStringExtra("content"));
        binding.setPublishAt(getIntent().getStringExtra("publishedAt"));
        binding.setUrl(getIntent().getStringExtra("url"));

        binding.url.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getIntent().getStringExtra("url")));
            startActivity(intent);
        });

        binding.backSpace.setOnClickListener(v -> onBackPressed());

    }


}