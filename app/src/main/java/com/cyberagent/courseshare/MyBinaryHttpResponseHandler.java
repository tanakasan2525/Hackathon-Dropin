package com.cyberagent.courseshare;

import android.widget.ImageView;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.cyberagent.courseshare.Map.Pin;

/**
 * Created by shogo on 2014/09/16.
 */
public class MyBinaryHttpResponseHandler extends BinaryHttpResponseHandler{
    ImageView handleMemberIcon;
    Pin handleMemberPin;

    public MyBinaryHttpResponseHandler(ImageView icon, Pin pin) {
        super();
        this.handleMemberIcon = icon;
        this.handleMemberPin = pin;
    }
}
