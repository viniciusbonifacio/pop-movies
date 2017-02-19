package com.udacity.study.popmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by vinicius on 18/02/17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.udacity.study.popmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";


    /**
     *
     */
    public static final class MovieTable implements BaseColumns {

        public static final Uri MOVIE_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = "movie";

        public static final String TMDB_ID = "tmdb_id";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String FAVORITE = "favorite";

        public static final String CREATE_SCRIPT =
                "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TMDB_ID + " INTEGER NOT NULL, "  +
                ORIGINAL_TITLE + " STRING NOT NULL);";
//                FAVORITE + " BOOLEAN NOT NULL CHECK(" + FAVORITE + " IN (0,1)))";
    }

}
