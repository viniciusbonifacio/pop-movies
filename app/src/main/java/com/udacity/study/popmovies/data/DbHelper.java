package com.udacity.study.popmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by vinicius on 18/02/17.
 */

public class DbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "popmovies.db";
    private static final int DATABASE_VERSION = 1;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(

                MovieContract.MovieTable.CREATE_SCRIPT
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieTable.TABLE_NAME);
        onCreate(db);
    }



}
