package com.mysterlee.www;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by aucun on 2017-04-22.
 */

public class GpsInfo extends Service implements LocationListener{

    private final Context mContext;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean isGetLocation = false;

    Location mlocation;
    double lat;
    double lon;

    private static final long MIN_DISTANCE_UPDATES = 10;

    private static final long MIN_TIME_UPDATES = 1000 * 1;

    protected LocationManager mlocationManager;

    public GpsInfo(Context mContext) {
        this.mContext = mContext;

        getLocation();
    }

    public Location getLocation() {
        try {
            Toast.makeText(getApplicationContext(), "GGPPSS", Toast.LENGTH_LONG).show();

            mlocationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = mlocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled || isNetworkEnabled){
                this.isGetLocation = true;
                if (isNetworkEnabled){
                    mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_UPDATES, this);

                    if (mlocationManager != null){
                        mlocation = mlocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (mlocation != null){
                            lat = mlocation.getLatitude();
                            lon = mlocation.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled){
                    if (mlocation == null){
                        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_UPDATES, this);

                        if (mlocation != null) {
                            mlocation = mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (mlocation != null){
                                lat = mlocation.getLatitude();
                                lon = mlocation.getLongitude();
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return mlocation;
    }

    public void stopUsingGPS(){
        if (mlocationManager != null){
            mlocationManager.removeUpdates(GpsInfo.this);
        }
    }

    public double getLatitude(){
        if (mlocation != null){
            lat = mlocation.getLatitude();
        }
        return lat;
    }

    public double getongitude(){
        if (mlocation != null){
            lon = mlocation.getLongitude();
        }
        return lon;
    }

    public boolean isGetLocation(){
        return this.isGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder malertDialog = new AlertDialog.Builder(mContext);

        malertDialog.setTitle("GPS 사용설정");
        malertDialog.setMessage("GPS설정이 되진 않은것 같습니다.\n설정창으로 이동하시겠습니까?");

        malertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
        malertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
        malertDialog.show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
