/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.popularmoviesapp.data.MovieContract.MovieEntry;

/**
 * Manages a local database for Movies data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    /*
     * This is the name of our database. Database names should be descriptive and end with the
     * .db extension.
     */
    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                MovieEntry.COLUMN_ID                + " INTEGER PRIMARY KEY, "      +
                MovieEntry.COLUMN_TITLE             + " VARCHAR NOT NULL, "         +
                MovieEntry.COLUMN_POSTER_PATH       + " VARCHAR NOT NULL, "         +

                    /*
                     * To ensure this table can only contain one weather entry per date, we declare
                     * the date column to be unique. We also specify "ON CONFLICT REPLACE". This tells
                     * SQLite that if we have a weather entry for a certain date and we attempt to
                     * insert another weather entry with that date, we replace the old weather entry.
                     */
                    " UNIQUE (" + MovieEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}