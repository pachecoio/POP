package com.example.thiago.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thiago.myapplication.data.Contract;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PopularMovies extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor>, CustomAdapter.ListItemClickListener{

    private static final String TAG = PopularMovies.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;

    private RecyclerView rvMovies;
    private ProgressBar progressBar;
    private TextView errorMessage;

    private TextView numberOfPages;

    private ImageView arrowNext;
    private ImageView arrowPrevious;

    private int pages = 1;

    private String url;

    private GridLayoutManager layoutManager;

    CustomAdapter mAdapter;

    boolean mIsLoading = true;
    int visibleItemCount;
    int totalItemCount;
    int pastVisibleItems;

    String prefMovieString;

    Cursor temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        errorMessage = (TextView)findViewById(R.id.error_message);
        errorMessage.setText(getString(R.string.error_message));
        arrowNext = (ImageView)findViewById(R.id.arrow_next);
        arrowPrevious = (ImageView)findViewById(R.id.arrow_previous);
        numberOfPages = (TextView)findViewById(R.id.number_of_page);
        rvMovies = (RecyclerView)findViewById(R.id.rv_movies);

        layoutManager = new GridLayoutManager(PopularMovies.this, numberOfColumns());
        rvMovies.setLayoutManager(layoutManager);
        rvMovies.setHasFixedSize(true);

        mAdapter = new CustomAdapter(this, this);
        rvMovies.setAdapter(mAdapter);

        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mIsLoading)
                    return;
                updateItemViewCount();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    Log.i("scroll","scrolled");
                }
            }
        });
        showListOfMovies();
    }

    public void updateItemViewCount(){
        visibleItemCount = layoutManager.getChildCount();
        totalItemCount = layoutManager.getItemCount();
        pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(PopularMovies.this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showListOfMovies(){

            getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

            numberOfPages.setText(getResources().getString(R.string.page) + " " + String.valueOf(pages));
    }

    public void showMovies(){
        rvMovies.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage(){
        rvMovies.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        rvMovies.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    public void showNextPage(View view){
        pages++;
        onRestartLoader();
        rvMovies.smoothScrollToPosition(0);
    }

    public void showPreviousPage(View view){
        if(pages > 1) {
            pages--;
            onRestartLoader();
            rvMovies.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_filter_key))){
            pages = 1;
            onRestartLoader();
        }
    }

    public void onRestartLoader(){
        showProgressBar();
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    //LOADER
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            Log.i("location", "oncreateloader");
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PopularMovies.this);
            prefMovieString = sharedPreferences.getString(getString(R.string.pref_filter_key), getString(R.string.pref_movie_most_popular));
            sharedPreferences.registerOnSharedPreferenceChangeListener(PopularMovies.this);
            showProgressBar();
            return new AsyncTaskLoader<Cursor>(this){

                Cursor mTaskData = null;

                @Override
                protected void onStartLoading() {
                    mTaskData = null;
                    if (mTaskData != null) {
                        Log.i("location","On startloading is not null");
                        showProgressBar();
                        deliverResult(mTaskData);
                    } else {
                        showProgressBar();
                        Log.i("locatoin","onstartloading is null");
                        // Force a new load
                        forceLoad();
                    }
                }

                @Override
                public Cursor loadInBackground() {

                    Log.i("location","loadinbackground");

                    if(prefMovieString.equals(getString(R.string.pref_movie_favorites))){
                        return queryFavorites();
                    }else {
                        return insertTempData();
                    }
                }

                public void deliverResult(Cursor data) {
                    mTaskData = data;
                    super.deliverResult(data);
                }

            };

        }

    private Cursor queryFavorites() {

        try {
            return temp = getContentResolver().query(Contract.FavoritesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

        } catch (Exception e) {
            Log.e(TAG, "Failed to asynchronously load data.");
            e.printStackTrace();
            showErrorMessage();
            return temp = null;
        }

    }

    @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if(data == null){
                mAdapter.swapCursor(null);
                showErrorMessage();
            }else {
                mAdapter.swapCursor(data);
                showMovies();
                numberOfPages.setText(getResources().getString(R.string.page) + " " + String.valueOf(pages));
            }
        }

    @Override
        protected void onResume() {
            super.onResume();
            showMovies();
        }

    @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            showProgressBar();
            mAdapter.swapCursor(null);
        }
    //END OF LOADER METHODS

    public Cursor insertTempData(){

        url = prefMovieString + pages + getResources().getString(R.string.pref_movie_complement) + getResources().getString(R.string.api_key);
        Log.i("sharedvalue", url);

        String JSONString;

        if(isNetworkAvailable()) {

            getContentResolver().delete(Contract.TempEntry.CONTENT_URI,null,null);

            OkHttpClient client = new OkHttpClient();

            ArrayList<Movie> movies;

            Request.Builder builder = new Request.Builder();
            builder.url(url);
            Request request = builder.build();
            try {
                Response response = client.newCall(request).execute();
                JSONString = response.body().string();

                ArrayList<Movie> movieArrayList = JSONUtils.getJSONFromString(PopularMovies.this, JSONString);

                for (Movie movie : movieArrayList) {

                    ContentValues cv = new ContentValues();

                    cv.put(Contract.TempEntry.MOVIE_ID, movie.getID());
                    cv.put(Contract.TempEntry.VOTE_COUNT, movie.getVOTE_COUNT());
                    cv.put(Contract.TempEntry.VIDEO, movie.getVIDEO());
                    cv.put(Contract.TempEntry.VOTE_AVERAGE, movie.getVOTE_AVERAGE());
                    cv.put(Contract.TempEntry.TITLE, movie.getTITLE());
                    cv.put(Contract.TempEntry.POPULARITY, movie.getPOPULARITY());
                    cv.put(Contract.TempEntry.POSTER_PATH, movie.getPOSTER_PATH());
                    cv.put(Contract.TempEntry.ORIGINAL_LANGUAGE, movie.getORIGINAL_LANGUAGE());
                    cv.put(Contract.TempEntry.ORIGINAL_TITLE, movie.getORIGINAL_TITLE());
                    if (movie.isAdult) {
                        cv.put(Contract.TempEntry.IS_ADULT, 1);
                    } else {
                        cv.put(Contract.TempEntry.IS_ADULT, 0);
                    }
                    cv.put(Contract.TempEntry.OVERVIEW, movie.getOVERVIEW());
                    cv.put(Contract.TempEntry.RELEASE_DATE, movie.getRELEASE_DATE());
                    Uri uri = getContentResolver().insert(Contract.TempEntry.CONTENT_URI, cv);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {

            temp = getContentResolver().query(Contract.TempEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

            return temp;

        } catch (Exception e) {
            Log.e(TAG, "Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("tempCursor", (Parcelable) temp);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("temp","temp recovered");
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        if(temp != null) {
            temp.moveToPosition(clickedItemIndex);
            Toast.makeText(this, "Clicked" + temp.getString(temp.getColumnIndex(Contract.FavoritesEntry.TITLE)), Toast.LENGTH_SHORT).show();

        }else {
            temp = getContentResolver().query(Contract.TempEntry.CONTENT_URI, null, null, null, null);
            temp.moveToPosition(clickedItemIndex);
            Log.i("movie id", String.valueOf(temp.getInt(temp.getColumnIndex(Contract.TempEntry._ID))));
        }
        Intent intent = new Intent(PopularMovies.this, DetailActivity.class);
        intent.putExtra("vote_count", temp.getString(temp.getColumnIndex(Contract.FavoritesEntry.VOTE_COUNT)));
        intent.putExtra("id", temp.getInt(temp.getColumnIndex(Contract.FavoritesEntry.MOVIE_ID)));
        intent.putExtra("video", temp.getString(temp.getColumnIndex(Contract.FavoritesEntry.VIDEO)));
        intent.putExtra("vote_average", temp.getString(temp.getColumnIndex(Contract.FavoritesEntry.VOTE_AVERAGE)));
        intent.putExtra("title", temp.getString(temp.getColumnIndex(Contract.FavoritesEntry.TITLE)));
        intent.putExtra("popularity", temp.getString(temp.getColumnIndex(Contract.FavoritesEntry.POPULARITY)));
        intent.putExtra("poster_path", temp.getString(temp.getColumnIndex(Contract.FavoritesEntry.POSTER_PATH)));
        intent.putExtra("original_language", temp.getString(temp.getColumnIndex(Contract.FavoritesEntry.ORIGINAL_LANGUAGE)));
        intent.putExtra("original_title", temp.getString(temp.getColumnIndex(Contract.FavoritesEntry.ORIGINAL_TITLE)));
        intent.putExtra("isAdult", temp.getInt(temp.getColumnIndex(Contract.FavoritesEntry.IS_ADULT)));
        intent.putExtra("overview", temp.getString(temp.getColumnIndex(Contract.FavoritesEntry.OVERVIEW)));
        intent.putExtra("release_date", temp.getString(temp.getColumnIndex(Contract.FavoritesEntry.RELEASE_DATE)));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_settings){
            Intent intent = new Intent(PopularMovies.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

}
