package com.android.popularmoviesapp.app.detail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.popularmoviesapp.R;
import com.android.popularmoviesapp.app.App;
import com.android.popularmoviesapp.data.model.Movie;
import com.android.popularmoviesapp.data.model.Review;
import com.android.popularmoviesapp.data.model.Video;
import com.android.popularmoviesapp.rest.responses.ResponseMovieVideos;
import com.android.popularmoviesapp.rest.responses.ResponsePaginated;
import com.android.popularmoviesapp.utils.DataBinder;

import java.util.ArrayList;

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
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView iVBackdrop;
    RecyclerView rVDetail;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        if(movie == null){
            if(getIntent() == null || !getIntent().hasExtra(MOVIE)){
                return;
            }
            movie = getIntent().getParcelableExtra(MOVIE);
        }
        rVDetail = (RecyclerView) findViewById(R.id.rv_detail_movie);
        rVDetail.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        if(dAdapter == null){
            dAdapter = new DetailAdapter(movie, this);
        }
        rVDetail.setAdapter(dAdapter);
        rVDetail.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        iVBackdrop = (ImageView) findViewById(R.id.iv_detail_backdrop);
        DataBinder.imageUrl(iVBackdrop, movie.getBackdropPathURL());
        updateVideosReviews();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(movie.getTitle());

        dynamicToolbarColor();
        toolbarTextAppernce();
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


    private void dynamicToolbarColor() {
        collapsingToolbarLayout.setContentScrimColor(getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setStatusBarScrimColor(getColor(R.color.colorPrimaryDark));
    }

    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }
}
