package com.cyberagent.courseshare;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by tanakasan on 9/14/2014.
 */
public class MapWrapperLayout extends RelativeLayout {

	private GoogleMap map;

	private int bottomOffsetPixels;

	/**
	 * 現在選択しているマーカー
	 */
	private Marker marker;

	private View infoWindow;

	public MapWrapperLayout(Context context) {
		super(context);
	}

	public MapWrapperLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MapWrapperLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * タッチイベントが起こる前に一度呼び出すメソッド
	 */
	public void init(GoogleMap map, int bottomOffsetPixels) {
		this.map = map;
		this.bottomOffsetPixels = bottomOffsetPixels;
	}

	/**
	 * InfoWindowAdapter.getInfoContents または InfoWindowAdapter.getInfoWindowで呼び出すメソッド
	 */
	public void setMarkerWithInfoWindow(Marker marker, View infoWindow) {
		this.marker = marker;
		this.infoWindow = infoWindow;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean ret = false;

		if (marker != null && marker.isInfoWindowShown() && map != null && infoWindow != null) {

			Point point = map.getProjection().toScreenLocation(marker.getPosition());

			// InfoWindowのクライアント領域に変換
			MotionEvent copyEv = MotionEvent.obtain(ev);
			copyEv.offsetLocation(
					-point.x + (infoWindow.getWidth() / 2),
					-point.y + infoWindow.getHeight() + bottomOffsetPixels);

			// 調節したイベントを呼び出し
			ret = infoWindow.dispatchTouchEvent(copyEv);
		}

		return ret || super.dispatchTouchEvent(ev);
	}
}