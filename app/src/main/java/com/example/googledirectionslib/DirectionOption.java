package com.example.googledirectionslib;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 12.11.13
 * Time: 17:09
 *
 * Class for option route
 */
public class DirectionOption {
    public enum TravelMode{
        DRIVING, WALKING, BICYCLING, TRANSIT
    }

    public enum AvoidType{
        TOLLS,HIGHWAYS,NOT_STAND
    }

    public enum UnitSystem{
        METRIC,IMPERIAL
    }

    private TravelMode travelMode = TravelMode.DRIVING;
    private boolean isAlternatives = false;
    private AvoidType avoid = AvoidType.NOT_STAND;
    private UnitSystem unit = UnitSystem.METRIC;
    private String language = "en";
    /*private Date departureTime;
    private Date arrivalTime;    */

    public TravelMode getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(TravelMode travelMode) {
        this.travelMode = travelMode;
    }

    public boolean isAlternatives() {
        return isAlternatives;
    }

    public void setAlternatives(boolean alternatives) {
        isAlternatives = alternatives;
    }

    public AvoidType getAvoid() {
        return avoid;
    }

    public void setAvoid(AvoidType avoid) {
        this.avoid = avoid;
    }

    public UnitSystem getUnit() {
        return unit;
    }

    public void setUnit(UnitSystem unit) {
        this.unit = unit;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
