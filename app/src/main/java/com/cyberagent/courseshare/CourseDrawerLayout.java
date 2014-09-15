package com.cyberagent.courseshare;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by tanakasan on 9/16/2014.
 */
public class CourseDrawerLayout extends DrawerLayout {

	public CourseDrawerLayout(Context context) {
		super(context);
	}

	public CourseDrawerLayout(Context context, AttributeSet as) {
		super(context, as);
	}

	public CourseDrawerLayout(Context context, AttributeSet as, int defStyle) {
		super(context, as, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		super.onTouchEvent(arg0);
		return false;// これを入れないとこのコンポーネントにタッチイベントが届かない。。。
	}

}
