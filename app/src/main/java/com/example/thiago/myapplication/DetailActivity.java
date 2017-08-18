package com.example.thiago.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailActivity extends AppCompatActivity {

    final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/";

    TextView mOriginalTitleTextView;
    TextView mOverviewTextView;
    TextView mUserRatingTextView;
    TextView mReleaseDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mOriginalTitleTextView = (TextView)findViewById(R.id.original_title);
        mOverviewTextView = (TextView)findViewById(R.id.overview);
        mUserRatingTextView = (TextView)findViewById(R.id.user_rating);
        mReleaseDateTextView = (TextView)findViewById(R.id.release_date);

        final Intent intent = getIntent();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, intent.getStringExtra("title") + " was added to your favorites.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                fab.setVisibility(View.INVISIBLE);
            }
        });

        if(intent != null){
            final String imageLink = IMAGE_URL + intent.getStringExtra("poster_path");
            ImageView posterImageView = (ImageView)findViewById(R.id.poster_background);
            Picasso.with(DetailActivity.this).load(imageLink).into(posterImageView);
            setTitle(intent.getStringExtra("title"));
            mOriginalTitleTextView.append(intent.getStringExtra("title"));
            mOverviewTextView.append(intent.getStringExtra("overview"));
            mUserRatingTextView.append(intent.getStringExtra("vote_average"));
            mReleaseDateTextView.append(intent.getStringExtra("release_date"));
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_settings){
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
