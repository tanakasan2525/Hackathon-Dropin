package com.cyberagent.courseshare;

import java.util.ArrayList;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by shogo on 2014/09/10.
 */
public class Course {
    /**
     * コースクラス
     */
    private String name;
    private ArrayList<LatLng> coordinatesList;

    public Course() {
        coordinatesList = new ArrayList<LatLng>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<LatLng> getCoordinatesList() {
        return coordinatesList;
    }

    public void addCoordinatesToList(double latitude, double longitude) {
        LatLng coordinates = new LatLng(latitude, longitude);
        coordinatesList.add(coordinates);
    }

}
