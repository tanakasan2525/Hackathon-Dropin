package com.cyberagent.courseshare;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * TODO: document your custom view class.
 */
public class CourseView extends LinearLayout {

    private Course course;

    public CourseView(Context context, Course course) {
        super(context);
        this.course = course;
        setLayout(context);
    }

    public CourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayout(context);
    }

    public void setLayout(Context context) {
        View layout = LayoutInflater.from(context).inflate(R.layout.sample_course_view, this);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(course.getName());
    }
}
