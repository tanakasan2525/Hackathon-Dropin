package com.cyberagent.courseshare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
        helper = new CourseShareSQLHelper(context);
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

    public void deleteTable() {
        db = helper.getWritableDatabase();
        String sql = "drop table " + CourseShareSQLHelper.TABLE_NAME;
        db.execSQL(sql);
        db.close();
    }



    // private Helper
    private class CourseShareSQLHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "sqlite3.db";
        public static final int VERSION = 1;

        public static final String COLUMN_NAME = "course_json";
        public static final String TABLE_NAME = "course_table";

        // SQL
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_NAME+" TEXT)";

        public CourseShareSQLHelper(Context context) {
            super(context, CourseShareSQLHelper.DATABASE_NAME, null, CourseShareSQLHelper.VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
