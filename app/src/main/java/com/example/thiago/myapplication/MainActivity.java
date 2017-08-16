package com.example.thiago.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements MyAdapter.ListItemClickListener{

    //TextView mPopularMoviesTextView;

    private static final int NUM_LIST_ITEMS = 100;
    private String[] names;

    private MyAdapter mAdapter;

    RecyclerView rv_movies;
    ProgressBar progressBar;
    TextView errorMessage;

    GridLayoutManager layoutManager;

    String LIST_STATE_KEY;

    Parcelable listState;

    String url = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=456cfaff4e1856ad9bc68406e43b271f";

    String MOST_POPULAR = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=456cfaff4e1856ad9bc68406e43b271f";
    String TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key=456cfaff4e1856ad9bc68406e43b271f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        errorMessage = (TextView)findViewById(R.id.error_message);

        OkHttpHandler okHttpHandler= new OkHttpHandler();
        okHttpHandler.execute(TOP_RATED);

    }

    public void showMovies(){
        rv_movies.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage(){
        rv_movies.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    public class OkHttpHandler extends AsyncTask<Object, Object, String> implements MyAdapter.ListItemClickListener{

        OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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

            try {
                ArrayList<Movie> movieArrayList = JSONUtils.getJSONFromString(MainActivity.this, s);

                rv_movies = (RecyclerView)findViewById(R.id.rv_movies);

                layoutManager = new GridLayoutManager(MainActivity.this,3);
                rv_movies.setLayoutManager(layoutManager);
                rv_movies.setHasFixedSize(true);

                mAdapter = new MyAdapter(movieArrayList.size(), movieArrayList, this);
                rv_movies.setAdapter(mAdapter);

                progressBar.setVisibility(View.INVISIBLE);
                showMovies();

            } catch (JSONException e) {
                e.printStackTrace();
                showErrorMessage();
            }



        }

        @Override
        public void onListItemClick(int clickedItemIndex) {
            Toast.makeText(MainActivity.this, "Teste"+clickedItemIndex, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Toast.makeText(this, "Teste", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "item: " + names[clickedItemIndex], Toast.LENGTH_SHORT).show();
    }

    //----------------------------------------
    //onsaveinstance


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putParcelable(LIST_STATE_KEY, layoutManager.onSaveInstanceState());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        listState = savedInstanceState.getParcelable(LIST_STATE_KEY);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(listState != null){
            layoutManager.onRestoreInstanceState(listState);
        }

    }
}
