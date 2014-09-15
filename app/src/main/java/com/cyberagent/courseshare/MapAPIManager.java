package com.cyberagent.courseshare;

import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import com.example.googledirectionslib.DirectionOption;
import com.example.googledirectionslib.GoogleDirections;
import com.example.googledirectionslib.data.Leg;
import com.example.googledirectionslib.data.Route;
import com.example.googledirectionslib.data.Step;
import com.example.googledirectionslib.listeners.OnGetRouteListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import io.github.axxiss.places.PlacesSettings;
import io.github.axxiss.places.Response;
import io.github.axxiss.places.callback.PlacesCallback;
import io.github.axxiss.places.model.Event;
import io.github.axxiss.places.model.Place;
import io.github.axxiss.places.request.NearbySearch;
import io.github.axxiss.places.request.PlaceSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shogo on 2014/09/14.
 */
public class MapAPIManager {
    // Log.v用のタグ
    private static final String TAG = "MapApiDebug";

    private Context context;

    // Google Direction のオプション
    private DirectionOption option;

    // 検索時の半径(m)
    private static int SEARCH_RADIUS;

    public MapAPIManager(Context context) {
        this.context = context;
        option = new DirectionOption();
        option.setLanguage("ja");
        option.setTravelMode(DirectionOption.TravelMode.WALKING);
    }

    // 半径を設定するメソッド
    public static void setRadius(int radius) {
        SEARCH_RADIUS = radius;
    }


    /**
     * 緯度経度と文字列のリストを受け取りリクエスト終了時のリクエストを返すメソッド
     * @param lat
     * @param lng
     * @param searchWords
     * @param listener
     */
    public void searchPlaces(double lat, double lng, ArrayList<String> searchWords, final OnEndPlaceRequestListener listener) {
        PlacesSettings.getInstance().setApiKey(context.getResources().getString(R.string.google_maps_key));
        // 検索範囲の指定
        NearbySearch search = PlaceSearch.nearbySearch(lat, lng, SEARCH_RADIUS);

        for (String word : searchWords) {
            search.setKeyword(word);
            Log.v(TAG, "word:" + word);
        }

        // リクエストの送信
        search.sendRequest(new PlacesCallback() {
            @Override
            public void onSuccess(Response response) {
                Log.v(TAG, "Request success");
                // Listenerに渡すリスト
                ArrayList<Spot> spots = new ArrayList<Spot>();
                Place[] places = response.getResults();
                for (int i = 0; i < places.length; i++) {
                    // 名前
                    String name = places[i].getName();
                    // 座標
                    double lat = places[i].getGeometry().getLocation().getLat();
                    double lng = places[i].getGeometry().getLocation().getLng();
                    LatLng latLng = new LatLng(lat, lng);
                    // サマリー
                    List<Event> events = places[i].getEvents();

                    Spot spot = new Spot(name, latLng);
                    spots.add(spot);

                }

                listener.onEndRequestListener(spots);
            }

            @Override
            public void onException(Exception exception) {
                exception.printStackTrace();
                Log.v(TAG, "Request error");
            }
        });
    }

    public void routingPlaces(LatLng start, LatLng goal, ArrayList<LatLng> waypoints, final OnEndDirectionsRequestListener listener) {
        // Google Map Directions のライブラリのインスタンス
        GoogleDirections googleDirections = new GoogleDirections();

        ArrayList<String> strWaypoints = new ArrayList<String>();

        // 座標の文字列リストを生成
        for (LatLng waypoint : waypoints) {
            String strWaypoint = waypoint.latitude + "," + waypoint.longitude;
            strWaypoints.add(strWaypoint);
        }
        strWaypoints.add(goal.latitude + "," + goal.longitude);

        // コールバック
        googleDirections.getRouteObjects(start, strWaypoints, this.option, new OnGetRouteListener() {
            @Override
            public void onGetRouteListener(ArrayList<Route> routes) {
                // コールバックに渡すリスト
                ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
                // 最短ルートのみ取得
                Route route = routes.get(0);
                // 区切りのリストを取得
                ArrayList<Leg> legs = route.getLegs();
                Log.v(TAG, legs.size()+"");
                for (Leg leg : legs) {
                    ArrayList<Step> steps = leg.getSteps();
                    Log.v(TAG, steps.size()+"");
                    for (Step step : steps) {
                        latLngs.addAll(step.getPolyLine());
                        Log.v(TAG, step.getPolyLine().toString());
                    }
                }
                Log.v(TAG, latLngs.size()+"");
                listener.onEndDirectionListener(latLngs);
            }
        });
    }

}

