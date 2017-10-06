package com.android.popularmoviesapp.app.discovery;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.popularmoviesapp.R;
import com.android.popularmoviesapp.app.App;
import com.android.popularmoviesapp.app.detail.DetailActivity;
import com.android.popularmoviesapp.data.MovieContract;
import com.android.popularmoviesapp.data.MovieDBUtils;
import com.android.popularmoviesapp.data.model.Movie;
import com.android.popularmoviesapp.rest.responses.ResponsePaginated;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDiscoveryActivity extends AppCompatActivity {

    private static final String SORT_BY = "SORT_BY";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String FAVORITES = "FAVORITES";
    RecyclerView rVMainDiscovery;
    DiscoveryAdapter dAdapter;
    private DiscoveryAdapter.OnDiscoverItemClickListener oDICListener;
    private String sortBy;
    private ArrayList<Movie> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_discovery);
        if(savedInstanceState != null){
            sortBy = savedInstanceState.getString(SORT_BY);
        }
        initViews();
        initData();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(SORT_BY, sortBy);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dicovery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_popularity:{
                if(!sortBy.equals(POPULAR)) {
                    sortBy = POPULAR;
                    refreshMovies();
                }
                return true;
            }
            case R.id.action_top_rated:{
                if(!sortBy.equals(TOP_RATED)) {
                    sortBy = TOP_RATED;
                    refreshMovies();
                }
                return true;
            }
            case R.id.action_favorites:{
                if(!sortBy.equals(FAVORITES)) {
                    sortBy = FAVORITES;
                    refreshMovies();
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        rVMainDiscovery = (RecyclerView) findViewById(R.id.rv_main_discovery);
        GridLayoutManager gLManager = new GridLayoutManager(getBaseContext(), getResources().getInteger(R.integer.grid_columns));
        rVMainDiscovery.setLayoutManager(gLManager);
        if(oDICListener == null){
            oDICListener = new DiscoveryAdapter.OnDiscoverItemClickListener() {
                @Override
                public void onItemClick(Movie movie) {
                    navigateToDetail(movie);
                }
            };
        }
        if(dAdapter == null){
            dAdapter = new DiscoveryAdapter(oDICListener);
        }
        rVMainDiscovery.setAdapter(dAdapter);
    }

    private void navigateToDetail(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE, movie);
        startActivity(intent);
    }

    private void initData() {
        if(sortBy == null) {
            sortBy = POPULAR;
        }
        if(moviesList == null || moviesList.isEmpty()) {
            refreshMovies();
        }
    }

    private void refreshMovies() {
        if(sortBy.equalsIgnoreCase(FAVORITES)){
            // get favorites from content provider
            ContentResolver contentResolver = getBaseContext().getContentResolver();
            Cursor cursorResult = contentResolver.query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
            if (cursorResult != null && cursorResult.getCount() > 0){
                moviesList = MovieDBUtils.cursorToMovies(cursorResult);
                dAdapter.setItems(moviesList);
            } else {
                moviesList = new ArrayList<>();
                //empty favorite movies
            }
        } else {
            showLoader();
            moviesList = new ArrayList<>();
            App.getRestClient().getPublicService().getMovies(sortBy).enqueue(new Callback<ResponsePaginated<Movie>>() {
                @Override
                public void onResponse(Call<ResponsePaginated<Movie>> call, Response<ResponsePaginated<Movie>> response) {
                    if (response.isSuccessful()) {
                        if (dAdapter != null) {
                            moviesList = response.body().getResults();
                            dAdapter.setItems(moviesList);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "something goes wrong ", Toast.LENGTH_SHORT).show();
                    }
                    hideLoader();
                }

                @Override
                public void onFailure(Call<ResponsePaginated<Movie>> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "something goes wrong ", Toast.LENGTH_SHORT).show();
                    hideLoader();
                }
            });
        }
    }

    private void showLoader() {
        //TODO
    }

    private void hideLoader() {
        //TODO
    }

}
