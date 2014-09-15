package com.example.googledirectionslib;

import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 12.11.13
 * Time: 17:09
 */
public class URLCreator {
    public static String getUrl(ArrayList<String> points, DirectionOption options) {
        String str_origin = "origin=" + codeName(points.get(0));

        // Destination of route
        String str_dest = "destination=" + codeName(points.get(points.size() - 1));

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";

        if (points.size() > 2)
            waypoints = "waypoints=";

        for (int i=1; i < points.size() - 1; i++) {
            String point = points.get(i);
            waypoints += codeName(point) + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + getOptions(options) + "&" + waypoints;

        // Output format
        String output = "json";


        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    public static String getUrl(LatLng current, ArrayList<String> points, DirectionOption options) {
        String str_origin = "origin=" + codeName(current.latitude + "," + current.longitude);

        // Destination of route
        String str_dest = "destination=" + codeName(points.get(points.size() - 1));

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";

        if (points.size() > 1)
            waypoints = "waypoints=";

        for (int i=0; i < points.size() - 1; i++) {
            String point = points.get(i);
            waypoints += codeName(point) + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + getOptions(options) + "&" + waypoints;

        // Output format
        String output = "json";


        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private static String getOptions(DirectionOption options) {
        StringBuilder builder = new StringBuilder("&sensor=false");
        builder.append("&mode=");
        builder.append(options.getTravelMode().toString().toLowerCase());
        builder.append("&units=");
        builder.append(options.getUnit().toString().toLowerCase());
        builder.append("&alternatives=");
        builder.append(Boolean.toString(options.isAlternatives()).toLowerCase());
        builder.append("&language=");
        builder.append(options.getLanguage());
        builder.append("&");
        return builder.toString();
    }

    private static String codeName(String s) {
        String urlEncoded = null;
        try {
            urlEncoded = URLEncoder.encode(s, "UTF-8");
            //urlEncoded = output + "?" + parameters;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlEncoded;
    }
}
