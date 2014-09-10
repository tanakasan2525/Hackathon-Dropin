package com.cyberagent.courseshare;

import java.util.ArrayList;

/**
 * Created by shogo on 2014/09/10.
 */
public class Course {
    private String name;
    private ArrayList<Coordinates> coordinatesList;

    public Course() {
        coordinatesList = new ArrayList<Coordinates>();
    }

    public void addCoordinatesToList(double latitude, double longitude) {
        Coordinates coordinates = new Coordinates(latitude, longitude);
        coordinatesList.add(coordinates);
    }

}
