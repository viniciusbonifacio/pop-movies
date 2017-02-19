package com.udacity.study.popmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
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

    @Override
    public int getCount() {
        if (mMovies == null) {
            return 0;
        }
        return mMovies.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridItem;
        if (convertView == null) {

            gridItem = layoutInflater.inflate(R.layout.poster_grid_item, parent, false);
            ImageView posterView = (ImageView) gridItem.findViewById(R.id.iv_film_poster);

            try {

                Picasso.with(mContext)
                        .load(NetworkUtils.getFilmPosterUrl(mMovies[position].getPosterPath()).toString())
                        .into(posterView);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        } else {

            gridItem = convertView;

        }

        gridItem.setTag(mMovies[position]);
        return gridItem;
    }

    /**
     *
     * @param movies
     */
    public void setMoviesData(Movie[] movies) {
        this.mMovies = movies;
    }

}
