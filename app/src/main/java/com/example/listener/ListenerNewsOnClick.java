package com.example.listener;

import androidx.annotation.StringDef;

import com.example.news.model.Articles;

public interface ListenerNewsOnClick {
    void OnNewsClick(Articles articles);

    void onShareClick (Articles articles);

}
