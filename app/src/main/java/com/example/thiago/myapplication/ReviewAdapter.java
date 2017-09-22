package com.example.thiago.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by admin on 21/09/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    Context context;

    ArrayList<Review> reviewArrayList;
    int reviewLenght;

    public ReviewAdapter (int reviewLenght, ArrayList<Review> reviewArrayList) {
        this.reviewLenght = reviewLenght;
        this.reviewArrayList = reviewArrayList;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        holder.authorTv.setText(reviewArrayList.get(position).getAuthor());
        holder.contentTv.setText(reviewArrayList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewLenght;
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView authorTv;
        TextView contentTv;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);

            authorTv = (TextView) itemView.findViewById(R.id.author_name);
            contentTv = (TextView)itemView.findViewById(R.id.content);

        }
    }

}
