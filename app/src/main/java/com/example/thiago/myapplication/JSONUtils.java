package com.example.thiago.myapplication;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by thiagop on 8/16/17.
 */

public final class JSONUtils {

    public static ArrayList<Movie> getJSONFromString(Context context, String jsonStr) throws JSONException {

        String[] parsedData = null;

        String VOTE_COUNT;
        String ID;
        String VIDEO;
        String VOTE_AVERAGE;
        String TITLE;
        String POPULARITY;
        String POSTER_PATH;
        String ORIGINAL_LANGUAGE;
        String ORIGINAL_TITLE;
        Boolean isAdult;
        String OVERVIEW;
        String RELEASE_DATE;

        JSONObject json = new JSONObject(jsonStr);
        JSONArray jsonArray = json.getJSONArray("results");

        parsedData = new String[jsonArray.length()];

        //Movie[] movieArray = new Movie[jsonArray.length()];
        ArrayList<Movie> movieArrayList = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++){

            JSONObject movie = jsonArray.getJSONObject(i);

            //parsedData[i] = movie.getString("title");


            Movie theMovie = new Movie(
                    movie.getString("vote_count"),
                    movie.getString("id"),
                    movie.getString("video"),
                    movie.getString("vote_average"),
                    movie.getString("title"),
                    movie.getString("popularity"),
                    movie.getString("poster_path"),
                    movie.getString("original_language"),
                    movie.getString("original_title"),
                    movie.getBoolean("adult"),
                    movie.getString("overview"),
                    movie.getString("release_date"));

            movieArrayList.add(theMovie);

        }

        return movieArrayList;
    }

}
