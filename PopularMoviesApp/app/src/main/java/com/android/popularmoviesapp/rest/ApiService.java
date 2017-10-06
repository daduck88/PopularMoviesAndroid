package com.android.popularmoviesapp.rest;

import com.android.popularmoviesapp.data.model.Movie;
import com.android.popularmoviesapp.data.model.Review;
import com.android.popularmoviesapp.rest.responses.ResponseMovieVideos;
import com.android.popularmoviesapp.rest.responses.ResponsePaginated;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Daduck on 7/26/17.
 */

public interface ApiService {

    @GET("movie/{filter}")
    Call<ResponsePaginated<Movie>> getMovies(@Path("filter") String  filter);

    @GET("movie/{movieID}/videos")
    Call<ResponseMovieVideos> getMovieVideos(@Path("movieID") String  movieID);

    @GET("movie/{movieID}/reviews")
    Call<ResponsePaginated<Review>> getMovieReviews(@Path("movieID") String  movieID);
}
