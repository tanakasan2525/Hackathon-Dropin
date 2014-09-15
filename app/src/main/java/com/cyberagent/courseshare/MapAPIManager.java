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
import io.github.axxiss.places.model.Photo;
import io.github.axxiss.places.model.Place;
import io.github.axxiss.places.request.NearbySearch;
import io.github.axxiss.places.request.PlaceSearch;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shogo on 2014/09/14.
 */
public class MapAPIManager {
    // Log.v用のタグ
    public static final String TAG = "MapApiDebug";

    public static final int IMAGE_SIZE = 150;

    private Context context;

    // Google Direction のオプション
    private DirectionOption option;

    public MapAPIManager(Context context) {
        this.context = context;
        option = new DirectionOption();
        option.setLanguage("ja");
        option.setTravelMode(DirectionOption.TravelMode.WALKING);
    }



    /**
     * 緯度経度と文字列のリストと半径を受け取りリクエスト終了時のリクエストを返すメソッド
     * @param lat
     * @param lng
     * @param searchWords
     * @param radius
     * @param listener
     */
    public void searchPlaces(double lat, double lng, ArrayList<String> searchWords, int radius, final OnEndPlaceRequestListener listener) {
        PlacesSettings.getInstance().setApiKey(context.getResources().getString(R.string.google_api_key));
        // 検索範囲の指定
        NearbySearch search = PlaceSearch.nearbySearch(lat, lng, radius);

        // 検索ワードの設定
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
                    String name = null;//名前
                    LatLng latLng = null;//座標
                    String description = null;//説明
                    List<URI> imageUri = null;//画像のURL

                    // 名前
                    name = places[i].getName();

                    // 座標
                    double lat = places[i].getGeometry().getLocation().getLat();
                    double lng = places[i].getGeometry().getLocation().getLng();
                    latLng = new LatLng(lat, lng);

                    // 画像データ
                    if (places[i].getPhotos() != null) {
                        // URIリストの作成
                        imageUri = new ArrayList<URI>();
                        // 画像のリストを走査
                        for (Photo photo : places[i].getPhotos()) {
                            // URI文字列の作成
                            String mapsUrl = "https://maps.googleapis.com/maps/api/place/photo?";
                            String maxWidth = "maxwidth=" + photo.getWidth();
                            String maxHeight = "maxheight=" + IMAGE_SIZE;
                            String ref = "photoreference=" + IMAGE_SIZE;
                            String sensor = "sensor=true";
                            String key = "key=" + context.getResources().getString(R.string.google_api_key);

                            String reqUrl = mapsUrl+maxWidth+"&"+maxHeight+"&"+ref+"&"+sensor+"&"+key;
                            Log.v(TAG+"_photo", reqUrl);

                            // URI オブジェクトの作成
                            URI uri = null;
                            try {
                                uri = new URI(reqUrl);
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                            imageUri.add(uri);
                        }

                    }

                    Spot spot = new Spot(name, latLng, description, imageUri);
                    spot.getDescription();
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

    /**
     * スタートとゴールのLatLngクラスと寄り道部分のLatLngクラスのリストを渡すと
     * 設定したコールバックにルートのLatLngクラスリストを渡すメソッド
     * @param start
     * @param goal
     * @param waypoints
     * @param listener
     */
    public void routingPlaces(LatLng start, LatLng goal, ArrayList<LatLng> waypoints, final OnEndDirectionsRequestListener listener) {
        // Google Map Directions のライブラリのインスタンス
        GoogleDirections googleDirections = new GoogleDirections();

        // ウェイポイントの座標をセットする文字列リスト
        ArrayList<String> strWaypoints = new ArrayList<String>();
        // 各座標の座標数値を文字列に変換して文字列リストに追加
        for (LatLng waypoint : waypoints) {
            String strWaypoint = waypoint.latitude + "," + waypoint.longitude;
            strWaypoints.add(strWaypoint);
        }
        // ゴールの部分の座標を文字列リストに追加
        strWaypoints.add(goal.latitude + "," + goal.longitude);

        // コールバック
        googleDirections.getRouteObjects(start, strWaypoints, this.option, new OnGetRouteListener() {
            @Override
            public void onGetRouteListener(ArrayList<Route> routes) {
                // コールバックに渡すリスト
                ArrayList<LatLng> latLngs = new ArrayList<LatLng>();

                // 各種データを入れるHashMap
                HashMap<String, Object> data = new HashMap<String, Object>();
                // 時間
                long duration = 0;
                // 距離
                long distance = 0;

                // 最短ルートのみ取得
                Route route = routes.get(0);
                // 座標間のリストを取得
                ArrayList<Leg> legs = route.getLegs();
                // 各座標間のステップのリストを取得
                for (Leg leg : legs) {

                    // データの設定
                    duration += leg.getDurationValue();
                    distance += leg.getDistanceValue();

                    // 各ステップのPolyLineを設定しコールバックに渡すリストにセット
                    ArrayList<Step> steps = leg.getSteps();
                    for (Step step : steps) {
                        latLngs.addAll(step.getPolyLine());
                    }
                }

                // 時間の設定
                data.put("duration", duration);
                // 距離の設定
                data.put("distance", distance);

                listener.onEndDirectionListener(latLngs, data);
            }
        });
    }

}

