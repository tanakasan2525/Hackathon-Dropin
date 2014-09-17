package com.cyberagent.courseshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * コース画面のアクティビティ
 */
public class CourseActivity extends FragmentActivity {

	private Map map;

	private MapAPIManager apiManager;

	private PersonTracker personTracker;

	private ArrayList<MapTask> mapTasks;

	LayoutInflater layoutFactory;

	private ProgressDialog waitDialog;

	private ActionBarDrawerToggle drawerToggle;
	private DrawerLayout drawer;
	private RatingBar ratingBar;
	private TextView timeLeftView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.apiManager = new MapAPIManager(this);
		this.personTracker = new PersonTracker(this);
		this.mapTasks = new ArrayList<MapTask>();

		layoutFactory = LayoutInflater.from(this);

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

		MapWrapperLayout mapView = (MapWrapperLayout)findViewById(R.id.activity_course_layout);

		TextView btnSearch = (TextView)mapView.findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(CourseActivity.this, SearchActivity.class);
			    startActivityForResult(intent, 123);
			}
		});

		this.ratingBar = (RatingBar)findViewById(R.id.ratingbar);
		ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				// レートが変更された際の処理
				map.hidePinByRating(rating);
			}
		});
		//map.hidePinByRating(rating);

		this.timeLeftView = (TextView)findViewById(R.id.time_left_view);

		this.map = new Map(this, mapView, (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map), this.apiManager);

		Intent i = getIntent();
		String start = i.getStringExtra("start");
		String goal = i.getStringExtra("goal");
		String waypoint = i.getStringExtra("transitPoint");
		int timeLeft = i.getIntExtra("timeLeft", 0);

		this.mapTasks.add(new MapTask(TaskType.START, "dummy")); // doNextTaskをうまく動かすためのダミー

		//if (!"現在地".equals(start)) {
		this.mapTasks.add(new MapTask(TaskType.START, start));
		//}
		this.mapTasks.add(new MapTask(TaskType.GOAL, goal));
		if (waypoint != null)
			this.mapTasks.add(new MapTask(TaskType.WAYPOINT, waypoint));

		resetSpotList();

		// カメラの移動
		LatLng now = new LatLng(this.personTracker.getLatitude(), this.personTracker.getLongitude());
		this.map.setCenter(now);

		/*if ("現在地".equals(start)) {
			this.map.confirmPin(this.map.addPin(new Spot(start, now)));
			this.map.confirmPin(this.map.addPin(new Spot(start, now)));
		} else*/
			doNextTask();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.map.setUpMapIfNeeded();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data == null) return; // 戻るボタンをおした時

		Bundle bundle = data.getExtras();

		switch (requestCode) {
			case 123:
				if (resultCode == RESULT_OK) {
					TextView btnSearch = (TextView)findViewById(R.id.btn_search);
					String keyword = bundle.getString("keyword");
					btnSearch.setText(keyword);
					search(keyword);
				} else if (resultCode == RESULT_CANCELED) {
				}
				break;

			default:
				break;
		}
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

		return false;//super.onOptionsItemSelected(item);
	}

	public void setLeftTime(int second) {
		this.timeLeftView.setText((second / 60) + "分" + (second % 60) + "秒");
	}

	public void search(String keyword) {
		if ("現在地".equals(keyword)) {
			LatLng now = new LatLng(this.personTracker.getLatitude(), this.personTracker.getLongitude());
			this.map.confirmPin(this.map.addPin(new Spot(keyword, now)));
			return;
		}

		this.waitDialog = new ProgressDialog(this);
		this.waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.waitDialog.setMessage("検索中");
		this.waitDialog.show();

		// 前回の検索結果を削除
		this.map.removeAllPins();

		ArrayList<String> keywords = new ArrayList<String>();
		keywords.add(keyword);

		LatLng prevPoint;

		if (this.map.getPrevPin() == null) {
			prevPoint = new LatLng(35.658517, 139.701334); // GPSで現在地を取得
		} else {
			prevPoint = this.map.getPrevPin().spot.getCoordinates();
		}

		// call WebAPI
		this.apiManager.searchPlaces(prevPoint.latitude, prevPoint.longitude, keywords, 2000, new OnEndPlaceRequestListener() {
			@Override
			public void onEndRequestListener(String status, ArrayList<Spot> spots) {

				//if (!spots.isEmpty())
				//	map.setCenter(spots.get(0).getCoordinates());

				if (getTaskType() == TaskType.START)
					if (!spots.isEmpty())
						map.setCenter(spots.get(0).getCoordinates());

				if (waitDialog != null)
					waitDialog.dismiss();
				waitDialog = null;

				if (spots.size() == 1) {
					// 候補のピンがひとつしかない場合は確定
					Map.Pin pin = map.addPin(spots.get(0));
					map.hidePinByRating(ratingBar.getRating());
					map.confirmPin(pin);
				} else {
					for (Spot spot : spots)
						map.addPin(spot);
					map.hidePinByRating(ratingBar.getRating());
				}
			}
		});
	}

	public void resetSpotList() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		Map.Pin start = this.map.getStartPin();
		Map.Pin goal = this.map.getGoalPin();

		LinearLayout listView = (LinearLayout)findViewById(R.id.waypoint_list);
		listView.removeAllViews();

		if (start != null) {
			LinearLayout item = (LinearLayout)layoutFactory.inflate(R.layout.waypoint_list_item, null);
			((TextView)item.findViewById(R.id.textView)).setText(start.spot.getName());
			listView.addView(item);
			//adapter.add(start.spot.getName());
		}

		for (Map.Pin pin : this.map.getWaypoints()) {
			LinearLayout item = (LinearLayout)layoutFactory.inflate(R.layout.waypoint_list_item, null);
			((TextView)item.findViewById(R.id.textView)).setText(pin.spot.getName());
			listView.addView(item);
			//adapter.add(pin.spot.getName());
		}

		if (goal != null) {
			LinearLayout item = (LinearLayout)layoutFactory.inflate(R.layout.waypoint_list_item_side, null);
			((TextView)item.findViewById(R.id.textView)).setText(goal.spot.getName());
			listView.addView(item);
			// adapter.add(goal.spot.getName());
		}

		//SortableListView listView = (SortableListView)findViewById(R.id.waypoint_list);
		//listView.setAdapter(adapter);



		Map.Pin newPin = map.getNewPin();
	}

	public void doNextTask() {
		this.mapTasks.remove(0);
		if (!this.mapTasks.isEmpty()) {
			MapTask mt = this.mapTasks.get(0);
			switch (mt.type) {
				case START:
					Toast.makeText(this, "現在地を選択してください", Toast.LENGTH_LONG).show();
					break;
				case GOAL:
					Toast.makeText(this, "目的地を選択してください", Toast.LENGTH_LONG).show();
					break;
				case WAYPOINT:
					Toast.makeText(this, "寄り道する場所を選択してください", Toast.LENGTH_LONG).show();
					break;
			}
			search(mt.keyword);
		}
	}


	public Map getMap() {
		return this.map;
	}

	public TaskType getTaskType() {
		if (this.mapTasks.isEmpty())
			return TaskType.WAYPOINT;
		else
			return this.mapTasks.get(0).type;
	}

	enum TaskType { START, GOAL, WAYPOINT }
	class MapTask {
		public TaskType type;
		public String keyword;

		MapTask(TaskType type, String keyword) {
			this.type = type;
			this.keyword = keyword;
		}
	}

}

