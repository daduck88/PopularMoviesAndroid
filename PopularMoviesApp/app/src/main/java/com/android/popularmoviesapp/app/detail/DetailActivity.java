package com.android.popularmoviesapp.app.detail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.popularmoviesapp.R;
import com.android.popularmoviesapp.databinding.ActivityMovieDetailBinding;
import com.android.popularmoviesapp.model.Movie;

/**
 * Created by Daduck on 7/27/17.
 */

public class DetailActivity  extends AppCompatActivity {
    public static final String MOVIE = "movie";
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        bindingData(binding);
    }

    private void bindingData(ActivityMovieDetailBinding binding) {
        if(movie == null){
            if(getIntent() == null || !getIntent().hasExtra(MOVIE)){
                return;
            }
            movie = getIntent().getParcelableExtra(MOVIE);
        }
        binding.setMovie(movie);
    }
}
