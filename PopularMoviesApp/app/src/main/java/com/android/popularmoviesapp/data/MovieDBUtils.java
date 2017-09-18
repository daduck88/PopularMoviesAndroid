package com.android.popularmoviesapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.popularmoviesapp.app.App;
import com.android.popularmoviesapp.data.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daduck on 8/31/17.
 */

public class MovieDBUtils {
    public static boolean addMovie(Movie movie) {
        boolean successful = false;
        ContentValues value = new ContentValues(3);
        value.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
        value.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        value.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
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

    private static Movie cursorToMovie(Cursor cursor) {
        Movie movie = new Movie();
        int index = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);
        movie.setId(cursor.getInt(index));
        index = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        movie.setTitle(cursor.getString(index));
        index = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        movie.setPosterPath(cursor.getString(index));
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
            ArrayList<Movie> movies = new ArrayList<>();
            for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
               movies.add(cursorToMovie(mCursor));
            }
            return movies;
        }
        return null;
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
}
