package com.mysterlee.www;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mysterlee.www.wifi_go.R;

public class MapsActivity extends FragmentActivity implements OnMapClickListener, OnMapReadyCallback {

    private GoogleMap mMap;

    private double longitude;
    private double latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapsInitializer.initialize(getApplicationContext());


    }


    @Override
    public void onMapClick(LatLng point) {

        Point screenPt = mMap.getProjection().toScreenLocation(point);

        LatLng mlatlng = mMap.getProjection().fromScreenLocation(screenPt);

        Log.d("맵좌표", "좌표: 위도(" + String.valueOf(point.latitude) + "), 경도(" + String.valueOf(point.longitude) + ")");
        Log.d("화면좌표", "화면좌표: X(" + String.valueOf(screenPt.x) + "), Y(" + String.valueOf(screenPt.y) + ")");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapsActivity.this);


        GpsInfo gps = new GpsInfo(MapsActivity.this);

        if(gps.isGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getongitude();

            LatLng mlatLng = new LatLng(latitude, longitude);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(mlatLng));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

            MarkerOptions optFirst = new MarkerOptions();
            optFirst.position(mlatLng);
            optFirst.title("현위치");
            optFirst.snippet("Snippet");
            //optFirst.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
            mMap.addMarker(optFirst).showInfoWindow();
        }

    }
}
