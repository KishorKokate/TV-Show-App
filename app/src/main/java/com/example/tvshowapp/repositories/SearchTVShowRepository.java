package com.example.tvshowapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvshowapp.network.ApiClient;
import com.example.tvshowapp.network.ApiService;
import com.example.tvshowapp.response.TVShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTVShowRepository {
    private ApiService apiService;
    public SearchTVShowRepository(){
        apiService= ApiClient.getRetrofit().create(ApiService.class);

    }

    public LiveData<TVShowResponse> searchTVShow(String query,int page){
        MutableLiveData<TVShowResponse> data=new MutableLiveData<>();
        apiService.searchTVShow(query,page).enqueue(new Callback<TVShowResponse>() {
            @Override
            public void onResponse(Call<TVShowResponse> call, Response<TVShowResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TVShowResponse> call, Throwable t) {

                data.setValue(null);
            }
        });
        return data;
    }
}
