package com.android.updateService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{

    public static final String DATABASE_NAME = "AndroidUpdate";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        String sql = "CREATE TABLE IF NOT EXISTS sUpdates (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "taskName INTEGER, " +
                "taskUID STRING, " +
                "taskStat INTEGER DEFAULT 1, " +
                "taskDate timestamp not null default current_timestamp);";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}