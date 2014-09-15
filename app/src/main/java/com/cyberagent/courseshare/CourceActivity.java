package com.cyberagent.courseshare;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


/**
 * コース画面のアクティビティ
 */
public class CourceActivity extends FragmentActivity {

    private GoogleMap map; // Might be null if Google Play services APK is not available.

	private Course course;

	private ArrayList<Marker> markerList;

	LayoutInflater layoutFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.markerList = new ArrayList<Marker>();

		layoutFactory = LayoutInflater.from(this);
		// テスト用ダミーデータ
		course = new Course("ダミーコース");
		course.addCoordinatesToList(35.65787,139.698066);  // 渋谷マークシティ
		course.addCoordinatesToList(35.6587292,139.7000174);
		course.addCoordinatesToList(35.6588081,139.7005457);
		course.addCoordinatesToList(35.6585071,139.7010799);
		course.addCoordinatesToList(35.6585152,139.7013252);
		course.addCoordinatesToList(35.658517, 139.701334);   // 渋谷駅
		course.addCoordinatesToList(35.6586415, 139.701594);
		course.addCoordinatesToList(35.6591188, 139.7037352); // 渋谷ヒカリエ
		course.addCoordinatesToList(35.6590627, 139.7041079);
		course.addCoordinatesToList(35.6593868, 139.7051022);
		course.addCoordinatesToList(35.6599376, 139.7055483);
		course.addCoordinatesToList(35.6600695, 139.7057102);
		course.addCoordinatesToList(35.6603081, 139.7057727);
		course.addCoordinatesToList(35.6607825, 139.7066618);
		course.addCoordinatesToList(35.6613866, 139.7075792);
		course.addCoordinatesToList(35.6615811, 139.7078944);
		course.addCoordinatesToList(35.6613542, 139.7082438);
		course.addCoordinatesToList(35.6619168, 139.7090568); //　青山学院大学

		course.addSpot(new Spot("渋谷マークシティ", new LatLng(35.65787,139.698066),
				"サイバーエージェントのオフィスがあります。"));
		course.addSpot(new Spot("渋谷駅", new LatLng(35.658517,139.701334),
				"JR東日本、東急、東京メトロの駅です。"));
		course.addSpot(new Spot("渋谷ヒカリエ", new LatLng(35.6591188,139.7037352),
				"レストラン街、劇場、シェアオフィス、一般オフィスなどがあります。"));
		course.addSpot(new Spot("青山学院大学", new LatLng(35.66147,139.709464),
				"青山学院大学の文系が集まるキャンパスです。"));


        /*
        PlacesSettings.getInstance().setApiKey(getResources().getString(R.string.google_maps_key));
        PlacesSettings.getInstance().setApiKey("AIzaSyAN_B0_xjV4FP4_POmqV8djx_ALEdgpBtc");
        NearbySearch search = PlaceSearch.nearbySearch(35.658517,139.701334, 2000);
        search.setKeyword("ガスト");

        search.sendRequest(new PlacesCallback() {
            @Override
            public void onSuccess(Response response) {
                Log.v("req_debug", "request success");
            }

            @Override
            public void onException(Exception exception) {
                Log.v("req_debug", "request exception");

            }
        });
        */



        MapAPIManager manager = new MapAPIManager(map);
        MapAPIManager.setRadius(2000);

        ArrayList<String> keywords = new ArrayList<String>();
        keywords.add("ガスト");
        manager.searchPlaces(35.658517, 139.701334, keywords, new OnEndPlaceRequestListener() {

            @Override
            public void onEndRequestListener(ArrayList<Spot> spots) {

            }
        });

        LatLng start = new LatLng(35.65787,139.698066);
        ArrayList<LatLng> waypoints = new ArrayList<LatLng>();
        waypoints.add(new LatLng(35.661905, 139.709577));
        waypoints.add(new LatLng(35.6591188,139.7037352));
        LatLng goal = new LatLng(35.66147,139.709464);
        manager.routingPlaces(start, goal, waypoints, new OnEndDirectionsRequestListener() {
            @Override
            public void onEndDirectionListener(ArrayList<LatLng> latLngs) {

            }
        });



