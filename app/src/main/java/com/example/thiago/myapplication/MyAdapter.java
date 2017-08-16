package com.example.thiago.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by thiagop on 8/15/17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder>{

    private static final String TAG = MyAdapter.class.getSimpleName();

    final private ListItemClickListener mOnClickListener;

    final String IMAGE_LINK = "https://image.tmdb.org/t/p/w500/";

    private int items;
    private int countItems = 0;
    //private String[] names;
    private ArrayList<Movie> movieArrayList;

    //CONSTRUTOR
    public MyAdapter(int nItems, ArrayList<Movie> movieArrayList, ListItemClickListener listener){
        this.items = nItems;
        //this.names = names;
        this.movieArrayList = movieArrayList;
        mOnClickListener = listener;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ItemViewHolder viewHolder = new ItemViewHolder(view);

        //viewHolder.posterImageView.setBackgroundColor();
        //viewHolder.teste.setText(names[countItems]);

        String posterImage = movieArrayList.get(countItems).getPOSTER_PATH();

        Picasso.with(context).load("https://image.tmdb.org/t/p/w500/"+posterImage).into(viewHolder.posterImageView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView posterImageView;
        TextView teste;

        public ItemViewHolder(View itemView) {
            super(itemView);

            posterImageView = (ImageView) itemView.findViewById(R.id.iv_movie);
            teste = (TextView) itemView.findViewById(R.id.teste);
            itemView.setOnClickListener(this);
        }
        void bind(int listIndex) {
            //listItemView.setText(String.valueOf(listIndex));

            countItems = listIndex+1;
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
