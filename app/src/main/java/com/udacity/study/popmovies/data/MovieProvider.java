package com.udacity.study.popmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by vinicius on 18/02/17.
 */

public class MovieProvider extends ContentProvider {


    public static final int CODE_MOVIE = 100;
    public static final int CODE_MOVIE_WITH_TMDB_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mDbHelper;

    public static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE_WITH_TMDB_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case CODE_MOVIE_WITH_TMDB_ID :
                String[] tmdbMovieId = new String[]{uri.getLastPathSegment()};

                cursor = mDbHelper.getReadableDatabase().query(
                        MovieContract.MovieTable.TABLE_NAME,
                        projection,
                        MovieContract.MovieTable.TMDB_ID + " = ?",
                        tmdbMovieId,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_MOVIE:
                cursor = mDbHelper.getReadableDatabase().query(
                        MovieContract.MovieTable.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        mDbHelper.getWritableDatabase().insert(
                MovieContract.MovieTable.TABLE_NAME,
                null,
                values
        );

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                MovieContract.MovieTable.TABLE_NAME,
                selection,
                selectionArgs
        );

        if(numRowsDeleted > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Update not available");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new RuntimeException("Get type not available");
    }
}
