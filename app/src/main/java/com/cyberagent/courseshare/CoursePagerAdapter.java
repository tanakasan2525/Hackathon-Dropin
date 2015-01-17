package com.cyberagent.courseshare;


import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by tatsuya tanaka on 9/11/2014.
 */
public class CoursePagerAdapter extends PagerAdapter {

	private Context context;

	private ArrayList<View> list;

	public CoursePagerAdapter(Context context) {
		this.context = context;
		this.list = new ArrayList<View>();
	}

	public void add(View view) {
		this.list.add(view);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		// リストから取得
		View view = this.list.get(position);

		// コンテナに追加
		container.addView(view);

		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// コンテナから View を削除
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		// リストのアイテム数を返す
		return this.list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// Object 内に View が存在するか判定する
		return view == (View) object;
	}

}
