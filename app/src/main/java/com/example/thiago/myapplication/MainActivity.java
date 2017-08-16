package com.example.thiago.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView mPopularMoviesTextView;

    String url = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=456cfaff4e1856ad9bc68406e43b271f";

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
            mPopularMoviesTextView.setText(s);
        }
    }

}
