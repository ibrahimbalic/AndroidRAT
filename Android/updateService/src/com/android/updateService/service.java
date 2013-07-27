package com.android.updateService;

import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class service extends Service implements LocationListener {


    public static final String PREFS_NAME = "Update";
    public static String WL_TAG = "WK";
    PowerManager pm = null;
    PowerManager.WakeLock xwk = null;
    LocationManager locationManager;
    int lat, lng;
    String coordinates = null;
    String latitude, longitude;
    static SharedPreferences Sharedpref;

    private Handler handler = new Handler();
    long delay = 20*1000;

    Timer timer = new Timer();

    public String getSession(String key) {
        Sharedpref = getSharedPreferences(PREFS_NAME, 0);
        return Sharedpref.getString(key, "").toString();
    }
    public void setSession(String key,String value) {
        Sharedpref = this.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor reg = Sharedpref.edit();
        reg.putString(key,value);
        reg.commit();
    }

    public void onDestroy()
    {

        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    public int onStartCommand(Intent intent, int flags, int startId){

        onStart(intent,startId);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }else{
          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        return START_STICKY;

    }
    public void onStart(Intent paramIntent, int paramInt)
    {
        super.onStart(paramIntent, paramInt);
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.xwk = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "AndroidUpdate");
        this.xwk.acquire();
        timer.cancel();
        timer = new Timer();
        Date executionDate = new Date();

         class LoopTask extends TimerTask {
             public void run() {
                 handler.post(new Runnable() {
                     public void run() {

                         UpdateServiceCnt task = new UpdateServiceCnt(service.this);
                         task.execute();
                     }
                 });
            }
        }


        timer.scheduleAtFixedRate( new LoopTask(), executionDate, delay);
    }
    public void onLocationChanged(Location location) {

        lat = (int) (location.getLatitude() * 1E6);
        lng = (int) (location.getLongitude() * 1E6);

        latitude = Integer.toString(lat);
        longitude = Integer.toString(lng);
        coordinates = "Coordinates:" + latitude + ", " + longitude;
        setSession("Coordinates",coordinates);
    }

    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

}

