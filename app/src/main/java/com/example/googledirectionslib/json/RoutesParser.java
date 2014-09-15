package com.example.googledirectionslib.json;

import com.example.googledirectionslib.DirectionOption;
import com.example.googledirectionslib.data.Leg;
import com.example.googledirectionslib.data.Route;
import com.example.googledirectionslib.data.Step;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 05.11.13
 * Time: 12:12
 */
public class RoutesParser implements Parser{

    @Override
    public ArrayList<Route> parse(JSONObject jObject) {

        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        ArrayList<Route> routes = new ArrayList<Route>();
        try {
            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                Route route = new Route();

                JSONObject jRoute = (JSONObject) jRoutes.get(i);
                JSONObject jBounds = jRoute.getJSONObject("bounds");
                Route.Bounds bounds = new Route.Bounds();
                bounds.northeast = jsonToLatLng(jBounds.getJSONObject("northeast"));
                bounds.southeast = jsonToLatLng(jBounds.getJSONObject("southwest"));
                route.setBounds(bounds);

                jLegs = jRoute.getJSONArray("legs");
                ArrayList<Leg> legs = new ArrayList<Leg>();

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    Leg leg= new Leg();
                    JSONObject jLeg = (JSONObject) jLegs.get(j);
                    JSONObject jDistance = jLeg.getJSONObject("distance");
                    leg.setDistanceText(jDistance.getString("text"));
                    leg.setDistanceValue(jDistance.getLong("value"));

                    JSONObject jDuration = jLeg.getJSONObject("duration");
                    leg.setDurationText(jDuration.getString("text"));
                    leg.setDurationValue(jDuration.getLong("value"));

                    leg.setStartAddress(jLeg.getString("start_address"));
                    leg.setStartLocation(jsonToLatLng(jLeg.getJSONObject("start_location")));
                    leg.setEndAddress(jLeg.getString("end_address"));
                    leg.setEndLocation(jsonToLatLng(jLeg.getJSONObject("end_location")));

                    jSteps = jLeg.getJSONArray("steps");
                    ArrayList<Step> steps = new ArrayList<Step>();

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        Step step = new Step();
                        JSONObject jStep = (JSONObject) jSteps.get(k);

                        JSONObject jStepDistance = jLeg.getJSONObject("distance");
                        step.setDistanceText(jStepDistance.getString("text"));
                        step.setDistanceValue(jStepDistance.getLong("value"));

                        JSONObject jStepDuration = jLeg.getJSONObject("duration");
                        step.setDurationText(jStepDuration.getString("text"));
                        step.setDurationValue(jStepDuration.getLong("value"));

                        step.setStartLocation(jsonToLatLng(jStep.getJSONObject("start_location")));
                        step.setEndLocation(jsonToLatLng(jStep.getJSONObject("end_location")));

                        step.setHtmlInstruction(jStep.getString("html_instructions"));
                        step.setTravelMode(DirectionOption.TravelMode.valueOf(jStep.getString("travel_mode").toUpperCase()));

                        String polyline = "";
                        polyline = (String) ((JSONObject) jStep.get("polyline")).get("points");
                        step.setPolyLine(decodePoly(polyline));

                        steps.add(step);
                    }
                    leg.setSteps(steps);
                    legs.add(leg);
                }
                route.setLegs(legs);
                routes.add(route);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    /**
     * Method to decode polylines
     */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static LatLng jsonToLatLng(JSONObject jObject) {
        try {
            LatLng latLng = new LatLng(jObject.getDouble("lat"), jObject.getDouble("lng"));
            return latLng;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
