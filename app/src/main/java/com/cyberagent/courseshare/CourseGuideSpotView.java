package com.cyberagent.courseshare;

import android.content.Context;
import android.support.v7.internal.widget.LinearLayoutICS;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by tanakasan on 9/12/2014.
 */
public class CourseGuideSpotView extends LinearLayout {

	public CourseGuideSpotView(Context context) {
		super(context);
		setOrientation(VERTICAL);

		LayoutInflater layoutFactory = LayoutInflater.from(context);

		TextView time = new TextView(context);
		time.setText("　所要時間：2時間半　 スポット数：4");
		addView(time);

		// ダミー
		Course course = new Course("ダミーコース");
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

		int no = 0;
		for (Spot spot : course.getSpotList()) {
			LinearLayout spotView = (LinearLayout) layoutFactory.inflate(R.layout.course_guide_spot, null);
			TextView name = (TextView)spotView.findViewById(R.id.course_guide_spot_name);
			name.setText(spot.getName());
			TextView desc = (TextView)spotView.findViewById(R.id.course_guide_spot_desc);
			desc.setText(spot.getDescription());
			ImageView thumb = (ImageView)spotView.findViewById(R.id.course_guide_spot_thumb);
			// for presentation
			int imgID = 0;
			if (no == 0) {
				imgID = R.drawable.thumbnail;
			} else if (no == 1) {
				imgID = R.drawable.shibuyaeki;
			} else if (no == 2) {
				imgID = R.drawable.shibuyahikarie;
			} else if (no == 3) {
				imgID = R.drawable.aogaku;
			}
			++no;
			thumb.setImageResource(imgID);
			addView(spotView);
		}
	}
}
