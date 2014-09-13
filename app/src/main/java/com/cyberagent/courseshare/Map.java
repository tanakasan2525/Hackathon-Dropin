package com.cyberagent.courseshare;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
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
 * Created by tatsuya tanaka on 9/13/2014.
 */
public class Map {
	private Activity owner;
	private SupportMapFragment mapFragment;
	private GoogleMap map;

	private ArrayList<Marker> pins;

	public Map(Activity owner, SupportMapFragment mapFragment) {
		this.owner = owner;
		this.mapFragment = mapFragment;
		this.pins = new ArrayList<Marker>();
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
	}

	/**
	 * ピンを追加します。
	 * @param spot 追加するスポット
	 */
	public void addPin(Spot spot) {
		//this.map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
		BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
		MarkerOptions options = new MarkerOptions()
				.position(spot.getCoordinates())
				.title(spot.getName())
				.snippet(spot.getDescription())
				.icon(icon);
		this.pins.add(this.map.addMarker(options));
	}

	/**
	 * 道を追加します。
	 * @param points 追加する道の座標リスト
	 */
	public void addRoute(ArrayList<LatLng> points) {
		PolylineOptions lineOptions = new PolylineOptions();
		lineOptions.addAll(points);
		lineOptions.width(10);
		lineOptions.color(0x550000ff);
		this.map.addPolyline(lineOptions);
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
		this.map.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
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
		private void render(Marker marker, View view) {
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
		}

	}
}
