package com.cyberagent.courseshare;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import net.arnx.jsonic.JSON;


public class CourseListViewActivity extends Activity {

    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list_view);

        findViewById(R.id.create_json).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course course = new Course();
                course.setName("hogehoge");
                json = JSON.encode(course, true);
                Log.v("JSON", json);
            }
        });

        findViewById(R.id.decode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course course = JSON.decode(json, Course.class);
                String name = course.getName();
                Log.v("JSON", name);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
