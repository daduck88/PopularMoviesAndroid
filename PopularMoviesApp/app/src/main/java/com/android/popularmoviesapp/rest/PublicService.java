package com.android.popularmoviesapp.rest;

import com.android.popularmoviesapp.rest.responses.ResponseMovies;

import retrofit2.Call;

/**
 * Created by Daduck on 7/26/17.
 */

public class PublicService {

    private ApiService apiService;

    public PublicService(ApiService apiService) {
        this.apiService = apiService;

    }

    /**
     * Get the movies to show with the sortBy to define the list of movies that you want.
     */
    public Call<ResponseMovies> getMovies(String filter) {
        return apiService.getMovies(filter);
    }
}
