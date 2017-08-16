package com.example.thiago.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    TextView mPopularMoviesTextView;

    String url = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=456cfaff4e1856ad9bc68406e43b271f";

    String MOST_POPULAR = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=456cfaff4e1856ad9bc68406e43b271f";
    String TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key=456cfaff4e1856ad9bc68406e43b271f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPopularMoviesTextView= (TextView)findViewById(R.id.tv_movies_json);

        OkHttpHandler okHttpHandler= new OkHttpHandler();
        okHttpHandler.execute(url);
    }

    public class OkHttpHandler extends AsyncTask<Object, Object, String> {

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

            String movies = "";

            try {
                ArrayList<Movie> movieArrayList = JSONUtils.getJSONFromString(MainActivity.this, s);

                for(Movie movie : movieArrayList){
                    Log.i("Movie Title", movie.getORIGINAL_TITLE());

                    mPopularMoviesTextView.append(movie.getORIGINAL_TITLE() + "\n\n\n");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
