package com.cyberagent.courseshare;

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

	private Course course;

	LayoutInflater layoutFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

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

		this.map = new Map(this, (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));

		this.map.setUpMapIfNeeded();

		// 道の描画
		ArrayList<LatLng> points = this.course.getCoordinatesList();
		this.map.addRoute(points);

		// 中心位置を最初の場所としてカメラを移動
		LatLng center = points.get(0);
		this.map.setCenter(center);

		// 主要な場所の処理
		for (Spot spot : this.course.getSpotList()) {
			this.map.addPin(spot);
		}
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
