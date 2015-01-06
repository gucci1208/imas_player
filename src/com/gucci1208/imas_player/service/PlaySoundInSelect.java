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
		//�\�����̋Ȃ̃R�[�h���󂯎��
		sound_code = intent.getStringExtra("SOUND_CODE");

		try {
			mp = new MediaPlayer();

			//�R�[�h����I�t�{�[�J���������擾����
			AssetFileDescriptor afd = getAssets().openFd(sound_code + "_off.mp3");
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mp.prepare();

			//���[�v�ݒ�
			mp.setLooping(true);
			mp.start();
		} catch (IllegalArgumentException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �����������ꂽ catch �u���b�N
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
