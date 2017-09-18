package com.android.popularmoviesapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.popularmoviesapp.app.App;
import com.android.popularmoviesapp.data.model.Movie;

/**
 * Created by Daduck on 9/12/17.
 */

public class MovieProvider extends ContentProvider {

    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_BY_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        /* This URI is content://com.example.android.sunshine/weather/ */
        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE);

        /* This URI is content://com.example.android.sunshine/weather/ */
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", CODE_MOVIE_BY_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        throw new RuntimeException(
                "We are not implementing bulkInsert in Movie App. Use insert instead");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues value) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE:
                db.beginTransaction();
                long _id;
                try {
                    _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);

                return getUriForId(_id, uri);

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_BY_ID: {
                String idString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{idString};
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }
            case CODE_MOVIE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE_BY_ID:
                String idStr = uri.getLastPathSegment();
                String where = MovieContract.MovieEntry.COLUMN_ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        where,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in Movies app.");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update in Movies app");
    }

    public static Uri getUriFromMovie(Movie movie) {
        if (movie != null) {
            return getUriForId(movie.getId(), MovieContract.MovieEntry.CONTENT_URI);
        }
        // s.th. went wrong:
        throw new SQLException(
                "Problem while creating uri, empty movie ");
    }

    private static Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            App.context.
                    getContentResolver().
                    notifyChange(uri, null);
            return itemUri;
        }
        // s.th. went wrong:
        throw new SQLException(
                "Problem while inserting into uri: " + uri);
    }

}