        CoursePagerAdapter adapter = new CoursePagerAdapter(this);

		LinearLayout mapView = (LinearLayout)layoutFactory.inflate(R.layout.activity_cource, null);
		adapter.add(new CourseGuideSpotView(this, this.course));
		adapter.add(mapView);


		// ViewPager を生成
		ViewPager viewPager = new ViewPager(this);
		viewPager.setAdapter(adapter);

		// レイアウトにセット
		setContentView(viewPager);


        //setContentView(R.layout.activity_cource);

		setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #map} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (this.map == null) {
            // Try to obtain the map from the SupportMapFragment.
            this.map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (this.map != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #map} is not null.
     */
    private void setUpMap() {
        //this.map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

		// 道の描画
		ArrayList<LatLng> points = this.course.getCoordinatesList();

		PolylineOptions lineOptions = new PolylineOptions();
		lineOptions.addAll(points);
		lineOptions.width(10);
		lineOptions.color(0x550000ff);
		this.map.addPolyline(lineOptions);


		// 中心位置を最初の場所としてカメラを移動
		LatLng center = points.get(0);

		CameraPosition.Builder builder = new CameraPosition.Builder()
				.bearing(0)
				.tilt(0)
				.zoom(16)
				.target(center);
		this.map.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

		this.map.setInfoWindowAdapter(new CustomInfoAdapter());

		// 主要な場所の案内リストの作成
		//LinearLayout mainList = (LinearLayout)findViewById(R.id.course_main_list);

		// 主要な場所の処理
		for (Spot spot : this.course.getSpotList()) {
			addMarker(spot);

			/*LinearLayout spotView = (LinearLayout)layoutFactory.inflate(R.layout.course_guide_spot, null);
			TextView text = (TextView)spotView.findViewById(R.id.course_guid_spot_name);
			text.setText(spot.getName());
			ImageView thumb = (ImageView)spotView.findViewById(R.id.course_guide_spot_thumb);
			thumb.setImageResource(R.drawable.thumbnail);
			mainList.addView(spotView);*/
		}
    }

	/**
	 * マーカーを追加します。
	 * @param spot
	 */
	private void addMarker(Spot spot) {
		BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
		MarkerOptions options = new MarkerOptions()
			.position(spot.getCoordinates())
			.title(spot.getName())
			.snippet(spot.getDescription())
			.icon(icon);
		this.markerList.add(this.map.addMarker(options));
	}

	/**
	 * マーカーをタップした際に表示するウィンドウの処理
	 */
	private class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter {

		private final View window;

		public CustomInfoAdapter() {
			this.window = getLayoutInflater().inflate(R.layout.course_custom_info_window, null);
		}

		@Override
		public View getInfoWindow(Marker marker) {
			render(marker, this.window);
			return this.window;
		}

		@Override
		public View getInfoContents(Marker marker) {
			return null;
		}

		/**
		 * InfoWindow を表示する
		 * @param marker {@link Marker}
		 * @param view {@link View}
		 */
		private void render(Marker marker, View view) {
			// ここでどの Marker がタップされたか判別する
			//if (marker.equals(marker)) {
			// for presentation
				int imgID = 0;
				int markerNo = markerList.indexOf(marker);
				if (markerNo == 0) {
					imgID = R.drawable.thumbnail;
				} else if (markerNo == 1) {
					imgID = R.drawable.shibuyaeki;
				} else if (markerNo == 2) {
					imgID = R.drawable.shibuyahikarie;
				} else if (markerNo == 3) {
					imgID = R.drawable.aogaku;
				}
				// 画像
				ImageView icon = (ImageView) view.findViewById(R.id.icon);
				icon.setImageResource(imgID);
				icon.setMaxWidth(200);
				icon.setMaxHeight(200);
			//}
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView snippet = (TextView) view.findViewById(R.id.snippet);
			title.setText(marker.getTitle());
			snippet.setText(marker.getSnippet());
		}

	}
}
