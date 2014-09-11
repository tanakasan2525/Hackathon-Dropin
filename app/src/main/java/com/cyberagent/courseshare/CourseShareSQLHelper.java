package com.cyberagent.courseshare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shogo on 2014/09/11.
 */
public class CourseShareSQLHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "sqlite3.db";
    public static final int VERSION = 1;

    public static final String COLUMN_NAME = "course_json";
    public static final String TABLE_NAME = "course_table";

    // SQL
    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_NAME+" TEXT)";

    // singleton
    private static CourseShareSQLHelper courseShareSQLHelper = null;

    public static CourseShareSQLHelper createInstance(Context context) {
        if (courseShareSQLHelper == null) {
            courseShareSQLHelper = new CourseShareSQLHelper(context);
        }
        return courseShareSQLHelper;
    }

    private CourseShareSQLHelper(Context context) {
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
