package com.cyberagent.courseshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.ArrayList;


public class FirstActivity extends Activity {
    private Button searchButton;
    private EditText start;
    private EditText goal;
    private NumberPicker hours;
    private NumberPicker minutes;
    private EditText transitPoint;
    private int timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        findViews();
        initViews();

    }

    private void findViews() {
        this.searchButton = (Button)findViewById(R.id.searchButton);
        this.start = (EditText)findViewById(R.id.start);
        this.goal = (EditText)findViewById(R.id.goal);
        this.hours = (NumberPicker)findViewById(R.id.hours);
        this.minutes = (NumberPicker)findViewById(R.id.minutes);
        this.transitPoint = (EditText)findViewById(R.id.transitPoint);
    }

    private void initViews(){
        hours.setMaxValue(23);
        hours.setMinValue(0);
        hours.setValue(1);
        minutes.setMaxValue(59);
        minutes.setMinValue(0);
        minutes.setValue(0);

        //ボタンクリック時の処理を実装する
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intentをつかって画面遷移する
                Intent intent = new Intent(FirstActivity.this,
                        CourceActivity.class);
                intent.putExtra("start", start.getText().toString());
                intent.putExtra("goal", goal.getText().toString());
                intent.putExtra("transitPoint", transitPoint.getText().toString());
                timeLeft = hours.getValue()* 60 + minutes.getValue();
                intent.putExtra("timeLeft",timeLeft);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first, menu);
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
