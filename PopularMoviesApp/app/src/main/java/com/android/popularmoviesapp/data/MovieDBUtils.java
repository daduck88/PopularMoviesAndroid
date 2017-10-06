package com.android.popularmoviesapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.android.popularmoviesapp.app.App;
import com.android.popularmoviesapp.data.model.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.android.popularmoviesapp.data.MovieContract.MovieEntry;

/**
 * Created by Daduck on 8/31/17.
 */

public class MovieDBUtils {
    public static boolean addMovie(Movie movie) {
        boolean successful = false;
        ContentValues value = new ContentValues(3);
        value.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
        value.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        value.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        value.put(MovieContract.MovieEntry.RELEASE_DATE, movie.getReleaseDate());
        value.put(MovieContract.MovieEntry.VOTE_AVERAGE, movie.getVoteAverage());
        value.put(MovieContract.MovieEntry.OVERVIEW, movie.getOverview());
        SQLiteDatabase db = App.getDBHelper().getWritableDatabase();
        try {
            db.beginTransaction();
            long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
            db.setTransactionSuccessful();
            if (_id != -1) {
                successful = true;
            }
        } finally {
            db.endTransaction();
        }
        return successful;
    }

    public static Movie getMovie(int id){
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();

        String[] selectionArguments = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                MovieContract.MovieEntry.COLUMN_ID + " = ? ",
                selectionArguments,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            return cursorToMovie(cursor);
        }
        return null;
    }

    public static Movie cursorToMovie(Cursor cursor) {
        if((cursor == null) || (cursor.getCount() == 0)){
            return null;
        }
        Movie movie = new Movie();
        int index = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);
        movie.setId(cursor.getInt(index));
        index = cursor.getColumnIndex(MovieEntry.COLUMN_TITLE);
        movie.setTitle(cursor.getString(index));
        index = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        movie.setPosterPath(cursor.getString(index));
        index = cursor.getColumnIndex(MovieContract.MovieEntry.RELEASE_DATE);
        movie.setReleaseDate(cursor.getString(index));
        index = cursor.getColumnIndex(MovieContract.MovieEntry.VOTE_AVERAGE);
        movie.setVoteAverage(Double.parseDouble(cursor.getString(index)));
        index = cursor.getColumnIndex(MovieContract.MovieEntry.OVERVIEW);
        movie.setOverview(cursor.getString(index));
        return movie;
    }

    public static List<Movie> getMovies() {
        SQLiteDatabase db = App.getDBHelper().getReadableDatabase();
        Cursor mCursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        if (mCursor != null && mCursor.moveToFirst()) {
            ArrayList<Movie> movies = cursorToMovies(mCursor);
            return movies;
        }
        return null;
    }

    @NonNull
    public static ArrayList<Movie> cursorToMovies(Cursor mCursor) {
        ArrayList<Movie> movies = new ArrayList<>();
        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
           movies.add(cursorToMovie(mCursor));
        }
        return movies;
    }

    public static boolean deleteMovie(Movie movie) {
        SQLiteDatabase db = App.getDBHelper().getWritableDatabase();
        String[] selectionArguments = new String[]{String.valueOf(movie.getId())};
        int numRowsDeleted = db.delete(
                MovieContract.MovieEntry.TABLE_NAME,
                MovieContract.MovieEntry.COLUMN_ID + " = ? ",
                selectionArguments);
        boolean successful= false;
        if (numRowsDeleted != -1) {
            successful = true;
        }
        return successful;
    }


    public static ContentValues getContentValuesFromMovie(Movie movie) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieEntry.COLUMN_ID, movie.getId());
        movieValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        movieValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        movieValues.put(MovieEntry.RELEASE_DATE, movie.getReleaseDate());
        movieValues.put(MovieEntry.VOTE_AVERAGE, movie.getVoteAverage());
        movieValues.put(MovieEntry.OVERVIEW, movie.getOverview());
        return movieValues;
    }
}
