package com.example.news.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.databinding.MainContainerBinding;
import com.example.news.model.Articles;
import com.example.news.model.Source;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsView> {

    private List<Articles> articlesList;
    private LayoutInflater layoutInflater;


    public NewsAdapter(List<Articles> articlesList) {
        this.articlesList = articlesList;
    }

    @NonNull
    @Override
    public NewsView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        MainContainerBinding binding = DataBindingUtil.inflate(layoutInflater
        , R.layout.main_container ,parent , false);

        return new NewsView(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsView holder, int position) {
        holder.bindArticle(articlesList.get(position));
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    class NewsView extends RecyclerView.ViewHolder {
        private MainContainerBinding binding;

        public NewsView(MainContainerBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindArticle (Articles article){
            binding.setArticle(article);
            binding.executePendingBindings();
        }

    }

}
