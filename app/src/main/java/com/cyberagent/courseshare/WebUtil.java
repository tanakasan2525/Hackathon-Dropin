package com.cyberagent.courseshare;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tanakasan on 9/15/2014.
 */
public class WebUtil {
	public interface JsonListener {
		public void callback(JSONObject json);
	}

	public static void getJson(String uri, final JsonListener listener) {
		(new AsyncTask<String, Integer, Integer>() {
			@Override
			protected Integer doInBackground(String... contents) {
				listener.callback(getJsonSync(contents[0]));
				return 0;
			}

		}).execute(uri);
	}

	public static JSONObject getJsonSync(String uri) {
		HttpURLConnection conn = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			URL url = new URL(uri);
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				stringBuilder.append(buff, 0, read);
			}

		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(stringBuilder.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}
}
