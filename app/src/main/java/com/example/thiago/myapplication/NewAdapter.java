package com.example.thiago.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by thiagop on 8/17/17.
 */

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.MyAdapterViewHolder>{

    private Context context;

    private final ListItemClickListener mOnClickListener;

    private ArrayList<Movie> movieArrayList;
    private int movieLenght;
    private final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/";

    public NewAdapter(int movieLenght, ArrayList<Movie> movieArrayList, ListItemClickListener listener){
        this.movieLenght = movieLenght;
        this.movieArrayList = movieArrayList;
        mOnClickListener = listener;
    }

    @Override
    public MyAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MyAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapterViewHolder holder, int position) {
        String imageLink = IMAGE_URL + movieArrayList.get(position).getPOSTER_PATH();
        Picasso.with(context).load(imageLink).into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return movieLenght;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public class MyAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView posterImageView;

        public MyAdapterViewHolder(View itemView) {
            super(itemView);

            posterImageView = (ImageView)itemView.findViewById(R.id.iv_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
