package com.gucci1208.imas_player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.gucci1208.imas_player.service.PlaySoundMAIN0;

public class PlayActivity extends Activity {
	private int now_page;

	private ImageView bt1;
	private ImageView bt2;
	private ImageView bt3;
	private ImageView bt4;
	private ImageView bt5;
	private ImageView bt6;
	private ImageView bt7;
	private ImageView bt8;
	private ImageView bt9;
	private ImageView bt10;
	private ImageView bt11;
	private ImageView bt12;

	private boolean playing1;
	private boolean playing2;
	private boolean playing3;
	private boolean playing4;
	private boolean playing5;
	private boolean playing6;
	private boolean playing7;
	private boolean playing8;
	private boolean playing9;
	private boolean playing10;
	private boolean playing11;
	private boolean playing12;

	private Intent serviceIntent0;
	private Intent serviceIntent1;
	private Intent serviceIntent2;
	private Intent serviceIntent3;
	private Intent serviceIntent4;
	private Intent serviceIntent5;
	private Intent serviceIntent6;
	private Intent serviceIntent7;
	private Intent serviceIntent8;
	private Intent serviceIntent9;
	private Intent serviceIntent10;
	private Intent serviceIntent11;
	private Intent serviceIntent12;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lo_select);

		bt1 = (ImageView)findViewById(R.id.bt1);
		bt2 = (ImageView)findViewById(R.id.bt2);
		bt3 = (ImageView)findViewById(R.id.bt3);
		bt4 = (ImageView)findViewById(R.id.bt4);
		bt5 = (ImageView)findViewById(R.id.bt5);
		bt6 = (ImageView)findViewById(R.id.bt6);
		bt7 = (ImageView)findViewById(R.id.bt7);
		bt8 = (ImageView)findViewById(R.id.bt8);
		bt9 = (ImageView)findViewById(R.id.bt9);
		bt10 = (ImageView)findViewById(R.id.bt10);
		bt11 = (ImageView)findViewById(R.id.bt11);
		bt12 = (ImageView)findViewById(R.id.bt12);

		//とりあえず全部falseで
		playing1 = false;
		playing2 = false;
		playing3 = false;
		playing4 = false;
		playing5 = false;
		playing6 = false;
		playing7 = false;
		playing8 = false;
		playing9 = false;
		playing10 = false;
		playing11 = false;
		playing12 = false;

		//画像をセット
		setButtonImage();

		Intent intent = getIntent();
		now_page = intent.getIntExtra("PAGE", 0);

		//処理によるラグを減らすために、極力ここで処理をやっちゃう

		//再生するサービスの準備
		serviceIntent0 = new Intent(this, PlaySoundMAIN0.class);

	}

	private void setButtonImage() {
	}
}
