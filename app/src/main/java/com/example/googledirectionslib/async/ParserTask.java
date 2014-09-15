package com.example.googledirectionslib.async;

import android.os.AsyncTask;
import com.example.googledirectionslib.json.Parser;
import com.example.googledirectionslib.listeners.BaseListener;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 12.11.13
 * Time: 17:55
 * Class to parse server request asynchroniously.
 */
public class ParserTask extends AsyncTask<String, Integer, Object> {
    BaseListener listener;
    Parser parser;

    public ParserTask(BaseListener listener, Parser parser) {
        this.listener = listener;
        this.parser = parser;
    }

    // Parsing the data in non-ui thread
    @Override
    protected Object doInBackground(String... jsonData) {

        JSONObject jObject;
        Object result = null;

        try {
            jObject = new JSONObject(jsonData[0]);

            // Starts parsing data
            result = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(Object result) {
        listener.onWorkDone(result);
    }
}
