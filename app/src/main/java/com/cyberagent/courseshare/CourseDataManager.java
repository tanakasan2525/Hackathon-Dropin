package com.cyberagent.courseshare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import net.arnx.jsonic.JSON;

import java.util.ArrayList;

/**
 * Created by shogo on 2014/09/11.
 */
public class CourseDataManager {
    private static String DATABASE_NAME = "course.db";
    private static int VERSION = 1;

    private static String TABLE_NAME = "course_table";
    private static String COURSE_COLUMN_NAME = "course_json";

    private static CourseDataManager singleton = null;

    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;

    /**
     * プライベートコンストラクタ
     * @param context
     */
    private CourseDataManager(Context context) {
        helper = new CourseSQLOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * コンテキストを受け取り自身のインスタンスを返す
     * @param context
     * @return
     */
    public static CourseDataManager createInstance(Context context) {
        if (singleton == null) {
            singleton = new CourseDataManager(context);
        }
        return singleton;
    }

    /**
     * 受け取ったコースを保存する
     * @param course
     */
    public void saveCourse(Course course) {
        this.saveToLocalConvertToJson(course);
    }

    /**
     * コースクラスをJSONに変換してローカルに保存するメソッド
     * @param course
     */
    private void saveToLocalConvertToJson(Course course) {

        // CourseクラスをJSONにして保存
        String courseJson = JSON.encode(course);

        ContentValues values = new ContentValues();
        values.put(COURSE_COLUMN_NAME, courseJson);

        db.insert(TABLE_NAME, null, values);

    }

    /**
     * 保存されているコースをリストにして返す
     * @return
     */
    public ArrayList<Course> getCourseList() {
        ArrayList<Course> courseList = this.getCourseListFromLocal();
        return courseList;
    }

    /**
     * ローカルに保存したデータからコースリストを生成して返すメソッド
     * @return
     */
    private ArrayList<Course> getCourseListFromLocal() {
        ArrayList<Course> courseList = new ArrayList<Course>();

        String selectSql = "SELECT * FROM "+TABLE_NAME+";";
        Cursor cursor = db.rawQuery(selectSql, new String[]{});

        if (cursor.getCount() <= 0) {
            return courseList;
        }

        boolean next = cursor.moveToFirst();
        while (next) {
            String jsonCourse = cursor.getString(1);
            Course course = JSON.decode(jsonCourse, Course.class);
            courseList.add(course);

            next = cursor.moveToNext();
        }

        return courseList;
    }

    /**
     * コースのデータをすべて削除
     */
    public void deleteLocalData() {
        db.delete(TABLE_NAME, null, null);
    }

    /**
     * Private class
     */
    private class CourseSQLOpenHelper extends SQLiteOpenHelper{

        private final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT, "+COURSE_COLUMN_NAME+" TEXT);";

        public CourseSQLOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
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
