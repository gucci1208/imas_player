package com.gucci1208.imas_player.service;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PlaySoundInSelect extends Service {
	MediaPlayer mp;
	String sound_code;

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
		//表示中の曲のコードを受け取る
		sound_code = intent.getStringExtra("SOUND_CODE");

		try {
			mp = new MediaPlayer();

			//コードからオフボーカル音源を取得する
			AssetFileDescriptor afd = getAssets().openFd(sound_code + "_off.mp3");
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mp.prepare();

			//ループ設定
			mp.setLooping(true);
			mp.start();
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


		/*
		int timeout = 60 * 1000;
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mp.isPlaying()) {
					mp.stop();
				}
			}
		}, timeout);
		*/

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mp.isPlaying()) {
			mp.stop();
		}
	}
}
