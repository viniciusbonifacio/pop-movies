package com.udacity.study.popmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.udacity.study.popmovies.data.MovieContract;
import com.udacity.study.popmovies.model.Movie;
import com.udacity.study.popmovies.exception.NoResultException;
import com.udacity.study.popmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    private GridView mPosterGridView;
    private PosterGridAdapter mPosterAdapter;
    private TextView mNoResultTv;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPosterAdapter = new PosterGridAdapter(this);

        mPosterGridView = (GridView) findViewById(R.id.gv_poster_grid);
        mPosterGridView.setAdapter(mPosterAdapter);

        mNoResultTv = (TextView) findViewById(R.id.tv_main_no_result);

        addListeners();

        loadPopularMovies();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();

        if (menuId == R.id.mn_sort_order){

            String popular = getString(R.string.mn_item_popular);
            String rated = getString(R.string.mn_item_top_rated);
            String favorite = getString(R.string.mn_item_favorite);


            if (String.valueOf(item.getTitle()).equalsIgnoreCase(popular)){
                item.setTitle(rated);
                loadBestRatedMovies();

            } else if (String.valueOf(item.getTitle()).equalsIgnoreCase(rated)) {
                item.setTitle(favorite);
                loadFavoriteMovies();

            } else if (String.valueOf(item.getTitle()).equalsIgnoreCase(favorite)) {
                item.setTitle(popular);
                loadPopularMovies();
            }
        }

        return super.onOptionsItemSelected(item);

    }

    /**
     *
     */
    private void addListeners() {
        mPosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) view.getTag();

                Intent detailsIntent = new Intent(getApplicationContext(), DetailActivity.class);
                detailsIntent.putExtra(Movie.class.getSimpleName(), movie);

                startActivity(detailsIntent);
            }
        });
    }

    /**
     *
     */
    private void loadPopularMovies() {

        try {

            new LoadMoviesTask().execute(NetworkUtils.getFilmPopularUrl());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void loadBestRatedMovies() {

        try {

            new LoadMoviesTask().execute(NetworkUtils.getFilmHighRatedUrl());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    private void loadFavoriteMovies(){

        Cursor cursor =
                getContentResolver().query(MovieContract.MovieTable.MOVIE_CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            int[] queryResult = readFavoritesCursor(cursor);

            List<URL> urls = prepareFavoritesUrl(queryResult);

            new LoadMoviesTask().execute(urls.toArray(new URL[urls.size()]));

        } else {
            showGridResults(false);
        }
    }

    /**
     *
     * @param queryResult
     * @return
     */
    private List<URL> prepareFavoritesUrl(int[] queryResult) {

        ArrayList<URL> urls = new ArrayList<>();

        for (int filmId : queryResult) {

            try {
                urls.add(NetworkUtils.getFavoriteFilmUrl(filmId));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return urls;
    }

    /**
     *
     * @param cursor
     * @return
     */
    private int[] readFavoritesCursor(Cursor cursor) {
        int[] queryResult = new int[cursor.getCount()];

        int position = 0;
        do {
            queryResult[position] =
                    cursor.getInt(cursor.getColumnIndex(MovieContract.MovieTable.TMDB_ID));

            position++;

        } while (cursor.moveToNext());

        return queryResult;
    }

    /**
     *
     * @param bol
     */
    private void showGridResults(boolean bol){
        if (bol){
            mPosterGridView.setVisibility(View.VISIBLE);
            mNoResultTv.setVisibility(View.GONE);
        } else {
            mPosterGridView.setVisibility(View.GONE);
            mNoResultTv.setVisibility(View.VISIBLE);
        }

    }

    /**
     *
     */
    private class LoadMoviesTask extends AsyncTask<URL, Void, List<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {

            ArrayList<Movie> movies = new ArrayList<>();

            try {

                for (URL url : params){

                    String serverResponse = NetworkUtils.getResultsFromServer(url);

                    JsonParser jsonParser = new JsonParser();
                    JsonElement jsonElement = jsonParser.parse(serverResponse);

                    if (jsonElement.getAsJsonObject().get("results") != null) {
                        String results = jsonElement.getAsJsonObject().get("results").toString();

                        movies.addAll(
                                Arrays.asList(
                                        new Gson().fromJson(results, Movie[].class)));
                    } else {
                        movies.add(new Gson().fromJson(serverResponse, Movie.class));
                    }
                }

            } catch (NoResultException | IOException e) {
                e.printStackTrace();

            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> result) {
            if (result != null && result.size() > 0) {
                showGridResults(true);
                Movie[] movies = (result.toArray(new Movie[result.size()]));

                mPosterAdapter.setMoviesData(movies);
                mPosterAdapter.notifyDataSetChanged();

            } else {
                showGridResults(false);
            }
        }
    }
}
