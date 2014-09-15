package com.example.googledirectionslib.data;

import com.example.googledirectionslib.DirectionOption;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 12.11.13
 * Time: 17:59
 * Minimal object in route
 */
public class Step {
    private DirectionOption.TravelMode travelMode;
    private LatLng startLocation, endLocation;
    private List<LatLng> polyLine;
    private long durationValue;
    private String durationText;
    private String htmlInstruction;
    private long distanceValue;
    private String distanceText;

    public DirectionOption.TravelMode getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(DirectionOption.TravelMode travelMode) {
        this.travelMode = travelMode;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public List<LatLng> getPolyLine() {
        return polyLine;
    }

    public void setPolyLine(List<LatLng> polyLine) {
        this.polyLine = polyLine;
    }

    public long getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(long durationValue) {
        this.durationValue = durationValue;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public String getHtmlInstruction() {
        return htmlInstruction;
    }

    public void setHtmlInstruction(String htmlInstruction) {
        this.htmlInstruction = htmlInstruction;
    }

    public long getDistanceValue() {
        return distanceValue;
    }

    public void setDistanceValue(long distanceValue) {
        this.distanceValue = distanceValue;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }
}
