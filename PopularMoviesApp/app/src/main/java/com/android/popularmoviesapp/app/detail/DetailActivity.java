package com.android.popularmoviesapp.app.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.popularmoviesapp.R;
import com.android.popularmoviesapp.app.App;
import com.android.popularmoviesapp.data.model.Movie;
import com.android.popularmoviesapp.data.model.Review;
import com.android.popularmoviesapp.data.model.Video;
import com.android.popularmoviesapp.rest.responses.ResponseMovieVideos;
import com.android.popularmoviesapp.rest.responses.ResponsePaginated;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Daduck on 7/27/17.
 */

public class DetailActivity  extends AppCompatActivity {
    public static final String MOVIE = "MOVIE";
    public static final String VIDEOS = "VIDEOS";
    public static final String REVIEWS = "REVIEWS";
    private Movie movie;
    RecyclerView rVMainDiscovery;
    DetailAdapter dAdapter;
    private ArrayList<Video> videoList;
    private ArrayList<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if(savedInstanceState != null){
            videoList = savedInstanceState.getParcelableArrayList(VIDEOS);
            reviewList = savedInstanceState.getParcelableArrayList(REVIEWS);
        }
        initViews();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(VIDEOS, videoList);
        savedInstanceState.putParcelableArrayList(REVIEWS, reviewList);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void initViews() {
        if(movie == null){
            if(getIntent() == null || !getIntent().hasExtra(MOVIE)){
                return;
            }
            movie = getIntent().getParcelableExtra(MOVIE);
        }
        rVMainDiscovery = (RecyclerView) findViewById(R.id.rv_detail_movie);
        rVMainDiscovery.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        if(dAdapter == null){
            dAdapter = new DetailAdapter(movie, this);
        }
        rVMainDiscovery.setAdapter(dAdapter);
        rVMainDiscovery.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        updateVideosReviews();
    }

    private void updateVideosReviews(){
        if(videoList != null && !videoList.isEmpty()){
            dAdapter.setVideos(videoList);
        } else {
            App.getRestClient().getPublicService().getMovieVideos(String.valueOf(movie.getId())).enqueue(new Callback<ResponseMovieVideos>() {
                @Override
                public void onResponse(Call<ResponseMovieVideos> call, Response<ResponseMovieVideos> response) {
                    if (response.isSuccessful()) {
                        if (dAdapter != null) {
                            videoList = response.body().getResults();
                            dAdapter.setVideos(videoList);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "something goes wrong ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseMovieVideos> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "something goes wrong ", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(reviewList != null && !reviewList.isEmpty()){
            dAdapter.setReviews(reviewList);
        } else {
            App.getRestClient().getPublicService().getMovieReviews(String.valueOf(movie.getId())).enqueue(new Callback<ResponsePaginated<Review>>() {
                @Override
                public void onResponse(Call<ResponsePaginated<Review>> call, Response<ResponsePaginated<Review>> response) {

                    if (response.isSuccessful()) {
                        if (dAdapter != null) {
                            reviewList = response.body().getResults();
                            dAdapter.setReviews(reviewList);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "something goes wrong ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponsePaginated<Review>> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "something goes wrong ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
