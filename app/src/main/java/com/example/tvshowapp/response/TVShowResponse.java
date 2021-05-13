package com.example.tvshowapp.response;

import com.example.tvshowapp.models.TVShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TVShowResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int TotalPages;

    @SerializedName("tv_shows")
    private List<TVShow> tvShows;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return TotalPages;
    }

    public List<TVShow> getTvShows() {
        return tvShows;
    }
}
