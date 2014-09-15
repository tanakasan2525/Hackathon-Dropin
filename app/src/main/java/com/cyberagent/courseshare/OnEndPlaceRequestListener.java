package com.cyberagent.courseshare;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by shogo on 2014/09/14.
 */
public interface OnEndPlaceRequestListener {
    abstract public void onEndRequestListener(ArrayList<Spot> spots);
}
