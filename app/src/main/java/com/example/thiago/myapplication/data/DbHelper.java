package com.example.thiago.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 16/09/2017.
 */

public class DbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "movies.db";

    private static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE "  + Contract.FavoritesEntry.TABLE_NAME + " (" +
                Contract.FavoritesEntry._ID                 + " INTEGER PRIMARY KEY, " +
                Contract.FavoritesEntry.MOVIE_ID            + " INT NOT NULL," +
                Contract.FavoritesEntry.VOTE_COUNT          + " TEXT NOT NULL," +
                Contract.FavoritesEntry.VIDEO               + " TEXT NOT NULL," +
                Contract.FavoritesEntry.VOTE_AVERAGE        + " TEXT NOT NULL," +
                Contract.FavoritesEntry.TITLE               + " TEXT NOT NULL," +
                Contract.FavoritesEntry.POPULARITY          + " TEXT NOT NULL," +
                Contract.FavoritesEntry.POSTER_PATH         + " TEXT NOT NULL," +
                Contract.FavoritesEntry.ORIGINAL_LANGUAGE   + " TEXT NOT NULL," +
                Contract.FavoritesEntry.ORIGINAL_TITLE      + " TEXT NOT NULL," +
                Contract.FavoritesEntry.IS_ADULT            + " INT NOT NULL," +
                Contract.FavoritesEntry.OVERVIEW            + " TEXT NOT NULL," +
                Contract.FavoritesEntry.RELEASE_DATE        + " TEXT NOT NULL);";

        Log.i("Table favorites","created");


        final String CREATE_TABLE_TEMP = "CREATE TABLE "  + Contract.TempEntry.TABLE_NAME + " (" +
                Contract.TempEntry._ID                 + " INTEGER PRIMARY KEY, " +
                Contract.TempEntry.MOVIE_ID            + " INT NOT NULL," +
                Contract.TempEntry.VOTE_COUNT          + " TEXT NOT NULL," +
                Contract.TempEntry.VIDEO               + " TEXT NOT NULL," +
                Contract.TempEntry.VOTE_AVERAGE        + " TEXT NOT NULL," +
                Contract.TempEntry.TITLE               + " TEXT NOT NULL," +
                Contract.TempEntry.POPULARITY          + " TEXT NOT NULL," +
                Contract.TempEntry.POSTER_PATH         + " TEXT NOT NULL," +
                Contract.TempEntry.ORIGINAL_LANGUAGE   + " TEXT NOT NULL," +
                Contract.TempEntry.ORIGINAL_TITLE      + " TEXT NOT NULL," +
                Contract.TempEntry.IS_ADULT            + " INT NOT NULL," +
                Contract.TempEntry.OVERVIEW            + " TEXT NOT NULL," +
                Contract.TempEntry.RELEASE_DATE        + " TEXT NOT NULL);";

        Log.i("Table temp","created");
        db.execSQL(CREATE_TABLE_TEMP);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
