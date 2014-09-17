package com.cyberagent.courseshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

public class FirstActivity extends Activity {

	private ImageView topTitleView;
    private Button searchButton;
    private BootstrapButton startButton;
    private BootstrapButton goalButton;
    private EditText start;
    private EditText goal;
	private TextView startText;
	private TextView endText;
    private BootstrapButton timeLeftButton;
    private NumberPicker hours;
    private NumberPicker minutes;
    private EditText transitPoint;
    private Button transitPointButton;
    //空き時間の初期設定（分）
    private int hoursLeft = 1;
    private int minutesLeft = 0;
    private int timeLeft = 60;
    //検索ができるかどうかのチェック
    private boolean isSearchEnabled = false;
    public static final int REQUEST_CODE = 100;

    // キーボード表示を制御するためのオブジェクト
    InputMethodManager inputMethodManager;
    // 背景のレイアウト
    private LinearLayout mainLayout;

    Intent intentCourse;
    Intent intentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //titleを非表示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_first);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mainLayout = (LinearLayout) findViewById(R.id.firstActivity);
        //遷移させる画面の決定
        findViews();
        initViews();
    }

    private void findViews() {
		this.topTitleView = (ImageView)findViewById(R.id.top_title);
        this.searchButton = (Button)findViewById(R.id.searchButton);
        this.start = (EditText)findViewById(R.id.start);
        this.goal = (EditText)findViewById(R.id.goal);
		this.startText = (TextView)findViewById(R.id.start_text);
		this.endText = (TextView)findViewById(R.id.end_text);
		this.timeLeftButton = (BootstrapButton) findViewById(R.id.timeLeftButton);
        this.startButton = (BootstrapButton)findViewById(R.id.startButton);
        this.goalButton = (BootstrapButton)findViewById(R.id.goalButton);
//        this.hours = (NumberPicker)findViewById(R.id.hours);
//        this.minutes = (NumberPicker)findViewById(R.id.minutes);
//        this.transitPoint = (EditText)findViewById(R.id.transitPoint);
        this.transitPointButton = (Button)findViewById(R.id.transitPointButton);
        intentCourse = new Intent(FirstActivity.this, CourseActivity.class);
        intentSearch = new Intent(FirstActivity.this, SearchActivity.class);
    }

    private void initViews(){
        timeLeftButton.setText("空き時間： "+ Integer.toString(hoursLeft)+"時間"+Integer.toString(minutesLeft)+"分");

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setText("現在地");
            }
        });

        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goal.setText("現在地");
            }
        });

        //ボタンクリック時の処理を実装する
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchEnabled = showSearchAlert();
                if (isSearchEnabled == true ) {
                    sendInputData(intentCourse);
                    startActivity(intentCourse);
                }
            }
        });
        timeLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });

        transitPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(intentSearch, REQUEST_CODE);
            }
        });

		start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean b) {
				if (b) {
					startText.setBackgroundColor(Color.parseColor("#8cc152")); // focus
				} else {
					startText.setBackgroundColor(Color.parseColor("#53cc91")); // default
				}
			}
		});

		goal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean b) {
				if (b) {
					endText.setBackgroundColor(Color.parseColor("#8cc152"));
				} else {
					endText.setBackgroundColor(Color.parseColor("#53cc91"));
				}
			}
		});

		UIUtil.animateAppearing(mainLayout, 2000);

    }

    private void sendInputData(Intent intent) {
        intent.putExtra("start", start.getText().toString());
        intent.putExtra("goal", goal.getText().toString());
        intent.putExtra("transitPoint", transitPointButton.getText().toString());
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
                        hoursLeft = hours.getValue();
                        minutesLeft = minutes.getValue();
                        timeLeft = hoursLeft*60 + minutesLeft;
                        timeLeftButton.setText("空き時間： "+ Integer.toString(hours.getValue()) +"時間 "
                                +Integer.toString(minutes.getValue())+"分");
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

    private boolean showSearchAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // アラートダイアログのタイトルを設定します
        if (start.getText().length() == 0) {
            alertDialogBuilder.setMessage("出発地を選択して下さい");
        } else if (goal.getText().length() == 0 ) {
            alertDialogBuilder.setMessage("目的地を選択して下さい");
        } else {
            return true;
        }
        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setPositiveButton("OK",
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
        return false;
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
    // 画面タップ時の処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {

// キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
// 背景にフォーカスを移す
        mainLayout.requestFocus();

        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extra = data.getExtras();
                String keyword = extra.getString("keyword");
                transitPointButton.setText(keyword);
            }
        }
    }

}
