package com.android.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.android.popularmoviesapp.app.App;

/**
 * Created by Daduck on 8/22/17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.android.popularmoviesapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_ID = "id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_POSTER_PATH = "posterPath";

        /**
         * @param id
         * @return Uri to query details about a single movie entry
         */
        public static Uri buildMovieUriWithID(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
    }
}
