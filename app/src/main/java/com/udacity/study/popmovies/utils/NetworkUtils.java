package com.udacity.study.popmovies.utils;

import android.net.Uri;
import android.util.Log;

import com.udacity.study.popmovies.BuildConfig;
import com.udacity.study.popmovies.exception.NoResultException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by vinicius on 09/02/17.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getName();

    private static final String TMDB_API_KEY = BuildConfig.TMDB_API_KEY;

    private static final String HOST_API_TMDB = "http://api.themoviedb.org/3/movie";
    private static final String HOST_IMAGES_TMDB = "http://image.tmdb.org/t/p/";

    private static final String IMAGE_SIZE_TMDB = "w185";

    private static final String QUERY_PARAM_API_KEY = "api_key";

    private static final String PATH_FILM_POPULAR = "popular";
    private static final String PATH_FILM_RATED = "top_rated";
    private static final String PATH_FILM_VIDEO = "videos";
    private static final String PATH_FILM_REVIEW = "reviews";


    /**
     *
     * @param identifier
     * @return
     * @throws MalformedURLException
     */
    public static URL getFilmPosterUrl(String identifier) throws MalformedURLException {

        StringBuilder sb = new StringBuilder(identifier);
        identifier = sb.deleteCharAt(sb.indexOf("/")).toString();

        Uri uri = Uri.parse(HOST_IMAGES_TMDB).buildUpon()
                .appendPath(IMAGE_SIZE_TMDB)
                .appendEncodedPath(identifier)
                .build();

        Log.d(TAG, "TMDB Poster Image URL: " + uri.toString());

        return new URL(uri.toString());

    }

    /**
     *
     * @return
     * @throws MalformedURLException
     */
    public static URL getFilmPopularUrl() throws MalformedURLException {

        //TODO complementar os parametros de requisicao da api

        Uri uri = Uri.parse(HOST_API_TMDB).buildUpon()
                .appendPath(PATH_FILM_POPULAR)
                .appendQueryParameter(QUERY_PARAM_API_KEY, TMDB_API_KEY)
                .build();

        Log.d(TAG, "TMDB Popular API URL: " + uri.toString());

        return new URL(uri.toString());
    }

    /**
     *
     * @return
     * @throws MalformedURLException
     */
    public static URL getFilmHighRatedUrl() throws MalformedURLException {

        //TODO complementar os parametros de requisicao da api

        Uri uri = Uri.parse(HOST_API_TMDB).buildUpon()
                .appendPath(PATH_FILM_RATED)
                .appendQueryParameter(QUERY_PARAM_API_KEY, TMDB_API_KEY)
                .build();

        Log.d(TAG, "TMDB Rated API URL: " + uri.toString());

        return new URL(uri.toString());
    }

    /**
     *
     * @param idFilm
     * @return
     */
    public static URL getTrailersUrl(int idFilm) throws MalformedURLException {
        Uri uri = Uri.parse(HOST_API_TMDB).buildUpon()
                .appendPath(String.valueOf(idFilm))
                .appendPath(PATH_FILM_VIDEO)
                .appendQueryParameter(QUERY_PARAM_API_KEY, TMDB_API_KEY)
                .build();

        Log.d(TAG, "TMDB Trailers URL: " + uri.toString());

        return new URL(uri.toString());
    }

    /**
     *
     * @param idFilm
     * @return
     * @throws MalformedURLException
     */
    public static URL getFavoriteFilmUrl(int idFilm) throws MalformedURLException {
        Uri uri = Uri.parse(HOST_API_TMDB).buildUpon()
                .appendPath(String.valueOf(idFilm))
                .appendQueryParameter(QUERY_PARAM_API_KEY, TMDB_API_KEY)
                .build();

        Log.d(TAG, "TMDB Favorite URL: " + uri.toString());

        return new URL(uri.toString());
    }

    /**
     *
     * @param idFilm
     * @return
     */
    public static URL getReviewsUrl(int idFilm) throws MalformedURLException {
        Uri uri = Uri.parse(HOST_API_TMDB).buildUpon()
                .appendPath(String.valueOf(idFilm))
                .appendPath(PATH_FILM_REVIEW)
                .appendQueryParameter(QUERY_PARAM_API_KEY, TMDB_API_KEY)
                .build();

        Log.d(TAG, "TMDB Review URL: " + uri.toString());

        return new URL(uri.toString());
    }

    /**
     *
     * @param url
     * @return
     * @throws IOException
     * @throws NoResultException
     */
    public static String getResultsFromServer(URL url) throws IOException, NoResultException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream is = conn.getInputStream();
        Scanner scanner = new Scanner(is);
        scanner.useDelimiter("\\A");

        if (scanner.hasNext()){

            String response = scanner.next();
            scanner.close();
            return response;

        } else {
            throw new NoResultException("There is no result from host");
        }
    }

}
