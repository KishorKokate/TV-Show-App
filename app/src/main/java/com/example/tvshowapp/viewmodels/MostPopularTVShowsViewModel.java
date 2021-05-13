package com.example.tvshowapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tvshowapp.repositories.MostPopularTVShowRepositories;
import com.example.tvshowapp.response.TVShowResponse;

public class MostPopularTVShowsViewModel extends ViewModel {
    private MostPopularTVShowRepositories mostPopularTVShowRepositories;

    public MostPopularTVShowsViewModel(){
        mostPopularTVShowRepositories=new MostPopularTVShowRepositories();
    }
    public LiveData<TVShowResponse> getMostPopularTVShows(int page){
        return mostPopularTVShowRepositories.getmostPopularTVShows(page);
    }
}
