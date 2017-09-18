package com.android.popularmoviesapp.app;

import android.app.Application;
import android.content.Context;

import com.android.popularmoviesapp.R;
import com.android.popularmoviesapp.data.MovieDbHelper;
import com.android.popularmoviesapp.rest.RestClient;
import com.facebook.stetho.Stetho;

/**
 * Created by Daduck on 7/27/17.
 */

public class App extends Application {

    private static RestClient restClient;
    public static Context context;
    public static MovieDbHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        restClient = new RestClient(getString(R.string.base_url));
        stetho();
    }

    private void stetho() {
        Stetho.initializeWithDefaults(this);
    }


    public static RestClient getRestClient() {
        return restClient;
    }

    public static MovieDbHelper getDBHelper(){
        if(dbHelper == null){
            dbHelper = new MovieDbHelper(context);
        }
        return dbHelper;
    }
}
