package com.cyberagent.courseshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;


public class FirstActivity extends Activity {
    private Button searchButton;
    private EditText start;
    private EditText goal;
    private Button timeLeftButton;
    private NumberPicker hours;
    private NumberPicker minutes;
    private EditText transitPoint;
    private int timeLeft;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        intent = new Intent(FirstActivity.this,CourceActivity.class);
        findViews();
        initViews(intent);


    }

    private void findViews() {
        this.searchButton = (Button)findViewById(R.id.searchButton);
        this.start = (EditText)findViewById(R.id.start);
        this.goal = (EditText)findViewById(R.id.goal);
        this.timeLeftButton = (Button)findViewById(R.id.timeLeftButton);
//        this.hours = (NumberPicker)findViewById(R.id.hours);
//        this.minutes = (NumberPicker)findViewById(R.id.minutes);
        this.transitPoint = (EditText)findViewById(R.id.transitPoint);
    }

    private void initViews(final Intent intent){

        //ボタンクリック時の処理を実装する
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInputData(intent);
                startActivity(intent);
            }
        });
        timeLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });
    }

    private void sendInputData(Intent intent) {
        intent.putExtra("start", start.getText().toString());
        intent.putExtra("goal", goal.getText().toString());
        intent.putExtra("transitPoint", transitPoint.getText().toString());
        timeLeft = hours.getValue()* 60 + minutes.getValue();
        intent.putExtra("timeLeft",timeLeft);
    }

    private void showTimeDialog() {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.timealertdialog,(ViewGroup)findViewById(R.id.timealertdialog_layout));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // アラートダイアログのタイトルを設定します
        alertDialogBuilder.setTitle("暇な時間");
        //timePickerを表示します
        alertDialogBuilder.setView(layout);
        //timePickerの設定をします
        this.hours = (NumberPicker)layout.findViewById(R.id.hours);
        this.minutes = (NumberPicker)layout.findViewById(R.id.minutes);
        hours.setMaxValue(23);
        hours.setMinValue(0);
        hours.setValue(1);
        minutes.setMaxValue(59);
        minutes.setMinValue(0);
        minutes.setValue(0);


        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setPositiveButton("設定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // アラートダイアログの否定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setNegativeButton("キャンセル",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // アラートダイアログのキャンセルが可能かどうかを設定します
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        // アラートダイアログを表示します
        alertDialog.show();
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
