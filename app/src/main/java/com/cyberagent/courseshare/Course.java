package com.cyberagent.courseshare;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shogo on 2014/09/10.
 */
public class Course implements Serializable{
    /**
     * コースクラス
     */
    private String name;
    private ArrayList<Coordinates> coordinatesList;

    public Course() {
        coordinatesList = new ArrayList<Coordinates>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Coordinates> getCoordinatesList() {
        return coordinatesList;
    }

    public void addCoordinatesToList(double latitude, double longitude) {
        Coordinates coordinates = new Coordinates(latitude, longitude);
        coordinatesList.add(coordinates);
    }

}
