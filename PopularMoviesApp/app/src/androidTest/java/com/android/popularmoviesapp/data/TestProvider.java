package com.android.popularmoviesapp.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android.popularmoviesapp.data.model.Movie;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

/**
 * Created by Daduck on 9/12/17.
 */


@RunWith(AndroidJUnit4.class)
public class TestProvider {

    private final Context mContext = InstrumentationRegistry.getTargetContext();

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_POSTER_PATH = "posterPath";

    @Test
    public void contentInsertQueryDelete() throws Exception {
        ContentResolver contentResolver = mContext.getContentResolver();

        Movie movie1 = TestStaticMovies.getMovie1();
        ContentValues moviesValues = createInsertTestMovieValues(movie1);
        Uri result = contentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, moviesValues);
        assertEquals("Not create movie " + movie1.getTitle(), "" + movie1.getId() , result.getLastPathSegment());

        Movie movie2 = TestStaticMovies.getMovie2();
        ContentValues moviesValues2 = createInsertTestMovieValues(movie2);
        result = contentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, moviesValues2);
        assertEquals("Not create movie " + movie2.getTitle(), "" + movie2.getId() , result.getLastPathSegment());

        Movie movie3 = TestStaticMovies.getMovie3();
        ContentValues moviesValues3 = createInsertTestMovieValues(movie3);
        result = contentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, moviesValues3);
        assertEquals("Not create movie " + movie3.getTitle(), "" + movie3.getId() , result.getLastPathSegment());


        Cursor cursorResult = contentResolver.query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        assertSame("Not get all movies movie ", 3 , cursorResult.getCount());

        int resultDelete = contentResolver.delete(MovieProvider.getUriFromMovie(movie1), null, null);
        assertSame("Not delete movie " + movie1.getTitle(), 1 , resultDelete);
        resultDelete = contentResolver.delete(MovieProvider.getUriFromMovie(movie2), null, null);
        assertSame("Not delete movie " + movie2.getTitle(), 1 , resultDelete);
        resultDelete = contentResolver.delete(MovieProvider.getUriFromMovie(movie3), null, null);
        assertSame("Not delete movie " + movie3.getTitle(), 1 , resultDelete);
    }



    static ContentValues createInsertTestMovieValues(Movie movie) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(COLUMN_ID, movie.getId());
        movieValues.put(COLUMN_TITLE, movie.getTitle());
        movieValues.put(COLUMN_POSTER_PATH, movie.getPosterPath());
        return movieValues;
    }
}
