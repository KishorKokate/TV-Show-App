package com.example.tvshowapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.tvshowapp.database.TVShowDatabase;
import com.example.tvshowapp.models.TVShow;
import com.example.tvshowapp.repositories.TVShowDetailsRepository;
import com.example.tvshowapp.response.TVShowDetailsResponse;

import io.reactivex.Completable;

public class TVShowDetailsViewModel extends ViewModel {
    private TVShowDetailsRepository tvShowDetailsRepository;
    private TVShowDatabase tvShowDatabase;

    public  TVShowDetailsViewModel(){

        tvShowDetailsRepository=new TVShowDetailsRepository();
       // tvShowDatabase=TVShowDatabase.getTvShowDatabase();
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId){
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }
    public Completable addToWatchList(TVShow tvShow){
        return tvShowDatabase.tvShowDao().addToWatchList(tvShow);
    }
}
