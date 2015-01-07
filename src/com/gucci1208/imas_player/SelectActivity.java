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

		//�����̎��
		String[] date_strs = getResources().getStringArray(R.array.sound_names);
		//�������Ƃ̃N���X�z��
		sound_datas = new ArrayList<ModelSound>();
		//�������F�ԍ��A�������F�T�E���h���A��O�����F�W���P�b�g�摜
		for (int i = 0; i < date_strs.length; i++) {
			sound_datas.add(new ModelSound(SelectActivity.this, i, date_strs[i], Const.SOUND_CODE[i]));
		}

		//�y�[�W�r���[�̃Z�b�g
		now_page = 0;
		SetPageView();

		//�I�t�{�[�J���������Đ�
		stopService(new Intent(getApplicationContext(), PlaySoundInSelect.class));
		Intent serviceIntent = new Intent(getApplicationContext(), PlaySoundInSelect.class);
		serviceIntent.putExtra("SOUND_CODE", Const.SOUND_CODE[now_page]);
		startService(serviceIntent);
	}

	private void SetPageView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		PagerAdapter mPagerAdapter = new AdapterPager(this, sound_datas);
		viewPager.setAdapter(mPagerAdapter);

		//�y�[�W���ς�������Ƃ��󂯎��C�x���g
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				now_page = position;

				//�܂���~
				stopService(new Intent(getApplicationContext(), PlaySoundInSelect.class));

				//�I�t�{�[�J���������Đ�
				Intent serviceIntent = new Intent(getApplicationContext(), PlaySoundInSelect.class);
				serviceIntent.putExtra("SOUND_CODE", Const.SOUND_CODE[now_page]);
				startService(serviceIntent);

				SetArrowImages();
			}
		});

		viewPager.setCurrentItem(now_page);
		SetArrowImages();
	}

	//���̌��������`
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
		viewPager.setCurrentItem(now_page, true);	//2�Ԗڂ̈����̓A�j���[�V�����̗L��
	}
	public void TO_RIGHT(View v) {
		now_page = ((now_page + 1) % sound_datas.size());
		viewPager.setCurrentItem(now_page, true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		//�I�����ɒ�~������
		stopService(new Intent(getApplicationContext(), PlaySoundInSelect.class));
	}
}
