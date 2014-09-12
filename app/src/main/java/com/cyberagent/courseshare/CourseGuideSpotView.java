package com.cyberagent.courseshare;

import android.content.Context;
import android.support.v7.internal.widget.LinearLayoutICS;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by tanakasan on 9/12/2014.
 */
public class CourseGuideSpotView extends LinearLayout {

	public CourseGuideSpotView(Context context, Course course) {
		super(context);
		setOrientation(VERTICAL);

		LayoutInflater layoutFactory = LayoutInflater.from(context);

		TextView time = new TextView(context);
		time.setText("　所要時間：2時間半　 スポット数：4");
		addView(time);

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
