package com.udacity.study.popmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udacity.study.popmovies.R;
import com.udacity.study.popmovies.model.Trailer;

/**
 * Created by vinicius on 13/02/17.
 */

public class TrailerAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;

    private Trailer[] mTrailers;

    private TextView mTitleTv;

    /**
     *
     * @param context
     */
    public TrailerAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        if (mTrailers == null) {
            return 0;
        }
        return mTrailers.length;
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

        View trailerItem;
        if (convertView == null) {

            trailerItem = layoutInflater.inflate(R.layout.trailer_list_item, parent, false);
            mTitleTv = (TextView) trailerItem.findViewById(R.id.tv_detail_trailer_title);

            mTitleTv.setText(mTrailers[position].getName());

        } else {
            trailerItem = convertView;
        }

        trailerItem.setOnClickListener(this);

        trailerItem.setTag(mTrailers[position]);

        return trailerItem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(View v) {

        if (v.getTag() instanceof Trailer) {

            Trailer trailer = (Trailer) v.getTag();
            Intent intent =
                    new Intent(Intent.ACTION_VIEW, Uri.parse("http://youtube.com/watch?v=" + trailer.getKey()));
            mContext.startActivity(intent);
        }
    }

    public void setTrailersData(Trailer[] trailers){
        this.mTrailers = trailers;
    }
}
