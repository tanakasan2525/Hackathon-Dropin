package com.cyberagent.courseshare;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * コース画面のアクティビティ
 */
public class CourceActivity extends FragmentActivity {

	private Map map;

	LayoutInflater layoutFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		layoutFactory = LayoutInflater.from(this);

		CoursePagerAdapter adapter = new CoursePagerAdapter(this);

		MapWrapperLayout mapView = (MapWrapperLayout)layoutFactory.inflate(R.layout.activity_cource, null);
		adapter.add(new CourseGuideSpotView(this));
		adapter.add(mapView);

		// ViewPager を生成
		ViewPager viewPager = new ViewPager(this);
		viewPager.setAdapter(adapter);

		// レイアウトにセット
		setContentView(viewPager);

		this.map = new Map(this, mapView, (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));


		ArrayList<LatLng> points = new ArrayList<LatLng>();

		Spot spot = new Spot("渋谷マークシティ", new LatLng(35.65787,139.698066),
				"サイバーエージェントのオフィスがあります。");
		String pinID = this.map.addPin(spot);

		spot = new Spot("渋谷駅", new LatLng(35.658517,139.701334),
				"JR東日本、東急、東京メトロの駅です。");
		pinID = this.map.addPin(spot);

		points.add(new LatLng(35.65787,139.698066));  // 渋谷マークシティ
		points.add(new LatLng(35.6587292,139.7000174));
		points.add(new LatLng(35.6588081,139.7005457));
		points.add(new LatLng(35.6585071,139.7010799));
		points.add(new LatLng(35.6585152,139.7013252));
		points.add(new LatLng(35.658517, 139.701334));   // 渋谷駅
		this.map.setRoute(pinID, points);

		spot = new Spot("代々木公園", new LatLng(35.6722044,139.6962304),
				"デング熱で今ヤバイです。");
		pinID = this.map.addPin(spot);

		points.clear();
		points.add(new LatLng(35.65787,139.698066));  // 渋谷マークシティ
		points.add(new LatLng(35.6722044,139.6962304)); // 代々木公園
		/*points.add(new LatLng(35.6587292,139.7000174));
		points.add(new LatLng(35.6588751,139.7005146));
		points.add(new LatLng(35.65898870000001,139.7010571));
		points.add(new LatLng(35.6587918,139.7020231));
		points.add(new LatLng(35.6589562,139.7025314));
		points.add(new LatLng(35.6588729,139.7030199));
		points.add(new LatLng(35.6591188,139.7037352));*/ // 渋谷ヒカリエ
		this.map.setRoute(pinID, points);

		// 中心位置を最初の場所としてカメラを移動
		LatLng center = points.get(0);
		this.map.setCenter(center);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.map.setUpMapIfNeeded();
	}

	public Map getMap() {
		return this.map;
	}

}
