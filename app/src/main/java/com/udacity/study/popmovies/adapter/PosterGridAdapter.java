package com.udacity.study.popmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.study.popmovies.R;
import com.udacity.study.popmovies.model.Movie;
import com.udacity.study.popmovies.utils.NetworkUtils;

import java.net.MalformedURLException;

/**
 * Created by vinicius on 09/02/17.
 */
public class PosterGridAdapter extends BaseAdapter {


    private Context mContext;
    private Movie[] mMovies;


    public PosterGridAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        if (mMovies == null) {
            return 0;
        }
        return mMovies.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridItem;
        if (convertView == null) {

            gridItem = layoutInflater.inflate(R.layout.poster_grid_item, parent, false);
            ImageView posterView = (ImageView) gridItem.findViewById(R.id.iv_film_poster);

            loadPosterImage(posterView, mMovies[position]);

        } else {

            gridItem = convertView;
            ImageView posterView = (ImageView) gridItem.findViewById(R.id.iv_film_poster);
            loadPosterImage(posterView, mMovies[position]);

        }

        gridItem.setTag(mMovies[position]);
        return gridItem;
    }

    /**
     *
     * @param posterView
     * @param movie
     */
    private void loadPosterImage(ImageView posterView, Movie movie){

        if (posterView != null && movie != null){
            try {

                Picasso.with(mContext)
                        .load(NetworkUtils.getFilmPosterUrl(movie.getPosterPath()).toString())
                        .into(posterView);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param movies
     */
    public void setMoviesData(Movie[] movies) {
        this.mMovies = movies;
    }

}
