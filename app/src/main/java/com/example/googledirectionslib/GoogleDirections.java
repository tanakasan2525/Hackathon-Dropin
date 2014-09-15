package com.example.googledirectionslib;

import com.example.googledirectionslib.async.DownloadTask;
import com.example.googledirectionslib.data.Leg;
import com.example.googledirectionslib.data.Route;
import com.example.googledirectionslib.json.RoutesParser;
import com.example.googledirectionslib.listeners.*;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 12.11.13
 * Time: 17:09
 */
public class GoogleDirections {
    public void getTime(ArrayList<String> points,DirectionOption options,final OnGetTimeListener listener){
        String url = URLCreator.getUrl(points,options);

        DownloadTask downloadTask = new DownloadTask(new RoutesParser(),new BaseListener() {
            @Override
            public void onWorkDone(Object result) {
                if(result instanceof ArrayList){
                    ArrayList<Route> routes = (ArrayList<Route>) result;
                    long time = 0;
                    for(Route route : routes){
                        for(Leg leg : route.getLegs()){
                            time += leg.getDurationValue();
                        }
                    }
                    listener.onGetTimeListener(time);
                } else throw new ClassCastException();
            }
        });
        downloadTask.execute(url);
    }

    public void getDistance(ArrayList<String> points,DirectionOption options,final OnGetDistanceListener listener){
        String url = URLCreator.getUrl(points,options);

        DownloadTask downloadTask = new DownloadTask(new RoutesParser(),new BaseListener() {
            @Override
            public void onWorkDone(Object result) {
                if(result instanceof ArrayList){
                    ArrayList<Route> routes = (ArrayList<Route>) result;
                    long distance = 0;
                    for(Route route : routes){
                        for(Leg leg : route.getLegs()){
                            distance += leg.getDistanceValue();
                        }
                    }
                    listener.onGetDistanceListener(distance);
                } else throw new ClassCastException();
            }
        });
        downloadTask.execute(url);
    }

    public void getTimeDistance(LatLng current, ArrayList<String> points,DirectionOption options,final OnGetTimeDistanceListener listener){
        String url = URLCreator.getUrl(current,points,options);

        DownloadTask downloadTask = new DownloadTask(new RoutesParser(),new BaseListener() {
            @Override
            public void onWorkDone(Object result) {
                if(result instanceof ArrayList){
                    ArrayList<Route> routes = (ArrayList<Route>) result;
                    long distance = 0;
                    long time = 0;
                    for(Route route : routes){
                        for(Leg leg : route.getLegs()){
                            distance += leg.getDistanceValue();
                            time += leg.getDurationValue();
                        }
                    }
                    listener.onGetTimeDistanceListener(time, distance);
                } else throw new ClassCastException();
            }
        });
        downloadTask.execute(url);
    }

    public void getRouteObjects(LatLng current, ArrayList<String> points,DirectionOption options,final OnGetRouteListener listener){
        String url = URLCreator.getUrl(current,points,options);

        DownloadTask downloadTask = new DownloadTask(new RoutesParser(),new BaseListener() {
            @Override
            public void onWorkDone(Object result) {
                if(result instanceof ArrayList){
                    listener.onGetRouteListener((ArrayList<Route>) result);
                } else throw new ClassCastException();
            }
        });
        downloadTask.execute(url);
    }
}
