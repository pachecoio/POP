package com.example.thiago.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    RecyclerView rv_movies;
    ProgressBar progressBar;
    TextView errorMessage;

    GridLayoutManager layoutManager;

    String MOST_POPULAR = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=456cfaff4e1856ad9bc68406e43b271f";
    String TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key=456cfaff4e1856ad9bc68406e43b271f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        errorMessage = (TextView)findViewById(R.id.error_message);

        OkHttpHandler okHttpHandler= new OkHttpHandler();
        okHttpHandler.execute(MOST_POPULAR);

    }

    public void showMovies(){
        rv_movies.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage(){
        rv_movies.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    public class OkHttpHandler extends AsyncTask<Object, Object, String> implements NewAdapter.ListItemClickListener{

        OkHttpClient client = new OkHttpClient();

        ArrayList<Movie> movies;

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

                movies = movieArrayList;

                rv_movies = (RecyclerView)findViewById(R.id.rv_movies);

                layoutManager = new GridLayoutManager(MainActivity.this,3);
                rv_movies.setLayoutManager(layoutManager);
                rv_movies.setHasFixedSize(true);
                NewAdapter newAdapter = new NewAdapter(movieArrayList.size(), movieArrayList, this);
                rv_movies.setAdapter(newAdapter);

                progressBar.setVisibility(View.INVISIBLE);
                showMovies();

            } catch (JSONException e) {
                e.printStackTrace();
                showErrorMessage();
            }



        }

        @Override
        public void onListItemClick(int clickedItemIndex) {

            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("vote_count",movies.get(clickedItemIndex).getVOTE_COUNT());
            intent.putExtra("id",movies.get(clickedItemIndex).getID());
            intent.putExtra("video", movies.get(clickedItemIndex).getVIDEO());
            intent.putExtra("vote_average", movies.get(clickedItemIndex).getVOTE_AVERAGE());
            intent.putExtra("title",movies.get(clickedItemIndex).getTITLE());
            intent.putExtra("popularity",movies.get(clickedItemIndex).getPOPULARITY());
            intent.putExtra("poster_path",movies.get(clickedItemIndex).getPOSTER_PATH());
            intent.putExtra("original_language",movies.get(clickedItemIndex).getORIGINAL_LANGUAGE());
            intent.putExtra("original_title",movies.get(clickedItemIndex).getORIGINAL_TITLE());
            intent.putExtra("isAdult",movies.get(clickedItemIndex).isAdult);
            intent.putExtra("overview",movies.get(clickedItemIndex).getOVERVIEW());
            intent.putExtra("release_date",movies.get(clickedItemIndex).getRELEASE_DATE());
            startActivity(intent);

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
