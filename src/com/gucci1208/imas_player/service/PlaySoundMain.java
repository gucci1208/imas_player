package com.gucci1208.imas_player.service;

import java.io.IOException;

import com.gucci1208.imas_player.model.Const;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PlaySoundMain extends Service {
	MediaPlayer[] mp;

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
		mp = new MediaPlayer[13];

		try {
			//�ȃR�[�h���擾
			String sound_code = Const.SOUND_CODE[1];

			//MediaPlayer�̏���
			AssetFileDescriptor[] afd = new AssetFileDescriptor[13];
			for (int i = 0; i < mp.length; i++) {
				mp[i] = new MediaPlayer();

				//�ǂݍ��މ����t�@�C���̖��O
				String mp3_filename = "";
				if (i == 0) {
					mp3_filename = sound_code + "_off.mp3";
				}
				else {
					mp3_filename = sound_code + "_" + Const.CHARA_CODE[i - 1] + ".mp3";
				}
				//�R�[�h���特�����擾����
				afd[i] = getAssets().openFd(mp3_filename);
				mp[i].setDataSource(afd[i].getFileDescriptor(), afd[i].getStartOffset(), afd[i].getLength());
				mp[i].prepare();

				//���[�v�ݒ�
				mp[i].setLooping(true);

				//�{�����[���ݒ�ō��E�̃p�����ӂ�i0�`1��float�l�j
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
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}

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
