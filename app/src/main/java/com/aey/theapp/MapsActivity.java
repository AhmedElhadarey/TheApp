package com.aey.theapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aey.theapp.util.DirectionApiHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aey.theapp.Constant.LOCATION_REQUEST_INTERVAL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;

    Location mLastLocation;
    LocationRequest mLocationRequest;

    private FusedLocationProviderClient mFusedLocationClient;


    private Location startLocation;
    private Location endLocation;

    private boolean isInTrip;

    @BindView(R.id.btn_start)
    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);


        checkLocationPermission();


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    com.google.android.gms.location.LocationCallback mLocationCallback = new com.google.android.gms.location.LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    mLastLocation = location;
                    startLocation = location;
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                    startBtn.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_REQUEST_INTERVAL);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                checkLocationPermission();
            }
        }

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);


    }


    @OnClick(R.id.btn_start)
    public void start() {
        if (!isInTrip) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(startLocation.getLatitude(), startLocation.getLongitude())).title("Start location"));
            startBtn.setText(R.string.stop_btn_message);
            Log.d(TAG, "[start] start location: " + startLocation);
            isInTrip = true;
        } else {
            // end


            final ProgressDialog progressDialog = new ProgressDialog(MapsActivity.this,
                    R.style.AppTheme_Dark_Dialog);


            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.trip_info_message));
            progressDialog.show();


            new android.os.Handler().postDelayed(

                    new Runnable() {
                        public void run() {
                            endLocation = mLastLocation;




                            Log.d(TAG, "[End] distance: " + startLocation.distanceTo(endLocation));

                            startBtn.setText(R.string.start_btn_message);
                            isInTrip = false;


                            showDirection();


                            progressDialog.dismiss();


                        }
                    }, 1000);


        }

    }


    private void showDirection() {


        DirectionApiHandler directionHandler = new DirectionApiHandler(getString(R.string.google_api_key_));

        if (!directionHandler.RequestDirection(startLocation, endLocation)) {

            // TODO: handle failure request
            return;
        }


        directionHandler.addMarkersToMap(mMap);

        String TripDetails = directionHandler.getEndLocationTitle();

        showTripDetails(TripDetails);
        Log.d(TAG, "[showDirection] Trip results   ");

    }


    private void showTripDetails(String TripDetails) {
        // ToDo : navigate to billing fragment with trip details
    }

    private void checkLocationPermission() {


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


}