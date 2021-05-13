package com.example.tvshowapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshowapp.repositories.SearchTVShowRepository;
import com.example.tvshowapp.response.TVShowResponse;

public class SearchViewModel extends ViewModel {
    private SearchTVShowRepository searchTVShowRepository;
    public SearchViewModel(){
        searchTVShowRepository=new SearchTVShowRepository();
    }
    public LiveData<TVShowResponse> searchTVShiw(String query,int page){
        return searchTVShowRepository.searchTVShow(query, page);
    }
}
