package com.mysterlee.www;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mysterlee.www.wifi_go.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.mysterlee.www.wifi_go.R.id.map;

public class NaviActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private boolean mLocationPermission = false;

    private LocationRequest mLocetionRequset;
    private Location mCurrentLocation;
    private static final String CAMERA_POSITION = "camra_position";
    private static final String LOCATION = "location";
    private CameraPosition mCameraPosition;

    private String num;

    String myJson;
    private WebView webView;


    private boolean myLocatCH = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            mCurrentLocation = savedInstanceState.getParcelable(LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(CAMERA_POSITION);
        }

        //setContentView(R.layout.activity_maps);

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");




    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_state) {
            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            intent.putExtra("num", num);
            startActivity(intent);
        }/*
        else if (id == R.id.nav_quest) {
            Intent intent = new Intent(getApplicationContext(), QuestActivity.class);
            intent.putExtra("num", num);
            startActivity(intent);
        }*/
        else if (id == R.id.nav_board) {
            Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
            intent.putExtra("num", num);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressWarnings("MissingPermission")
    private void updatesLocation() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocetionRequset, this);
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        insertToDatabase(num);

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);


        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mCurrentLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mCurrentLocation.getLatitude(),
                            mCurrentLocation.getLongitude()), 16));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.15660555390101, 128.10578774660829), 16));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }


        updateLocationUI();


        // Add a marker in Sydney and move the camera

    }

    @SuppressWarnings("MissingPermission")
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(NaviActivity.this, "권한 확인", Toast.LENGTH_SHORT).show();

            mLocationPermission = true;
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            updatesLocation();
            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocetionRequset, this);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(NaviActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };

    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
    }


    @SuppressWarnings("MissingPermission")
    private void updateLocationUI() {

        if (mMap == null) return;






        //if (mLocationPermission) {
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    myLocatCH = true;
                    return false;
                }
            });
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        /*} else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }*/

    }

    private void createLocationRequest() {
        mLocetionRequset = new LocationRequest();
        mLocetionRequset.setInterval(10000); //ms
        mLocetionRequset.setFastestInterval(5000);
        mLocetionRequset.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //위치정확도 or 배터리 중요도

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (mLocationPermission != true) {
            new TedPermission(this)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .check();
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            updatesLocation();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {


        if (myLocatCH == true) {

            if (((mMap.getCameraPosition().target.latitude > (location.getLatitude() + 0.0001))
                    || (mMap.getCameraPosition().target.latitude < (location.getLatitude() - 0.0001)))
                    && (mMap.getCameraPosition().target.longitude > (location.getLongitude() + 0.0001))
                    || (mMap.getCameraPosition().target.longitude < (location.getLongitude() - 0.00001))) {
                myLocatCH = false;
            } else {
                mCurrentLocation = location;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (mMap != null) {
            outState.putParcelable(CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(LOCATION, mCurrentLocation);
            super.onSaveInstanceState(outState);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    protected void onResume() {

        if (mGoogleApiClient.isConnected()) {
            if(mLocationPermission != true) {
                new TedPermission(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                        .check();
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                updatesLocation();
            }
        }
        super.onResume();
    }

    //하이여 테스트입니다.
    ///테스트2
    protected void makeMarker() {
        try {

            JSONObject jsonObj = new JSONObject(myJson);
            JSONArray var = jsonObj.getJSONArray("wifi");

            int no = var.length();

            final Intent intent = new Intent(this, WifiActivity.class);

            for (int j = 0; j < no; j++) {
                JSONObject c = var.getJSONObject(j);

                String name = c.getString("name");
                String con = c.getString("con");
                String reward = c.getString("pass");

                Double n = c.getDouble("n");
                Double e = c.getDouble("e");


                if (c.getString("var").equals("빨강"))
                    mMap.addMarker(new MarkerOptions().position(new LatLng(n, e)).title(name).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("red",70,100))));
                else if (c.getString("var").equals("노랑") && c.getString("user").equals(num))
                    mMap.addMarker(new MarkerOptions().position(new LatLng(n, e)).title(name).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("yellow",70,100))));
                else if (c.getString("var").equals("파랑"))
                    mMap.addMarker(new MarkerOptions().position(new LatLng(n, e)).title(name).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("blue",70,100))));
                else if (c.getString("var").equals("초록"))
                    mMap.addMarker(new MarkerOptions().position(new LatLng(n, e)).title(name).icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("green",70,100))));


                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {


                        intent.putExtra("num", num);
                        intent.putExtra("lat", marker.getPosition().latitude);
                        intent.putExtra("lon", marker.getPosition().longitude);
                        startActivity(intent);
                        return false;
                    }
                });


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    private void insertToDatabase(String num) {

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPostExecute(String s) {

                myJson = s;
                makeMarker();
            }

            @Override
            protected String doInBackground(String... params) {

                try {
                    String num = (String) params[0];

                    String link = "https://www.mysterlee.com/wifigo/wifi.php";
                    String data = URLEncoder.encode("no", "UTF-8") + "=" + URLEncoder.encode(num, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }


                    return sb.toString().trim();

                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

        }

        InsertData task = new InsertData();
        task.execute(num);

    }



    @Override
    public void onMapLongClick(LatLng latLng) {

        Intent intent = new Intent(this, MakerActivity.class);
        intent.putExtra("lat", latLng.latitude);
        intent.putExtra("lon", latLng.longitude);
        intent.putExtra("num", num);
        startActivity(intent);
    }



    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(NaviActivity.this, "클릭", Toast.LENGTH_SHORT).show();
    }
}
