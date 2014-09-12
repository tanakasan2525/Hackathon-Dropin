package com.cyberagent.courseshare;

import java.util.ArrayList;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by shogo on 2014/09/10.
 */
public class Course {
    private String name;
    private ArrayList<LatLng> coordinatesList;
	private ArrayList<Spot> spotList;

    public Course(String name) {
		this.name = name;
		this.coordinatesList = new ArrayList<LatLng>();
		this.spotList = new ArrayList<Spot>();
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

	public ArrayList<Spot> getSpotList() {
		return this.spotList;
	}

    public void addCoordinatesToList(double latitude, double longitude) {
        LatLng coordinates = new LatLng(latitude, longitude);
        coordinatesList.add(coordinates);
    }

	public void addSpot(Spot spot) {
		this.spotList.add(spot);
	}

}

