package com.example.tvshowapp.response;

import com.example.tvshowapp.models.TVShow;
import com.example.tvshowapp.models.TVShowDetails;
import com.google.gson.annotations.SerializedName;

public class TVShowDetailsResponse {

    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;

    public TVShowDetails getTvShowDetails(){
        return tvShowDetails;
    }
}
