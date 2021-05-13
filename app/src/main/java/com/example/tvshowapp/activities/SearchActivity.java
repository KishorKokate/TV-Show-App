package com.example.tvshowapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshowapp.R;
import com.example.tvshowapp.adapters.TVShowadapetr;
import com.example.tvshowapp.databinding.ActivitySearchBinding;
import com.example.tvshowapp.listeners.TVShowListener;
import com.example.tvshowapp.models.TVShow;
import com.example.tvshowapp.viewmodels.SearchViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TVShowListener {

    private ActivitySearchBinding activitySearchBinding;
    private SearchViewModel viewModel;
    private List<TVShow> tvShow = new ArrayList<>();
    private TVShowadapetr tvShowadapetr;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        doInitialization();
    }

    private void doInitialization() {
        activitySearchBinding.imageBack.setOnClickListener(v -> onBackPressed());

        activitySearchBinding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        tvShowadapetr = new TVShowadapetr(tvShow, this);
        activitySearchBinding.tvShowRecyclerView.setAdapter(tvShowadapetr);
        activitySearchBinding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (timer!=null){
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().trim().isEmpty()){
                    timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    currentPage=1;
                                    totalAvailablePages=1;
                                    searchTVShow(s.toString());
                                }
                            }) ;
                        }
                    },800);
                }else {
                    tvShow.clear();
                    tvShowadapetr.notifyDataSetChanged();
                }
            }
        });

    }

    private void searchTVShow(String query){
        toggleLoading();
        viewModel.searchTVShiw(query,currentPage).observe(this,tvShowResponse -> {
            toggleLoading();
            if (tvShowResponse!=null){
                totalAvailablePages=tvShowResponse.getTotalPages();
                if (tvShowResponse.getTvShows()!=null){
                    int oldCount=tvShow.size();
                    tvShow.addAll(tvShowResponse.getTvShows());
                    tvShowadapetr.notifyItemRangeInserted(oldCount,tvShow.size());
                }
            }
        });

        activitySearchBinding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull  RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activitySearchBinding.tvShowRecyclerView.canScrollVertically(1)){
                    if (!activitySearchBinding.inputSearch.getText().toString().isEmpty()){
                        if (currentPage<totalAvailablePages){
                            currentPage+=1;
                            searchTVShow(activitySearchBinding.inputSearch.getText().toString());
                        }
                    }
                }

            }
        });
        activitySearchBinding.inputSearch.requestFocus();
    }


    private void toggleLoading() {
        if (currentPage == 1) {
            if (activitySearchBinding.getIsLoading() != null && activitySearchBinding.getIsLoading()) {
                activitySearchBinding.setIsLoading(false);
            } else {
                activitySearchBinding.setIsLoading(true);
            }
        } else {
            if (activitySearchBinding.getIsLoadingMore() != null && activitySearchBinding.getIsLoadingMore()) {
                activitySearchBinding.setIsLoadingMore(false);
            } else {
                activitySearchBinding.setIsLoadingMore(true);
            }

        }
    }


    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowdetailActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }
}