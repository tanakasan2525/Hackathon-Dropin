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

    // シングルトンインスタンス
    public static CourseShareDBManager createInstance(Context context) {
        if (courseShareDBManager == null) {
            courseShareDBManager = new CourseShareDBManager(context);
            return courseShareDBManager;
        }
        return courseShareDBManager;
    }

    // プライベートインスタンス
    private CourseShareDBManager(Context context) {
        helper = CourseShareSQLHelper.createInstance(context);
    }

    // 渡されたコースをJSONにして保存
    public void saveCourse(Course course) {
        db = helper.getWritableDatabase();

        String courseJson = JSON.encode(course);
        Log.v("sql_test", courseJson);
        ContentValues values = new ContentValues();
        values.put(CourseShareSQLHelper.COLUMN_NAME, courseJson);
        try {
            db.insert(CourseShareSQLHelper.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
    }

    // 保存してあるコースのリストを取得
    public ArrayList<Course> fetchCourseList() {
        db = helper.getWritableDatabase();
        ArrayList<Course> courseList = new ArrayList<Course>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + CourseShareSQLHelper.TABLE_NAME, new String[]{});
        Log.v("sql_test", String.valueOf(cursor.getCount()));

        boolean next = cursor.moveToFirst();
        while (next) {
            String jsonCourse = cursor.getString(1);
            Course course = JSON.decode(jsonCourse, Course.class);
            courseList.add(course);
            Log.v("sql_test", jsonCourse);

            next = cursor.moveToNext();
        }

        db.close();

        return courseList;
    }

}
