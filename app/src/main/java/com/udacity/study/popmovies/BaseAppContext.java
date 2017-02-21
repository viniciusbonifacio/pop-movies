package com.udacity.study.popmovies;

import android.app.Application;

/**
 * Created by vinicius on 21/02/17.
 */

public class BaseAppContext extends Application{


    public static String TMDB_API_KEY;

    @Override
    public void onCreate() {
        super.onCreate();
        TMDB_API_KEY = getString(R.string.tmdb_api_key);
    }

}
