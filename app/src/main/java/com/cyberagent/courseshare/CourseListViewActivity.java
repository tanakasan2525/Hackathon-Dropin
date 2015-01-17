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

import java.util.ArrayList;
import java.util.Random;


public class CourseListViewActivity extends Activity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list_view);

        linearLayout = (LinearLayout) findViewById(R.id.course_list);

        final CourseDataManager dataManager = CourseDataManager.createInstance(getApplicationContext());
        ArrayList<Course> courseList = dataManager.getCourseList();


        for (int i = 0; i < courseList.size(); i++) {
            CourseView courseView = new CourseView(this, courseList.get(i));
            linearLayout.addView(courseView);
        }


        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				Random rand = new Random();
                Course course = new Course("Course " + rand.nextInt(100));
                dataManager.saveCourse(course);

                CourseView courseView = new CourseView(getBaseContext(), course);
                linearLayout.addView(courseView);
            }
        });

        // テーブルのデータをすべて削除
        findViewById(R.id.delete_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataManager.deleteLocalData();
                linearLayout.removeAllViews();
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
