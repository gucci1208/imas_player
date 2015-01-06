package com.gucci1208.imas_player.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gucci1208.imas_player.PlayActivity;
import com.gucci1208.imas_player.R;
import com.gucci1208.imas_player.model.Const;
import com.gucci1208.imas_player.model.ModelSound;
import com.gucci1208.imas_player.service.PlaySoundInSelect;

public class AdapterPager extends PagerAdapter {
	Activity activity;
	private int N;
	private LayoutInflater _inflater;
	ArrayList<ModelSound> sound_datas;

	public AdapterPager(Context c, ArrayList<ModelSound> sound_datas) {
		super();
		this.activity = (Activity) c;
		_inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.sound_datas = sound_datas;
		N = this.sound_datas.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		LinearLayout layout = (LinearLayout) _inflater.inflate(R.layout.page_sound, null);

		//ImageViewのセット
		ImageView img = (ImageView) layout.findViewById(R.id.frame_image);
		sound_datas.get(position).setImageView(img);
		img.setImageResource(sound_datas.get(position).getImage());

		//再生ページへ
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//流れてるオフボーカル音源を停止
				activity.stopService(new Intent(activity, PlaySoundInSelect.class));

				Intent intent = new Intent(activity, PlayActivity.class);
				intent.putExtra("PAGE", position);
				intent.setAction(Intent.ACTION_VIEW);
				activity.startActivity(intent);
			}
		});

		container.addView(layout);
		return layout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public int getCount() {
		return N;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}
}
