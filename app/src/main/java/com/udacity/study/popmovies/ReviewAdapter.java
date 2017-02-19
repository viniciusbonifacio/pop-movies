package com.udacity.study.popmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udacity.study.popmovies.model.Review;

/**
 * Created by vinicius on 19/02/17.
 */

public class ReviewAdapter extends BaseAdapter{

    private Context mContext;
    private Review[] mReviews;

    private TextView mAuthorTv;
    private TextView mContentTv;

    /**
     *
     * @param context
     y* @param reviews
     */
    public ReviewAdapter(Context context){
        this.mContext = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        if (mReviews == null) {
            return 0;
        }
        return mReviews.length;
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

        View reviewItem;
        if (convertView == null) {
            reviewItem = layoutInflater.inflate(R.layout.review_list_item, parent, false);
            mAuthorTv = (TextView) reviewItem.findViewById(R.id.tv_review_author);
            mContentTv = (TextView) reviewItem.findViewById(R.id.tv_review_content);

            mAuthorTv.setText(mReviews[position].getAuthor());
            mContentTv.setText(mReviews[position].getContent());

        } else {
            reviewItem = convertView;

        }

        return reviewItem;
    }


    public void setReviewsData(Review[] trailersData) {
        this.mReviews = trailersData;
    }
}
