package com.example.thiago.myapplication.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by admin on 16/09/2017.
 */

public class Contract {

    public static final String AUTHORITY = "com.example.thiago.myapplication";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    public static final String PATH_TEMP = "temp";


    public static final class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        // Table
        public static final String TABLE_NAME = "favorites";
        //Columns
        public static final String MOVIE_ID = "movieid";
        public static final String VOTE_COUNT = "votecount";
        public static final String VIDEO = "video";
        public static final String VOTE_AVERAGE = "voteaverage";
        public static final String TITLE = "title";
        public static final String POPULARITY = "popularity";
        public static final String POSTER_PATH = "posterpath";
        public static final String ORIGINAL_LANGUAGE = "originallanguage";
        public static final String ORIGINAL_TITLE = "originaltitle";
        public static final String IS_ADULT = "isadult";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "releasedate";

    }

    public static final class TempEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEMP).build();

        // Table
        public static final String TABLE_NAME = "temp";
        //Columns
        public static final String MOVIE_ID = "movieid";
        public static final String VOTE_COUNT = "votecount";
        public static final String VIDEO = "video";
        public static final String VOTE_AVERAGE = "voteaverage";
        public static final String TITLE = "title";
        public static final String POPULARITY = "popularity";
        public static final String POSTER_PATH = "posterpath";
        public static final String ORIGINAL_LANGUAGE = "originallanguage";
        public static final String ORIGINAL_TITLE = "originaltitle";
        public static final String IS_ADULT = "isadult";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "releasedate";

    }

}
