package com.gucci1208.imas_player;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.ImageView;

import com.gucci1208.imas_player.model.Const;
import com.gucci1208.imas_player.service.PlaySoundInSelect;
import com.gucci1208.imas_player.service.PlaySoundMain;

public class PlayActivity extends Activity {
	private int now_page;

	private boolean[] playing;
	private ImageView[] bt;

	MediaPlayer[] mp;

	private Intent serviceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lo_select);

		//とりあえず全部falseで
		playing = new boolean[12];
		for (int i = 0; i < playing.length; i++) {
			playing[i] = false;
		}

		bt = new ImageView[12];
		for (int i = 0; i < bt.length; i++) {
			//bt[i] = (ImageView)findViewById(R.id.bt1);
			//bt1～bt12のImageViewのIDを文字列から取得する
			int viewId = getResources().getIdentifier("bt" + (i + 1), "id", getPackageName());
			bt[i] = (ImageView)findViewById(viewId);
		}

		//画像をセット
		setButtonImage();

		Intent intent = getIntent();
		now_page = intent.getIntExtra("PAGE", 0);

		//処理によるラグを減らすために、極力ここで処理をやっちゃう
		//旧データをリストア
		ProgressDialog mProgress =null;
		Handler mHandler = new Handler();
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("   ");
		mProgress.show();
		prepareSounds t = new prepareSounds(this, mHandler, mProgress);
		t.start();
	}

	private void setButtonImage() {
		//ボタンに画像をセットする
		for (int i = 0; i < bt.length; i++) {

			//文字列から画像のdrawableのIDを取得する
			int image_id_on		= getResources().getIdentifier(Const.CHARA_CODE[i] + "_on", "drawable", getPackageName());
			int image_id_off	= getResources().getIdentifier(Const.CHARA_CODE[i] + "_off", "drawable", getPackageName());
			System.out.println("image_id_on:" + image_id_on);
			System.out.println("image_id_off:" + image_id_off);
			//bt[i].setImageResource(image_id_on);

			if (!playing[i]) {
				//bt[i].setImageResource(image_id_off);
			}

			//クリックリスナー
			final int inner_id = i;
			/*
			bt[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					playing[inner_id] = !playing[inner_id];
					setButtonImage();
				}
			});
			*/
		}
	}

	private class prepareSounds extends Thread {
		Handler mHandler;
		ProgressDialog mProgress;

		public prepareSounds(Context context, Handler mHandler, ProgressDialog mProgress) {
			this.mHandler = mHandler;
			this.mProgress = mProgress;

			mp = new MediaPlayer[13];

			//再生するサービスの準備
			serviceIntent = new Intent(context, PlaySoundMain.class);
		}

		//時間がかかる処理
		public void run() {
			try {
				//曲コードを取得
				String sound_code = Const.SOUND_CODE[now_page];

				//MediaPlayerの準備
				AssetFileDescriptor[] afd = new AssetFileDescriptor[13];
				for (int i = 0; i < mp.length; i++) {
					mp[i] = new MediaPlayer();

					//読み込む音声ファイルの名前
					String mp3_filename = "";
					if (i == 0) {
						mp3_filename = sound_code + "_off.mp3";
					}
					else {
						mp3_filename = sound_code + "_" + Const.CHARA_CODE[i - 1] + ".mp3";
					}
					//コードから音源を取得する
					afd[i] = getAssets().openFd(mp3_filename);
					mp[i].setDataSource(afd[i].getFileDescriptor(), afd[i].getStartOffset(), afd[i].getLength());
					mp[i].prepare();

					//ループ設定
					mp[i].setLooping(true);

					//ボリューム設定で左右のパンをふる（0～1のfloat値）
					int num = mp.length - 1;
					float per = 1.0f / num;

					if (i == 0) {
						mp[i].setVolume(1.0f, 1.0f);
					}
					else {
						float left = 1.0f - (i - 1) * per;
						float right = (i - 1) * per;
						if (left > 1.0f) {
							left = 1.0f;
						}
						if (right > 1.0f) {
							right = 1.0f;
						}
						if (left < 0) {
							left = 0;
						}
						if (right < 0) {
							right = 0;
						}
						mp[i].setVolume(left, right);
					}
				}
			} catch (IllegalArgumentException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

			// スレッドが終了した場合、終了したことをHandlerに知らせる。
			mHandler.post(new Runnable() {
				public void run() {
					// ダイアログを消す
					mProgress.dismiss();

					//再生開始
					startService(serviceIntent);
				}
			});
		}
	}

	private class PlaySoundMAIN extends Service {
		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}

		@Override
		public void onCreate() {
			super.onCreate();
		}

		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			System.out.println("aaaaaaaaaaa");
			for (int i = 0; i < mp.length; i++) {
				System.out.println("i:" + i);
				mp[i].start();
			}

			return START_STICKY;
		}

		@Override
		public void onDestroy() {
			super.onDestroy();

			for (int i = 0; i < mp.length; i++) {
				System.out.println("mp[" + i + "].isPlaying():" + mp[i].isPlaying());
				if (mp[i].isPlaying()) {
					mp[i].stop();
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		//終了時に停止させる
		stopService(new Intent(getApplicationContext(), PlaySoundMain.class));
	}

}
