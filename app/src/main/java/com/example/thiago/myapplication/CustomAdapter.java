package com.example.thiago.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.thiago.myapplication.data.Contract;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by admin on 16/09/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MovieViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    private final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/";

    //final private CustomAdapterOnClickHandler mClickHandler;


    private final ListItemClickListener mOnClickListener;

    public interface CustomAdapterOnClickHandler {
        void onClick(int index);
    }

    public CustomAdapter(Context mContext, ListItemClickListener listener) {
        this.mContext = mContext;
        //mClickHandler = clickHandler;
        mOnClickListener = listener;

    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_item, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {

        int posterPathIndex = mCursor.getColumnIndex(Contract.FavoritesEntry.POSTER_PATH);

        mCursor.moveToPosition(position);

        String posterPath = mCursor.getString(posterPathIndex);

        final String imageLink = IMAGE_URL + posterPath;
        Picasso
                .with(mContext)
                .load(imageLink)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.moviePosterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso
                                .with(mContext)
                                .load(imageLink)
                                .noPlaceholder()
                                .into(holder.moviePosterImageView);
                    }
                    @Override
                    public void onError() {
                        Picasso
                                .with(mContext)
                                .load(imageLink)
                                .error(R.drawable.error)
                                .into(holder.moviePosterImageView);
                    }
                });

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Class variables for the task description and priority TextViews
        ImageView moviePosterImageView;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public MovieViewHolder(View itemView) {
            super(itemView);

            moviePosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
