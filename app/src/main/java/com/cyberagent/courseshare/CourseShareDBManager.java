package com.cyberagent.courseshare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import net.arnx.jsonic.JSON;

import java.util.ArrayList;

/**
 * Created by shogo on 2014/09/11.
 */
public class CourseShareDBManager {

    // singleton
    private static CourseShareDBManager courseShareDBManager = null;

    private CourseShareSQLHelper helper;
    private SQLiteDatabase db;

    public static CourseShareDBManager createInstance(Context context) {
        if (courseShareDBManager == null) {
            courseShareDBManager = new CourseShareDBManager(context);
            return courseShareDBManager;
        }
        return courseShareDBManager;
    }

    private CourseShareDBManager(Context context) {
        helper = CourseShareSQLHelper.createInstance(context);
        db = helper.getWritableDatabase();
    }

    public void saveCourse(Course course) {
        String courseJson = JSON.encode(course);
        Log.v("sql_test", courseJson);
        ContentValues values = new ContentValues();
        values.put(CourseShareSQLHelper.COLUMN_NAME, courseJson);
        try {
            db.insert(CourseShareSQLHelper.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Course> fetchCourseList() {
        ArrayList<Course> courseList;

        Cursor cursor = db.rawQuery("SELECT * FROM " + CourseShareSQLHelper.TABLE_NAME, new String[]{});
        boolean next = cursor.moveToFirst();
        Log.v("sql_test", String.valueOf(cursor.getCount()));
        return null;
    }

}
