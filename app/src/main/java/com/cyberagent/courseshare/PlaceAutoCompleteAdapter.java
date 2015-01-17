package com.cyberagent.courseshare;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.Filter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by tanakasan on 9/14/2014.
 */
public class PlaceAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	private final String GOOGLE_API_KEY;

	private ArrayList<String> resultList;

	public PlaceAutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		GOOGLE_API_KEY = context.getResources().getString(R.string.google_api_key);
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public String getItem(int index) {
		return resultList.get(index);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// サジェストの取得
					resultList = autocomplete(constraint.toString());

					// 結果を格納
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				}
				else {
					notifyDataSetInvalidated();
				}
			}};
		return filter;
	}

	private ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?sensor=false&key=" + GOOGLE_API_KEY);
			sb.append("&components=country:jp");
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// 読み込み
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}

		} catch (MalformedURLException e) {
			Log.e("TEST", "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Log.e("TEST", "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			// Json のパース
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			resultList = new ArrayList<String>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {
				//resultList.add(predsJsonArray.getJSONObject(i).getString("description")); // 詳細な住所
				resultList.add(predsJsonArray.getJSONObject(i).getJSONArray("terms").getJSONObject(0).getString("value"));
			}
		} catch (JSONException e) {
			Log.e("TEST", "Cannot process JSON results", e);
		}

		return resultList;
	}
}



