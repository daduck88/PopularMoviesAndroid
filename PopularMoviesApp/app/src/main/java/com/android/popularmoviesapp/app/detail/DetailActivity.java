package com.android.popularmoviesapp.app.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.popularmoviesapp.R;
import com.android.popularmoviesapp.app.App;
import com.android.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by Daduck on 7/27/17.
 */

public class DetailActivity  extends AppCompatActivity {
    public static final String MOVIE = "movie";
    private TextView tvTitle;
    private TextView tvReleaseDate;
    private TextView tvVoteAverage;
    private TextView tvPlotSynopsis;
    private ImageView ivPoster;
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
        tvReleaseDate = (TextView) findViewById(R.id.tv_movie_detail_release_day);
        tvVoteAverage = (TextView) findViewById(R.id.tv_movie_detail_vote_average);
        tvPlotSynopsis = (TextView) findViewById(R.id.tv_movie_detail_plot);
        ivPoster = (ImageView) findViewById(R.id.iv_movie_detail_poster);
    }

    private void initData() {
        if(movie == null){
            if(getIntent() == null || !getIntent().hasExtra(MOVIE)){
                return;
            }
            movie = getIntent().getParcelableExtra(MOVIE);
        }
        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(movie.getReleaseDate());
        tvVoteAverage.setText(movie.getVoteAverage() + "");
        tvPlotSynopsis.setText(movie.getOverview());

        String path = App.context.getString(R.string.url_image_thumb_path) + movie.getPosterPath();
        Picasso.with(getBaseContext()).load(path).into(ivPoster);
    }
}
