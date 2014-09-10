package com.cyberagent.courseshare;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * コース画面のアクティビティ
 */
public class CourceActivity extends FragmentActivity {

    private GoogleMap map; // Might be null if Google Play services APK is not available.

	private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cource);

		// テスト用ダミーデータ
		course = new Course();
		course.addCoordinatesToList(35.65787,139.698066);  // 渋谷マークシティ
		course.addCoordinatesToList(35.66147, 139.709464); // 青山学院大学

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

		ArrayList<Coordinates> coords = this.course.getCoordinatesList();
		ArrayList<LatLng> points = new ArrayList<LatLng>();

		for (Coordinates coord : coords) {
			LatLng latlon = new LatLng(coord.getLatitude(), coord.getLongitude());
			points.add(latlon);
		}

		PolylineOptions lineOptions = new PolylineOptions();
		lineOptions.addAll(points);
		lineOptions.width(10);
		lineOptions.color(0x550000ff);
		this.map.addPolyline(lineOptions);

		Coordinates start = coords.get(0);
		LatLng center = new LatLng(start.getLatitude(), start.getLongitude());

		CameraPosition.Builder builder = new CameraPosition.Builder()
				.bearing(0)
				.tilt(0)
				.zoom(16)
				.target(center);
		this.map.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
    }
}
