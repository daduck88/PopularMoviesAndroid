package com.android.popularmoviesapp.app.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.popularmoviesapp.R;
import com.android.popularmoviesapp.model.Movie;

/**
 * Created by Daduck on 7/27/17.
 */

public class DetailActivity  extends AppCompatActivity {
    public static final String MOVIE = "movie";
    private TextView tvTitle;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initViews();
        initData();
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.tv_movie_detail_title);
    }

    private void initData() {
        if(movie == null){
            movie = getIntent().getParcelableExtra(MOVIE);
        }
        tvTitle.setText(movie.getTitle());
    }
}
