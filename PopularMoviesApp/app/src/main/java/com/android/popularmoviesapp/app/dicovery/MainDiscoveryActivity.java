package com.android.popularmoviesapp.app.dicovery;

import android.content.Intent;
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
import com.android.popularmoviesapp.model.Movie;
import com.android.popularmoviesapp.rest.responses.ResponseMovies;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDiscoveryActivity extends AppCompatActivity {

    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    RecyclerView rVMainDiscovery;
    DiscoveryAdapter dAdapter;
    private DiscoveryAdapter.OnDiscoverItemClickListener oDICListener;
    private String sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_discovery);
        initViews();
        initData();
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        rVMainDiscovery = (RecyclerView) findViewById(R.id.rv_main_discovery);
        GridLayoutManager gLManager = new GridLayoutManager(getBaseContext(), 2);
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
        sortBy =  POPULAR;
        refreshMovies();
    }

    private void refreshMovies() {
        App.getRestClient().getPublicService().getMovies(sortBy).enqueue(new Callback<ResponseMovies>() {
            @Override
            public void onResponse(Call<ResponseMovies> call, Response<ResponseMovies> response) {
                if(response.isSuccessful()) {
                    if (dAdapter != null) {
                        dAdapter.setItems(response.body().getResults());
                    }
                } else {
                    Toast.makeText(getBaseContext(), "something goes wrong ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMovies> call, Throwable t) {
                Toast.makeText(getBaseContext(), "something goes wrong ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
