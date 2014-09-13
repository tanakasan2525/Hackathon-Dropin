package com.cyberagent.courseshare;

import android.app.Activity;
import android.view.View;
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
	private SupportMapFragment mapFragment;
	private GoogleMap map;

	private HashMap<String, Pin> pins;
	private ArrayList<Pin> waypoints;	// 確定した寄り道場所

	public Map(Activity owner, SupportMapFragment mapFragment) {
		this.owner = owner;
		this.mapFragment = mapFragment;
		this.pins = new HashMap<String, Pin>();
		this.waypoints = new ArrayList<Pin>();
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
		
		this.map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				Pin pin = pins.get(marker.getId());

				return false;
			}
		});
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
		for (Entry<String, Pin> pin : this.pins.entrySet())
			pin.getValue().marker.remove();
		removeAllRoutes();
	}

	/**
	 * 道を設定します。
	 * @param pinID 関連付けるピンのID
	 * @param points 追加する道の座標リスト
	 */
	public void setRoute(String pinID, ArrayList<LatLng> points) {
		PolylineOptions lineOptions = new PolylineOptions();
		lineOptions.addAll(points);
		lineOptions.width(10);
		lineOptions.color(0x550000ff);
		this.pins.get(pinID).route = this.map.addPolyline(lineOptions);
	}

	/**
	 * すべての道を削除します。
	 */
	public void removeAllRoutes() {
		for (Entry<String, Pin> pin : this.pins.entrySet()) {
			pin.getValue().route.remove();
			pin.getValue().route = null;
		}
	}

	/**
	 * 経由地を追加します。
	 * @param pinID 追加するピンのID
	 */
	public void addWeyPoint(String pinID) {
		Pin pin = this.pins.get(pinID);
		if (pin.route != null) {
			pin.route.setColor(0x55ff0000);
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

		private final View window;

		public CustomInfoAdapter() {
			this.window = owner.getLayoutInflater().inflate(R.layout.course_custom_info_window, null);
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
		private void render(final Marker marker, View view) {
			// ここでどの Marker がタップされたか判別する
			//if (marker.equals(marker)) {
			// 画像
			ImageView icon = (ImageView) view.findViewById(R.id.icon);
			//icon.setImageResource(imgID);
			icon.setMaxWidth(200);
			icon.setMaxHeight(200);
			//}
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView snippet = (TextView) view.findViewById(R.id.snippet);
			title.setText(marker.getTitle());
			snippet.setText(marker.getSnippet());

			Button btnDropIn = (Button) view.findViewById(R.id.btn_drop_in);
			btnDropIn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					addWeyPoint(marker.getId());
					removeAllPins();
				}
			});
		}

	}
}
