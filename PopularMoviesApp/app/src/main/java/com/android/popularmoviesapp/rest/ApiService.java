package com.android.popularmoviesapp.rest;

import com.android.popularmoviesapp.rest.responses.ResponseMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Daduck on 7/26/17.
 */

public interface ApiService {

    @GET("movie/{filter}")
    Call<ResponseMovies> getMovies(@Path("filter") String  filter);
}
