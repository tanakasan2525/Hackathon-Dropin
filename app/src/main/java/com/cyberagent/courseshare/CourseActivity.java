package com.cyberagent.courseshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.HashMap;

/**
 * コース画面のアクティビティ
 */
public class CourseActivity extends FragmentActivity {

	private Map map;

	LayoutInflater layoutFactory;

	private ProgressDialog waitDialog;

	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout drawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		//layoutFactory = LayoutInflater.from(this);

		//CoursePagerAdapter adapter = new CoursePagerAdapter(this);

		//MapWrapperLayout mapView = (MapWrapperLayout)layoutFactory.inflate(R.layout.activity_cource, null);
		//adapter.add(new CourseGuideSpotView(this));
		//adapter.add(mapView);

		// ViewPager を生成
		//ViewPager viewPager = new ViewPager(this);
		//viewPager.setAdapter(adapter);

		// レイアウトにセット
		//setContentView(viewPager);
		setContentView(R.layout.activity_cource);

		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerToggle = new ActionBarDrawerToggle(this, drawer,
				R.drawable.ic_launcher, R.string.hello_world, // open
				R.string.hello_world) { // close
			@Override
			public void onDrawerClosed(View drawerView) {

			}

			@Override
			public void onDrawerOpened(View drawerView) {

			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset); // アイコンのアニメーション

				// MapFragmentとの相性が悪く、メニューの上にMapが表示されてしまうバグの回避
				// drawerViewを前面へ
				drawer.bringChildToFront(drawerView);
				drawer.requestLayout();

				// drawerViewの余白部分の背景を透明にする
				drawer.setScrimColor(Color.TRANSPARENT);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				// newState
				// 表示済み、閉じ済みの状態：0
				// ドラッグ中状態:1
				// ドラッグを放した後のアニメーション中：2

			}
		};

		drawer.setDrawerListener(drawerToggle);

		// UpNavigationアイコン(アイコン横の<の部分)を有効に
		// NavigationDrawerではR.drawable.drawerで上書き
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		// UpNavigationを有効に
		//getActionBar().setHomeButtonEnabled(true);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		// 要素の追加（1）
		adapter.add("メニュー１");
		adapter.add("メニュー２");
		adapter.add("メニュー３");

		SortableListView listView = (SortableListView)findViewById(R.id.waypoint_list);
		listView.setAdapter(adapter);


		MapWrapperLayout mapView = (MapWrapperLayout)findViewById(R.id.activity_course_layout);

		TextView btnSearch = (TextView)mapView.findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
				startActivity(intent);
			}
		});

		this.map = new Map(this, mapView, (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));

		Bundle b = getIntent().getExtras();
		if (b != null) {
			String keyword = b.getString("keyword");
			if (keyword != null) {
				btnSearch.setText(keyword);
				search(keyword);
			}
		}


		ArrayList<LatLng> points = new ArrayList<LatLng>();

		Spot start = new Spot("渋谷駅", new LatLng(35.658517,139.701334), "JR東日本、東急、東京メトロの駅です。");
		Spot goal = new Spot("原宿駅", new LatLng(35.6701676,139.7026946), "原宿駅です");
		this.map.setStartAndGoal(start, goal);

		Spot spot = new Spot("青山学院大学", new LatLng(35.66147,139.709464),
				"青山学院大学の文系が集まるキャンパスです。");
		this.map.addPin(spot);

        //******************************
        // MapAPIManager テスト
        //******************************
        ///*
        MapAPIManager manager = new MapAPIManager(this);
        LatLng latLng = start.getCoordinates();
        ArrayList<String> keywords = new ArrayList<String>();
        keywords.add("food");
        manager.searchPlaces(latLng.latitude,latLng.longitude, keywords, 2000, new OnEndPlaceRequestListener() {
            @Override
            public void onEndRequestListener(ArrayList<Spot> spots) {
                for (Spot s : spots) {
                    //Log.v(MapAPIManager.TAG, s.toString());
                }

            }
        });
        ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(spot.getCoordinates());
        manager.routingPlaces(start.getCoordinates(), goal.getCoordinates(),latLngs, new OnEndDirectionsRequestListener() {
            @Override
            public void onEndDirectionListener(ArrayList<LatLng> latLngs, HashMap<String, Object> data) {
                Log.v(MapAPIManager.TAG, data.toString());
            }
        });
        //*/

		// 中心位置を最初の場所としてカメラを移動
		LatLng center = new LatLng(35.658517, 139.701334);
		this.map.setCenter(center);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.map.setUpMapIfNeeded();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// ActionBarDrawerToggleにandroid.id.home(up ナビゲーション)を渡す。
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void search(String keyword) {
		this.waitDialog = new ProgressDialog(this);
		this.waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.waitDialog.setMessage("検索中");
		this.waitDialog.show();
		// call WebAPI

		this.waitDialog.dismiss();
	}


	public Map getMap() {
		return this.map;
	}

}
