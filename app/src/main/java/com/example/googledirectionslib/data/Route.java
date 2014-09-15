package com.example.googledirectionslib.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 12.11.13
 * Time: 18:09
 * Class of one Route
 */
public class Route {
    public static class Bounds{
        public LatLng northeast;
        public LatLng southeast;
    }

    private Bounds bounds;
    private ArrayList<Leg> legs;
    private ArrayList<LatLng> overviewPolyline;

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public ArrayList<Leg> getLegs() {
        return legs;
    }

    public void setLegs(ArrayList<Leg> legs) {
        this.legs = legs;
    }

    public ArrayList<LatLng> getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(ArrayList<LatLng> overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }
}
