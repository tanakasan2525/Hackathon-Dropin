package com.cyberagent.courseshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Random;


public class SearchActivity extends Activity {
    Intent firstActivityIntent;
    private LinearLayout mainLayout;
	private ArrayList<LinearLayout> btnListLayouts;

    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mainLayout = (LinearLayout) findViewById(R.id.searchActivity);
		btnListLayouts = new ArrayList<LinearLayout>();
		btnListLayouts.add((LinearLayout) findViewById(R.id.btn_list));
		btnListLayouts.add((LinearLayout) findViewById(R.id.btn_list2));
		final AutoCompleteTextView autoCompView = (AutoCompleteTextView)findViewById(R.id.autocomplete);
		autoCompView.setAdapter(new PlaceAutoCompleteAdapter(this, R.layout.search_list_item));
        autoCompView.postDelayed(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.showSoftInput(autoCompView, 0);
            }
        }, 200);
        firstActivityIntent = getIntent();

		autoCompView.setOnKeyListener(new View.OnKeyListener() {
			//コールバックとしてonKey()メソッドを定義
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// エンターキーで決定
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    firstActivityIntent.putExtra("keyword", autoCompView.getText().toString());
					returnIntent(autoCompView.getText().toString());
                    setResult(RESULT_OK, firstActivityIntent);
                    finish();
					return true;
				}

				return false;
			}
		});

		((Button)findViewById(R.id.randomButton)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectAtRandom();
			}
		});

		UIUtil.animateAppearing(mainLayout, 800);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
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

	private void returnIntent(String keyword) {
		Intent i = new Intent();
		i.putExtra("keyword", keyword);
		setResult(RESULT_OK, i);
		finish();
	}
    @Override
    public boolean onTouchEvent(MotionEvent event) {

// キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
// 背景にフォーカスを移す
        mainLayout.requestFocus();

        return true;
    }

	private void selectAtRandom() {
		long seed = System.currentTimeMillis();
		Random rnd = new Random(seed);
		final int rndNum = rnd.nextInt(20) + 30;

		final ArrayList<ToggleButton> btnList = new ArrayList<ToggleButton>();
		int btnNumBuffer = 0;
		for (LinearLayout ll : btnListLayouts) {
			btnNumBuffer += ll.getChildCount();
			for (int i = 0; i < ll.getChildCount(); ++i) {
				ToggleButton btn = (ToggleButton) ll.getChildAt(i);
				btnList.add(btn);
			}
		}

		final int btnNum = btnNumBuffer;

		final Handler handler = new Handler();

		new Thread(new Runnable() {
			public void run() {
				int counter = 0;
				while(true){
					try {
						Thread.sleep(100);
					}catch(InterruptedException e){
					}
					final int counterForHandler = counter;
					handler.post(new Runnable() {
						public void run() {
							btnList.get((counterForHandler + btnNum - 1) % btnNum).setChecked(false);
							btnList.get(counterForHandler % btnNum).setChecked(true);
						}
					});

					if (++counter > rndNum) {
						btnList.get(rndNum % btnNum).callOnClick();
						break;
					}
				}
			}
		}).start();
	}

	public void onClickRestaurant(View view) {
		returnIntent("レストラン");
	}

	public void onClickCafe(View view) {
		returnIntent("カフェ");
	}

	public void onClickBar(View view) {
		returnIntent("居酒屋");
	}

	public void onClickGasSt(View view) {
		returnIntent("ガソリンスタンド");
	}

	public void onClickBookStore(View view) {returnIntent("本屋");}

	public void onClickCinema(View view) {returnIntent("映画館");}

	public void onClickKaraoke(View view) {returnIntent("カラオケ");}




}
