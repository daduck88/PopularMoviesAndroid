package com.android.popularmoviesapp.rest;

import com.android.popularmoviesapp.data.model.Movie;
import com.android.popularmoviesapp.data.model.Review;
import com.android.popularmoviesapp.rest.responses.ResponseMovieVideos;
import com.android.popularmoviesapp.rest.responses.ResponsePaginated;

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
    public Call<ResponsePaginated<Movie>> getMovies(String filter) {
        return apiService.getMovies(filter);
    }

    public Call<ResponseMovieVideos> getMovieVideos(String movieID) {
        return apiService.getMovieVideos(movieID);
    }

    public Call<ResponsePaginated<Review>> getMovieReviews(String movieID) {
        return apiService.getMovieReviews(movieID);
    }
}
