package com.example.googledirectionslib.async;

import android.os.AsyncTask;
import android.util.Log;
import com.example.googledirectionslib.json.Parser;
import com.example.googledirectionslib.listeners.BaseListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 12.11.13
 * Time: 17:44
 * Class to download JSON with route data
 */
public class DownloadTask extends AsyncTask<String, Void, String> {

    Parser parser;
    BaseListener listener;

    public DownloadTask(Parser parser, BaseListener listener) {
        this.parser = parser;
        this.listener = listener;
    }

    // Downloading data in non-ui thread
    @Override
    protected String doInBackground(String... url) {

        // For storing data from web service

        String data = "";

        try {
            // Fetching the data from web service
            data = downloadUrl(url[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserTask parserTask = new ParserTask(listener, parser);
        // Invokes the thread for parsing the JSON data
        parserTask.execute(result);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("User-agent", "Mozilla/4.0");

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
