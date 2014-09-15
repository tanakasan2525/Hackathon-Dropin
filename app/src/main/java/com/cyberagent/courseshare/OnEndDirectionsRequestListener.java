package com.cyberagent.courseshare;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shogo on 2014/09/15.
 */
public interface OnEndDirectionsRequestListener {
    abstract public void onEndDirectionListener(ArrayList<LatLng> latLngs, HashMap<String, Object> data);
}
