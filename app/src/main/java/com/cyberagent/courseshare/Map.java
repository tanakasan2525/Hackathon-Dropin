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

	private Pin startPin;
	private Pin goalPin;
	private ArrayList<Pin> pins;	// 寄り道候補のピン
	private ArrayList<Pin> waypoints;	// 確定した寄り道場所

	private Route dropInRoute;

	public Map(Activity owner, MapWrapperLayout mapView, SupportMapFragment mapFragment) {
		this.owner = owner;
		this.mapView = mapView;
		this.mapFragment = mapFragment;
		this.pins = new ArrayList<Pin>();
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
	}

	public void setStartAndGoal(Spot start, Spot goal) {
		this.startPin = addPin(start);
		this.goalPin = addPin(goal);
		Route route = new Route(this.startPin, this.goalPin);
		route.line = addLines(getRoutes(this.startPin, this.goalPin), 0x5500ff00);
		this.goalPin.prevRoute = route;
		this.startPin.nextRoute = route;
	}

	/**
	 * ピンを追加します。
	 * @param spot 追加するスポット
	 * @return 追加したピン
	 */
	public Pin addPin(Spot spot) {
		//this.map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
		BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
		MarkerOptions options = new MarkerOptions()
				.position(spot.getCoordinates())
				.title(spot.getName())
				.snippet(spot.getDescription())
				.icon(icon);
		Pin pin = new Pin(this.map.addMarker(options));
		this.pins.add(pin);
		return pin;
	}

	/**
	 * すべてのピンと道を削除します。
	 */
	public void removeAllPins() {
		removeRoute();
		for (Pin pin : this.pins) {
			if (!this.waypoints.contains(pin) && !pin.equals(this.startPin) && !pin.equals(this.goalPin))
				pin.marker.remove();
		}
		pins.clear();
	}

	/**
	 * 道を設定します。
	 * @param startPin 始点
	 * @param goalPin 終点
	 */
	public Route setRoute(Pin startPin, Pin goalPin) {

		removeRoute();

		Route route = new Route(startPin, goalPin);
		route.line = addLines(getRoutes(startPin, goalPin), 0x550000ff);
		this.dropInRoute = route;
		return route;
	}

	/**
	 * 道を削除します。
	 */
	public void removeRoute() {
		if (this.dropInRoute != null) {
			this.dropInRoute.line.remove();
			this.dropInRoute = null;
		}
	}

	/**
	 * 経由地を追加します。
	 * @param pin 追加するピン
	 */
	public void addWeyPoint(Pin pin) {
		BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
		pin.marker.setIcon(icon);
		if (this.dropInRoute != null) {
			this.dropInRoute.line.setColor(0x5500ff00);

			Pin prevPin = this.dropInRoute.prev;
			Pin nextPin = prevPin.nextRoute.next;

			nextPin.prevRoute.line.remove();

			Route nextRoute = new Route(pin, nextPin);
			nextRoute.line = addLines(getRoutes(pin, nextPin), 0x5500ff00);
			nextPin.prevRoute = nextRoute;
			pin.nextRoute = nextRoute;
			pin.prevRoute = this.dropInRoute;
			prevPin.nextRoute = this.dropInRoute;
			this.dropInRoute = null;
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

	public Pin getStartPin() {
		return this.startPin;
	}

	public Pin getGoalPin() {
		return this.goalPin;
	}

	public Pin getPrevPin() {
		if (this.waypoints.isEmpty())
			return this.startPin;
		else
			return waypoints.get(waypoints.size() - 1);
	}

	private Pin getPinFromMarker(Marker marker) {
		for (Pin p : pins)
			if (marker.equals(p.marker)) {
				return p;
			}
		return null;
	}

	private ArrayList<LatLng> getRoutes(Pin start, Pin goal) {
		ArrayList<LatLng> points = new ArrayList<LatLng>(); // APIにする
		points.add(start.marker.getPosition());
		points.add(goal.marker.getPosition());
		return points;
	}

	private Polyline addLines(ArrayList<LatLng> points, int color) {
		PolylineOptions lineOptions = new PolylineOptions();
		lineOptions.addAll(points);
		lineOptions.width(11);
		lineOptions.color(color);
		return this.map.addPolyline(lineOptions);
	}

	private static int getPixelsFromDp(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dp * scale + 0.5f);
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

		int debugFlag = 0;

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
					addWeyPoint(getPinFromMarker(marker));
					removeAllPins();
					if (debugFlag == 0) {
						Spot spot = new Spot("ラフォーレ原宿", new LatLng(35.6689883,139.7056496),
								"ラフォーレ原宿です。");
						addPin(spot);
						debugFlag++;
					}
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

			Pin prevPin = getPrevPin();
			Pin pin = getPinFromMarker(marker);
			setRoute(prevPin, pin);

			return this.window;
		}

		@Override
		public View getInfoContents(Marker marker) {
			// getInfoWindow()の戻り値がnullの時だけ呼ばれるっぽい
			return null;
		}

	}

	class Pin {
		public Marker marker;
		public Route prevRoute; // このピンに着く前の道
		public Route nextRoute; // このピンから出る次の道

		Pin(Marker marker) {
			this.marker = marker;
		}
	}

	class Route {
		public Polyline line;
		public Pin prev;
		public Pin next;

		Route(Pin prev, Pin next) {
			this.prev = prev;
			this.next = next;
		}
	}
}
