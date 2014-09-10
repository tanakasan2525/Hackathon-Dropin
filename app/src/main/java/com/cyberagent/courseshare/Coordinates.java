package com.cyberagent.courseshare;

/**
 * Created by shogo on 2014/09/10.
 */
public class Coordinates {
    private double latitude;    //緯度
    private double longitude;   //軽度

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
