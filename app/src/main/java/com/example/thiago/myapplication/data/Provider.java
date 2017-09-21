package com.example.thiago.myapplication.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * Created by admin on 16/09/2017.
 */

public class Provider extends ContentProvider {

    public static final int FAVORITES = 100;
    public static final int FAVORITE_WITH_ID = 101;
    public static final int TEMP = 102;
    public static final int TEMP_WITH_ID = 103;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private DbHelper mDbHelper;


    private static final UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
            uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_FAVORITES, FAVORITES);
            uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_FAVORITES + "/#", FAVORITE_WITH_ID);
            uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_TEMP, TEMP);
            uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_TEMP + "/#", TEMP_WITH_ID);

        return uriMatcher;

    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new DbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

            final SQLiteDatabase db = mDbHelper.getReadableDatabase();

            int match = sUriMatcher.match(uri);
            Cursor retCursor;

            switch (match) {
                case FAVORITES:
                    retCursor =  db.query(Contract.FavoritesEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                    break;
                case TEMP:

                    retCursor =  db.query(Contract.TempEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            retCursor.setNotificationUri(getContext().getContentResolver(), uri);

            return retCursor;
    }

    private void createTable(SQLiteDatabase db) {
        final String CREATE_TABLE_TEMP = "CREATE TABLE IF NOT EXISTS "  + Contract.TempEntry.TABLE_NAME + " (" +
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
        db.execSQL(CREATE_TABLE_TEMP);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        createTable(db);

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITES:
                long id = db.insert(Contract.FavoritesEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(Contract.FavoritesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case TEMP:
                long idt = db.insert(Contract.TempEntry.TABLE_NAME, null, values);
                if ( idt > 0 ) {
                    returnUri = ContentUris.withAppendedId(Contract.TempEntry.CONTENT_URI, idt);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case FAVORITES:
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        Contract.FavoritesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            case TEMP:
                createTable(mDbHelper.getWritableDatabase());
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        Contract.TempEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
