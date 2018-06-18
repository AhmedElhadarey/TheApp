package com.aey.theapp.util;

import android.util.Log;

import com.aey.theapp.Constant;


public class FareHandler {

    private static final String TAG = "FareHandler";

    private long StartTime ;
    private long EndTime ;
    private double CostPerKillo;
    private double CostPerMin;


    public FareHandler()
    {

        StartTime = -1 ;
        EndTime = -1 ;

        CostPerKillo = Constant.fAREPerKM ;
        CostPerMin =Constant.FAREPerMin ;

    }


    public long getStartTime() {
        return StartTime;
    }

    public void setStartTime(long startTime) {
        StartTime = startTime;
    }

    public long getEndTime() {
        return EndTime;
    }

    public void setEndTime(long endTime) {
        EndTime = endTime;
    }


    public double estimateTripCoste(double TripDistance , double TripTime ){

        return TripDistance*CostPerKillo  + TripTime * CostPerMin ;

    }


    public double CalculateActualCost(double TripDistance ) throws Exception {

        if ( StartTime ==-1 || EndTime == -1 )
            throw  new Exception("Trip Time is Not setup yet ");


        double TripTime = Math.ceil( (double) (EndTime - StartTime)  / (double) (1000*60)) ;


        Log.d(TAG, "[TripTime] TripTime  start " + StartTime );
        Log.d(TAG, "[TripTime] TripTime end " + EndTime );
        Log.d(TAG, "[TripTime] TripTime " + TripTime );


        return TripDistance*CostPerKillo  + TripTime * CostPerMin ;



    }


}
