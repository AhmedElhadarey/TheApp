package com.aey.theapp.util;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class andoridUtil {

    private static final String TAG = "andoridUtil";
    public static JSONObject DirectionAPiJson = new JSONObject();

    public static long ParseGoogleDirectionTime(String Result) throws JSONException {


        // 1 hour 1 min


        JSONObject json = new JSONObject();

        String[] TimeUnits = Result.split(" ");


        for (int i = 0; i < TimeUnits.length; i++) {
            if (TimeUnits[i].equals("hours") || TimeUnits[i].contains("hour"))
                json.put("hours", Integer.parseInt(TimeUnits[i - 1]));


            else if (TimeUnits[i].equals("days") || TimeUnits[i].contains("day"))
                json.put("days", Integer.parseInt(TimeUnits[i - 1]));


            else if (TimeUnits[i].equals("mins") || TimeUnits[i].contains("m") || TimeUnits[i].contains("min"))
                json.put("mins", Integer.parseInt(TimeUnits[i - 1]));

        }


        Log.d(TAG, "[TripTime] TimeUnits  " + json.toString());


        int Time = 0 ;

        if (json.has("days"))
            Time += json.getInt("days") * 24 * 60 ;

        if (json.has("hours"))
            Time += json.getInt("hours") * 60 ;


        if (json.has("mins"))
            Time += json.getInt("mins");


        DirectionAPiJson.put("Time",json);

        return Time;
    }

    public static double ParseGoogleDirectionDistance(String Result) throws JSONException {


        // 1 hour 1 min


        JSONObject json = new JSONObject();

        String[] TimeUnits = Result.split(" ");


        for (int i = 0; i < TimeUnits.length; i++) {
            if (TimeUnits[i].equals("km") || TimeUnits[i].contains("KM"))
                json.put("KM", Double.parseDouble(TimeUnits[i - 1]));


            else if (TimeUnits[i].equals("m") || TimeUnits[i].contains("meter"))
                json.put("M", Double.parseDouble(TimeUnits[i - 1]));

        }


        Log.d(TAG, "[TripTime] DistanceUnits  " + json.toString());


        double Time = 0 ;

        if (json.has("KM"))
            Time += json.getDouble("KM") ;

        if (json.has("M"))
            Time += json.getDouble("M") / 1000;



        DirectionAPiJson.put("Distance",json);

        return Time;
    }


    public static void ParseGoogleDirectionTrip(String origin , String destination ,String TimeDate) throws JSONException {


        JSONObject json = new JSONObject();

        json.put("origin",origin);
        json.put("destination",destination);
        json.put("Date",TimeDate);

        DirectionAPiJson.put("Trip",json);

    }



}