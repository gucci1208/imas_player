package com.gucci1208.imas_player;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gucci1208.imas_player.adapter.AdapterPager;
import com.gucci1208.imas_player.model.Const;
import com.gucci1208.imas_player.model.ModelSound;
import com.gucci1208.imas_player.service.PlaySoundInSelect;

public class SelectActivity extends Activity {
	private int now_page;
	private ViewPager viewPager;

	private ArrayList<ModelSound> sound_datas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lo_select);

		//音声の種類
		String[] date_strs = getResources().getStringArray(R.array.sound_names);
		//音声ごとのクラス配列
		sound_datas = new ArrayList<ModelSound>();
		//第一引数：番号、第二引数：サウンド名、第三引数：ジャケット画像
		for (int i = 0; i < date_strs.length; i++) {
			sound_datas.add(new ModelSound(SelectActivity.this, i, date_strs[i], Const.SOUND_CODE[i]));
		}

		//ページビューのセット
		now_page = 0;
		SetPageView();

		//オフボーカル音源を再生
		stopService(new Intent(getApplicationContext(), PlaySoundInSelect.class));
		Intent serviceIntent = new Intent(getApplicationContext(), PlaySoundInSelect.class);
		serviceIntent.putExtra("SOUND_CODE", Const.SOUND_CODE[now_page]);
		startService(serviceIntent);
	}

	private void SetPageView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		PagerAdapter mPagerAdapter = new AdapterPager(this, sound_datas);
		viewPager.setAdapter(mPagerAdapter);

		//ページが変わったことを受け取るイベント
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				now_page = position;

				//まず停止
				stopService(new Intent(getApplicationContext(), PlaySoundInSelect.class));

				//オフボーカル音源を再生
				Intent serviceIntent = new Intent(getApplicationContext(), PlaySoundInSelect.class);
				serviceIntent.putExtra("SOUND_CODE", Const.SOUND_CODE[now_page]);
				startService(serviceIntent);

				SetArrowImages();
			}
		});

		viewPager.setCurrentItem(now_page);
		SetArrowImages();
	}

	//矢印の見え方を定義
	private void SetArrowImages() {
		Button arrow_l	= (Button)findViewById(R.id.bt_l);
		Button arrow_r	= (Button)findViewById(R.id.bt_r);
		TextView date	= (TextView)findViewById(R.id.sound_name);

		if (now_page == 0) {arrow_l.setVisibility(View.INVISIBLE);}
		else {arrow_l.setVisibility(View.VISIBLE);}

		if (now_page == (sound_datas.size()-1)) {arrow_r.setVisibility(View.INVISIBLE);}
		else {arrow_r.setVisibility(View.VISIBLE);}

		date.setText(sound_datas.get(now_page).getName());

		if (now_page > 0) {
			arrow_l.setText(sound_datas.get(now_page - 1).getName());
		}
		if (now_page < (sound_datas.size()-1)) {
			arrow_r.setText(sound_datas.get(now_page + 1).getName());
		}
	}

	public void TO_LEFT(View v) {
		now_page = ((now_page + sound_datas.size() - 1) % sound_datas.size());
		viewPager.setCurrentItem(now_page, true);	//2番目の引数はアニメーションの有無
	}
	public void TO_RIGHT(View v) {
		now_page = ((now_page + 1) % sound_datas.size());
		viewPager.setCurrentItem(now_page, true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		//終了時に停止させる
		stopService(new Intent(getApplicationContext(), PlaySoundInSelect.class));
	}
}
