package com.android.updateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.PowerManager;
import android.util.Log;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class main extends BroadcastReceiver
{

    protected SQLiteDatabase db;
    @Override
    public void onReceive(Context context, Intent intent)
    {
      db = (new DatabaseHelper(context)).getWritableDatabase();
      // db.delete("sUpdates", "_id<?", new String[] {"100"});
        Cursor cursor = this.db.query("sUpdates", new String[] { "taskUID"},null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
             //   Log.d("aa",cursor.getString(0));
            } while (cursor.moveToNext());
        }

      Intent x = new Intent(context , service.class);
      context.startService(x);
    }

    public String getTlp(String Str) {
        String Xt = null;
        for (int i = 0; i < Str.length() ; i++) {
            char c = Str.charAt(0);
            Xt += getChr(c);
        }
        return  Xt;
    }
    public static String getChr(char chr)
    {
        return String.format("%x", (int) chr);
    }
}

