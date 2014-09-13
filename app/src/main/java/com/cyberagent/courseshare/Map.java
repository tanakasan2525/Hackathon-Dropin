package com.cyberagent.courseshare;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.internal.d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TooManyListenersException;

/**
 * Created by tatsuya tanaka on 9/13/2014.
 */
public class Map {
	private Activity owner;
	MapWrapperLayout mapView;
	private SupportMapFragment mapFragment;
	private GoogleMap map;

	private HashMap<String, Pin> pins;
	private ArrayList<Pin> waypoints;	// 確定した寄り道場所

	public Map(Activity owner, MapWrapperLayout mapView, SupportMapFragment mapFragment) {
		this.owner = owner;
		this.mapView = mapView;
		this.mapFragment = mapFragment;
		this.pins = new HashMap<String, Pin>();
		this.waypoints = new ArrayList<Pin>();

		setUpMapIfNeeded();

		// MapWrapperLayout initialization
		// 39 - default marker height
		// 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
		mapView.init(map, getPixelsFromDp(owner, 39 + 20));
	}

	public void setUpMapIfNeeded() {
		if (this.map == null) {
			this.map = this.mapFragment.getMap();
			if (this.map != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		this.map.setInfoWindowAdapter(new CustomInfoAdapter());

		/*this.map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				Pin pin = pins.get(marker.getId());

				return false;
			}
		});*/

	}

	/**
	 * ピンを追加します。
	 * @param spot 追加するスポット
	 * @return 追加したピンのID
	 */
	public String addPin(Spot spot) {
		//this.map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
		BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
		MarkerOptions options = new MarkerOptions()
				.position(spot.getCoordinates())
				.title(spot.getName())
				.snippet(spot.getDescription())
				.icon(icon);
		Marker marker = this.map.addMarker(options);
		this.pins.put(marker.getId(), new Pin(marker));
		return marker.getId();
	}

	/**
	 * すべてのピンと道を削除します。
	 */
	public void removeAllPins() {
		removeAllRoutes();
		for (Entry<String, Pin> pinKey : this.pins.entrySet()) {
			Pin pin = pinKey.getValue();
			if (!this.waypoints.contains(pin))
				pin.marker.remove();
		}
		pins.clear();
	}

	/**
	 * 道を設定します。
	 * @param pinID 関連付けるピンのID
	 * @param points 追加する道の座標リスト
	 */
	public void setRoute(String pinID, ArrayList<LatLng> points) {
		PolylineOptions lineOptions = new PolylineOptions();
		lineOptions.addAll(points);
		lineOptions.width(11);
		lineOptions.color(0x550000ff);
		this.pins.get(pinID).route = this.map.addPolyline(lineOptions);
	}

	/**
	 * すべての道を削除します。
	 */
	public void removeAllRoutes() {
		for (Entry<String, Pin> pinKey : this.pins.entrySet()) {
			Pin pin = pinKey.getValue();
			if (!this.waypoints.contains(pin) && pin.route != null) {
				pin.route.remove();
				pin.route = null;
			}
		}
	}

	/**
	 * 経由地を追加します。
	 * @param pinID 追加するピンのID
	 */
	public void addWeyPoint(String pinID) {
		Pin pin = this.pins.get(pinID);
		BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
		pin.marker.setIcon(icon);
		if (pin.route != null) {
			pin.route.setColor(0x5500ff00);
		}
		this.waypoints.add(pin);
	}

	/**
	 * カメラの中心位置を設定します。
	 * @param center 中心位置の緯度経度
	 */
	public void setCenter(LatLng center) {
		CameraPosition.Builder builder = new CameraPosition.Builder()
				.bearing(0)			// カメラの向き
				.tilt(0)			// カメラの傾き
				.zoom(16)
				.target(center);
		//this.map.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
		this.map.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
	}

	private static int getPixelsFromDp(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dp * scale + 0.5f);
	}

	private class Pin {
		public Marker marker;
		public Polyline route;

		Pin(Marker marker) {
			this.marker = marker;
		}

	}

	/**
	 * マーカーをタップした際に表示するウィンドウの処理
	 */
	private class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter {

		private ViewGroup window;
		private TextView title;
		private TextView snippet;
		private ImageView icon;
		private Button btnDropIn;

		private OnInfoWindowElemTouchListener infoButtonListener;

		public CustomInfoAdapter() {
			this.window = (ViewGroup)owner.getLayoutInflater().inflate(R.layout.course_custom_info_window, null);
			this.title = (TextView) window.findViewById(R.id.title);
			this.snippet = (TextView) window.findViewById(R.id.snippet);
			this.icon = (ImageView) window.findViewById(R.id.icon);
			this.btnDropIn = (Button) window.findViewById(R.id.btn_drop_in);
			icon.setMaxWidth(200);
			icon.setMaxHeight(200);

			this.infoButtonListener = new OnInfoWindowElemTouchListener(this.btnDropIn,
					owner.getResources().getDrawable(R.drawable.btn_ok),
					owner.getResources().getDrawable(R.drawable.btn_ok))
			{
				@Override
				protected void onClickConfirmed(View v, Marker marker) {
					addWeyPoint(marker.getId());
					removeAllPins();
				}
			};
			this.btnDropIn.setOnTouchListener(infoButtonListener);
		}

		@Override
		public View getInfoWindow(Marker marker) {
			// Setting up the infoWindow with current's marker info
			this.title.setText(marker.getTitle());
			this.snippet.setText(marker.getSnippet());
			this.infoButtonListener.setMarker(marker);
			//icon.setImageResource(imgID);
			// We must call this to set the current marker and infoWindow references
			// to the MapWrapperLayout
			mapView.setMarkerWithInfoWindow(marker, this.window);
			return this.window;
		}

		@Override
		public View getInfoContents(Marker marker) {
			// getInfoWindow()の戻り値がnullの時だけ呼ばれるっぽい
			return null;
		}

	}
}
