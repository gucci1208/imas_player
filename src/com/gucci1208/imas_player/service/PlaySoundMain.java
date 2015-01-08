package com.gucci1208.imas_player.service;

import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.gucci1208.imas_player.model.Const;
import com.gucci1208.imas_player.receiver.UpdateReceiver;

public class PlaySoundMain extends Service {
	MediaPlayer[] mp;
	float default_volume_l[];
	float default_volume_r[];

	UpdateReceiver upReceiver;
	IntentFilter intentFilter;

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
		//クリックリスナーイベントを受け取るための準備
		upReceiver = new UpdateReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction("UPDATE_ACTION");
		registerReceiver(upReceiver, intentFilter);
		upReceiver.registerHandler(updateHandler);

		//Activityから選択中の曲のページを受け取る
		int page = intent.getIntExtra("PAGE", 0);

		Handler mHandler = new Handler();
		prepareSounds t = new prepareSounds(PlaySoundMain.this, mHandler, page);
		t.start();

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		for (int i = 0; i < mp.length; i++) {
			if (mp[i].isPlaying()) {
				mp[i].stop();
			}
		}
	}

	private class prepareSounds extends Thread {
		Handler mHandler;
		int page;

		public prepareSounds(Context context, Handler mHandler, int page) {
			this.mHandler = mHandler;
			this.page = page;
		}

		//時間がかかる処理
		public void run() {
			mp = new MediaPlayer[13];
			default_volume_l = new float[12];
			default_volume_r = new float[12];

			//曲コードを取得
			String sound_code = Const.SOUND_CODE[page];

			try {
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
					if (i == 0) {
						mp[i].setVolume(Const.PAN_VOLUME[0], Const.PAN_VOLUME[0]);
					}
					else {
						int pointer_l = 0;
						int pointer_r = 0;

						//左半分のキャラ
						if (i <= 6) {
							pointer_l = 0;
							pointer_r = Const.PAN_VOLUME.length - 1 - (i - 1);
						}
						//右半分のキャラ
						else {
							pointer_r = 0;
							pointer_l = i - 6;
						}

						default_volume_l[i - 1] = Const.PAN_VOLUME[pointer_l];
						default_volume_r[i - 1] = Const.PAN_VOLUME[pointer_r];

						System.out.println(Const.CHARA_CODE[i - 1] + " -> " + Const.PAN_VOLUME[pointer_l] + "/" + Const.PAN_VOLUME[pointer_r]);

						//mp[i].setVolume(Const.PAN_VOLUME[pointer_l], Const.PAN_VOLUME[pointer_r]);
						//最初は全員オフ
						mp[i].setVolume(0f, 0f);
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
					//再生
					for (int i = 0; i < mp.length; i++) {
						mp[i].start();
					}
				}
			});
		}
	}

	// サービスから値を受け取ったら動かしたい内容を書く
	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			int pos_id = bundle.getInt("POS_ID");
			boolean nowPlaying = bundle.getBoolean("PLAYING");

			if (nowPlaying) {
				mp[pos_id + 1].setVolume(default_volume_l[pos_id], default_volume_r[pos_id]);
			}
			else {
				mp[pos_id + 1].setVolume(0f, 0f);
			}
		}
	};
}
