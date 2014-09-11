package com.cyberagent.courseshare;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import net.arnx.jsonic.JSON;

import java.util.ArrayList;


public class CourseListViewActivity extends Activity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list_view);

        linearLayout = (LinearLayout) findViewById(R.id.course_list);
        CourseShareDBManager dbManager = CourseShareDBManager.createInstance(getApplicationContext());
        ArrayList<Course> courseList = dbManager.fetchCourseList();


        for (int i = 0; i < courseList.size(); i++) {
            CourseView courseView = new CourseView(this, courseList.get(i));
            linearLayout.addView(courseView);
        }
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
