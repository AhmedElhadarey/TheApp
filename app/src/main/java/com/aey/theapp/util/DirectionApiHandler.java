package com.aey.theapp.util;


import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.aey.theapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;

import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class DirectionApiHandler {


    private static final String TAG = "DirectionApiHandler";


    private GeoApiContext GeoContext;

    private DirectionsApiRequest req;

    private DirectionsResult directionsResult;

    private List<LatLng> Direction_polylines;

    public DirectionApiHandler(String API_KEY) {


        GeoContext = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();


        Direction_polylines = new ArrayList();


    }


    public boolean RequestDirection(Location origin_location, Location Destination_location) {


        String origin = String.valueOf(origin_location.getLatitude()) + "," + origin_location.getLongitude();


        String destination = String.valueOf(Destination_location.getLatitude()) + "," + Destination_location.getLongitude();


         req = DirectionsApi.getDirections(GeoContext, origin, destination);

        try {

            DateTime now = new DateTime();

            directionsResult = req.departureTime(now)
                    .await();

            Log.d(TAG, "[RequestDirection] res.routes.length  " + directionsResult.routes.length);


            if (directionsResult.routes != null && directionsResult.routes.length > 0) {
                DirectionsRoute route = directionsResult.routes[0];


                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                Direction_polylines.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }


                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            Direction_polylines.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }

        return Direction_polylines.size() > 0;
    }




    public void addMarkersToMap(GoogleMap mMap) {

        PolylineOptions opts = new PolylineOptions().addAll(Direction_polylines).color(Color.BLACK).width(12);
        mMap.addPolyline(opts);


        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setTrafficEnabled(false);


        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(directionsResult.routes[0].legs[0].startLocation.lat, directionsResult.routes[0].legs[0].startLocation.lng))
                .title(directionsResult.routes[0].legs[0].startAddress));


        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(directionsResult.routes[0].legs[0].endLocation.lat, directionsResult.routes[0].legs[0].endLocation.lng))
                .title(directionsResult.routes[0].legs[0].startAddress)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dest_marker)));
    }

    public String getEndLocationTitle()

    {

        return directionsResult.routes[0].legs[0].duration.humanReadable +

                "|" + directionsResult.routes[0].legs[0].distance.humanReadable;
    }


}