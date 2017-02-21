package com.udacity.study.popmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.udacity.study.popmovies.adapter.ReviewAdapter;
import com.udacity.study.popmovies.adapter.TrailerAdapter;
import com.udacity.study.popmovies.data.MovieContract;
import com.udacity.study.popmovies.exception.NoResultException;
import com.udacity.study.popmovies.model.Movie;
import com.udacity.study.popmovies.model.Review;
import com.udacity.study.popmovies.model.Trailer;
import com.udacity.study.popmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vinicius on 12/02/17.
 */

public class DetailActivity extends AppCompatActivity{

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private Movie mMovie;
    private ListView mTrailerRecyclerView;
    private ListView mRevirewListView;

    private boolean mIsFavorite;

    private TextView mOriginalTitleTv;
    private TextView mSynopsisTv;
    private TextView mRatingTv;
    private TextView mReleaseTv;
    private ImageView mPosterIv;
    private ImageView mFavoriteIv;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mMovie = getIntent().getParcelableExtra(Movie.class.getSimpleName());

        bindUi();
        writeDetails();
        getTrailers();
        getReviews();
        checkIfFavorite();
        addListeners();
    }

    /**
     *
     */
    private void bindUi() {
        bindTrailerView();
        bindReviewView();

        mOriginalTitleTv = (TextView) findViewById(R.id.tv_detail_original_title);
        mSynopsisTv = (TextView) findViewById(R.id.tv_detail_synopsis);
        mRatingTv = (TextView) findViewById(R.id.tv_detail_rating);
        mReleaseTv = (TextView) findViewById(R.id.tv_detail_release);
        mPosterIv = (ImageView) findViewById(R.id.iv_detail_poster);
        mFavoriteIv = (ImageView) findViewById(R.id.iv_detail_favorite);
    }

    /**
     *
     */
    private void bindTrailerView(){
        mTrailerRecyclerView = (ListView) findViewById(R.id.lv_detail_trailer);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
    }

    /**
     *
     */
    private void bindReviewView(){
        mRevirewListView = (ListView) findViewById(R.id.lv_detail_review);
        mReviewAdapter = new ReviewAdapter(this);
        mRevirewListView.setAdapter(mReviewAdapter);
    }

    /**
     *
     */
    private void writeDetails() {
        if (mMovie != null) {
            mOriginalTitleTv.setText(mMovie.getOriginalTitle());
            mSynopsisTv.setText(mMovie.getOverview());
            mRatingTv.setText(String.valueOf(mMovie.getVoteAverage()));
            mReleaseTv.setText(mMovie.getReleaseDate());

            try {
                Picasso.with(this)
                        .load(NetworkUtils.getFilmPosterUrl(mMovie.getPosterPath()).toString())
                        .into(mPosterIv);

            } catch (MalformedURLException e) {
                e.printStackTrace();

            }
        }
    }

    private void checkIfFavorite(){

        String[] queryParam = {String.valueOf(mMovie.getIdFilm())};

        Cursor cursor = getContentResolver().query(
                MovieContract.MovieTable.MOVIE_CONTENT_URI,
                null,
                MovieContract.MovieTable.TMDB_ID + " = ?",
                queryParam,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            mIsFavorite = true;
            mFavoriteIv.setImageResource(android.R.drawable.btn_star_big_on);

        } else {
            mIsFavorite = false;
            mFavoriteIv.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }

    private void markAsFavorite(){

        if (mIsFavorite){

            String[] queryParam = {String.valueOf(mMovie.getIdFilm())};
            getContentResolver().delete(
                    MovieContract.MovieTable.MOVIE_CONTENT_URI,
                    MovieContract.MovieTable.TMDB_ID + " = ?",
                    queryParam);

            mIsFavorite = false;

            checkIfFavorite();

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieTable.TMDB_ID, mMovie.getIdFilm());
            contentValues.put(MovieContract.MovieTable.ORIGINAL_TITLE, mMovie.getTitle());

            getContentResolver().insert(
                    MovieContract.MovieTable.MOVIE_CONTENT_URI,
                    contentValues
            );

            mIsFavorite = true;

            checkIfFavorite();
        }

    }

    /**
     *
     */
    private void getTrailers(){
        try {

            new LoadTrailerTask().execute(NetworkUtils.getTrailersUrl(mMovie.getIdFilm()));

        } catch (MalformedURLException e) {
            e.printStackTrace();

        }

    }

    private void getReviews(){
        try {

            new LoadReviewsTask().execute(NetworkUtils.getReviewsUrl(mMovie.getIdFilm()));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void addListeners(){
        mFavoriteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markAsFavorite();

            }
        });
    }


    /**
     *
     */
    private class LoadTrailerTask extends AsyncTask<URL, Void, String>{

        private String serverResponse;

        @Override
        protected String doInBackground(URL... params) {

            try {

                serverResponse = NetworkUtils.getResultsFromServer(params[0]);

            } catch (NoResultException | IOException e) {
                e.printStackTrace();

            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                JsonParser jsonParser = new JsonParser();
                JsonElement parse = jsonParser.parse(s);
                String results = parse.getAsJsonObject().get("results").toString();
                Trailer[] trailers = new Gson().fromJson(results, Trailer[].class);

                mTrailerAdapter.setTrailersData(trailers);
                mTrailerAdapter.notifyDataSetChanged();
            }
        }
    }

    private class LoadReviewsTask extends AsyncTask<URL, Void, String> {

        private String serverResponse;

        @Override
        protected String doInBackground(URL... params) {

            try {

                serverResponse = NetworkUtils.getResultsFromServer(params[0]);

            } catch (NoResultException | IOException e) {
                e.printStackTrace();

            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                JsonParser jsonParser = new JsonParser();
                JsonElement parse = jsonParser.parse(s);
                String results = parse.getAsJsonObject().get("results").toString();
                Review[] reviews = new Gson().fromJson(results, Review[].class);

                mReviewAdapter.setReviewsData(reviews);
                mReviewAdapter.notifyDataSetChanged();
            }
        }
    }

}
