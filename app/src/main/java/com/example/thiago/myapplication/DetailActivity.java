package com.example.thiago.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.print.PrintAttributes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thiago.myapplication.data.Contract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity{

    private final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/";

    private TextView mOriginalTitleTextView;
    private TextView mOverviewTextView;
    private TextView mUserRatingTextView;
    private TextView mReleaseDateTextView;
    private ImageView moviePosterImageView;
    private RecyclerView rvReviews;
    private ProgressBar progressBar;
    TextView noReviewsTv;

    RelativeLayout contentRelativeLayout;

    public static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";

    String youtubeUrl;

    View recentView;

    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }
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
        moviePosterImageView = (ImageView)findViewById(R.id.movie_poster);
        rvReviews = (RecyclerView)findViewById(R.id.rv_reviews);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        noReviewsTv = (TextView)findViewById(R.id.no_reviews);


        final Intent intent = getIntent();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, intent.getStringExtra("title") + " was added to your favorites.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                ContentValues cv = new ContentValues();

                cv.put(Contract.FavoritesEntry.MOVIE_ID, intent.getIntExtra("id",1));
                cv.put(Contract.FavoritesEntry.VOTE_COUNT, intent.getStringExtra("vote_count"));
                cv.put(Contract.FavoritesEntry.VIDEO, intent.getStringExtra("video"));
                cv.put(Contract.FavoritesEntry.VOTE_AVERAGE, intent.getStringExtra("vote_average"));
                cv.put(Contract.FavoritesEntry.TITLE, intent.getStringExtra("title"));
                cv.put(Contract.FavoritesEntry.POPULARITY, intent.getStringExtra("popularity"));
                cv.put(Contract.FavoritesEntry.POSTER_PATH, intent.getStringExtra("poster_path"));
                cv.put(Contract.FavoritesEntry.ORIGINAL_LANGUAGE, intent.getStringExtra("original_language"));
                cv.put(Contract.FavoritesEntry.ORIGINAL_TITLE, intent.getStringExtra("original_title"));
                cv.put(Contract.FavoritesEntry.IS_ADULT, intent.getIntExtra("isAdult",1));
                cv.put(Contract.FavoritesEntry.OVERVIEW, intent.getStringExtra("overview"));
                cv.put(Contract.FavoritesEntry.RELEASE_DATE, intent.getStringExtra("release_date"));
                getContentResolver().insert(Contract.FavoritesEntry.CONTENT_URI, cv);

                fab.setVisibility(View.INVISIBLE);
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailActivity.this);
        String prefMovieString = sharedPreferences.getString(getString(R.string.pref_filter_key), getString(R.string.pref_movie_most_popular));

        if(prefMovieString.equals(getString(R.string.pref_movie_favorites))){
            fab.setVisibility(View.INVISIBLE);
        }

        if(intent != null){
            final String imageLink = IMAGE_URL + intent.getStringExtra("poster_path");
            ImageView posterImageView = (ImageView)findViewById(R.id.poster_background);
            if(getRotation(this).equals("portrait")) {
                Picasso.with(DetailActivity.this).load(imageLink).into(posterImageView);
            }
            Picasso.with(DetailActivity.this).load(imageLink).into(moviePosterImageView);
            setTitle(intent.getStringExtra("title"));
            mOriginalTitleTextView.append(intent.getStringExtra("title"));
            mOverviewTextView.append(intent.getStringExtra("overview"));
            mUserRatingTextView.append(intent.getStringExtra("vote_average"));
            mReleaseDateTextView.append(intent.getStringExtra("release_date"));

            String url = getString(R.string.movie_base_url) + intent.getIntExtra("id",1) + getString(R.string.trailer_type_url) + getString(R.string.api_key) + getString(R.string.movie_language_url);

            Log.i("url",url);

            OkHttpHandler okHttpHandler = new OkHttpHandler();
            okHttpHandler.execute(url);

            String reviewUrl = getString(R.string.movie_base_url) + intent.getIntExtra("id",1) + getString(R.string.review_type_url) + getString(R.string.api_key) + getString(R.string.movie_language_url);

            Log.i("review url", reviewUrl);
            GetReviews getReviews = new GetReviews();
            getReviews.execute(reviewUrl);
        }


    }

    public void showErrorMessage(){
        rvReviews.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        noReviewsTv.setVisibility(View.VISIBLE);
    }

    public void showProgressBar(){
        rvReviews.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        noReviewsTv.setVisibility(View.INVISIBLE);
    }

    public void showReviews(){
        rvReviews.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        noReviewsTv.setVisibility(View.INVISIBLE);
    }

    public void onClickTrailer(View view){
        if(youtubeUrl != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_BASE_URL + youtubeUrl)));
        }else{
            Toast.makeText(this, "Trailer not found, please try again later!", Toast.LENGTH_SHORT).show();
        }
    }

    public class OkHttpHandler extends AsyncTask<Object, Object, String>{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... params) {
            Request.Builder builder = new Request.Builder();
            builder.url((String) params[0]);
            Request request = builder.build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{
                ArrayList<String> videoArrayList = JSONUtils.getVideoJSONFromString(DetailActivity.this, s);
                if(videoArrayList != null) {
                    youtubeUrl = videoArrayList.get(0);
                }else{
                    youtubeUrl = null;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }

    public class GetReviews extends AsyncTask<Object, Object, String>{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected String doInBackground(Object... params) {
            Request.Builder builder = new Request.Builder();
            builder.url((String) params[0]);
            Request request = builder.build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{
                ArrayList<Review> reviewArrayList = JSONUtils.getReviewsFromString(DetailActivity.this, s);
                if(reviewArrayList!=null) {

                    layoutManager = new LinearLayoutManager(DetailActivity.this);
                    rvReviews.setLayoutManager(layoutManager);
                    rvReviews.setHasFixedSize(true);
                    ReviewAdapter reviewAdapter = new ReviewAdapter(reviewArrayList.size(),reviewArrayList);
                    rvReviews.setAdapter(reviewAdapter);
                    showReviews();
                }else{
                    showErrorMessage();
                }
            }catch (JSONException e){
                e.printStackTrace();
                showErrorMessage();
            }

        }
    }


    public String getRotation(Context context){
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "portrait";
            case Surface.ROTATION_90:
                return "landscape";
            case Surface.ROTATION_180:
                return "reverse portrait";
            default:
                return "reverse landscape";
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
            Intent intent = new Intent(DetailActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
