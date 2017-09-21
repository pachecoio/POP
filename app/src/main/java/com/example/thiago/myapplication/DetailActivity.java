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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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

    private static final String MOVIE_TRAILER_URL_BEGIN = "https://api.themoviedb.org/3/movie/";
    private static final String MOVIE_TRAILER_URL_TYPE = "/videos?api_key=";
    private static final String MOVIE_LANGUAGE = "&language=en-US";
    private String JSONString;

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
        moviePosterImageView = (ImageView)findViewById(R.id.movie_poster);

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

            String url = getString(R.string.trailer_base_url) + intent.getIntExtra("id",1) + getString(R.string.trailer_type_url) + getString(R.string.api_key) + getString(R.string.trailer_language_url);

            Log.i("url",url);

            OkHttpHandler okHttpHandler = new OkHttpHandler();
            okHttpHandler.execute(url);

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
                for(String videoKey : videoArrayList){
                    Log.i("Video key", videoKey);
                }
            }catch (JSONException e){

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
