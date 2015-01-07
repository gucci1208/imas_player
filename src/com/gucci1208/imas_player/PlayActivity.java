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

		//�Ƃ肠�����S��false��
		playing = new boolean[12];
		for (int i = 0; i < playing.length; i++) {
			playing[i] = false;
		}

		bt = new ImageView[12];
		for (int i = 0; i < bt.length; i++) {
			//bt[i] = (ImageView)findViewById(R.id.bt1);
			//bt1�`bt12��ImageView��ID�𕶎��񂩂�擾����
			int viewId = getResources().getIdentifier("bt" + (i + 1), "id", getPackageName());
			bt[i] = (ImageView)findViewById(viewId);
		}

		//�摜���Z�b�g
		setButtonImage();

		Intent intent = getIntent();
		now_page = intent.getIntExtra("PAGE", 0);

		//�����ɂ�郉�O�����炷���߂ɁA�ɗ͂����ŏ�����������Ⴄ
		//���f�[�^�����X�g�A
		ProgressDialog mProgress =null;
		Handler mHandler = new Handler();
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("   ");
		mProgress.show();
		prepareSounds t = new prepareSounds(this, mHandler, mProgress);
		t.start();
	}

	private void setButtonImage() {
		//�{�^���ɉ摜���Z�b�g����
		for (int i = 0; i < bt.length; i++) {

			//�����񂩂�摜��drawable��ID���擾����
			int image_id_on		= getResources().getIdentifier(Const.CHARA_CODE[i] + "_on", "drawable", getPackageName());
			int image_id_off	= getResources().getIdentifier(Const.CHARA_CODE[i] + "_off", "drawable", getPackageName());
			System.out.println("image_id_on:" + image_id_on);
			System.out.println("image_id_off:" + image_id_off);
			//bt[i].setImageResource(image_id_on);

			if (!playing[i]) {
				//bt[i].setImageResource(image_id_off);
			}

			//�N���b�N���X�i�[
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

			//�Đ�����T�[�r�X�̏���
			serviceIntent = new Intent(context, PlaySoundMain.class);
		}

		//���Ԃ������鏈��
		public void run() {
			try {
				//�ȃR�[�h���擾
				String sound_code = Const.SOUND_CODE[now_page];

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

			// �X���b�h���I�������ꍇ�A�I���������Ƃ�Handler�ɒm�点��B
			mHandler.post(new Runnable() {
				public void run() {
					// �_�C�A���O������
					mProgress.dismiss();

					//�Đ��J�n
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

		//�I�����ɒ�~������
		stopService(new Intent(getApplicationContext(), PlaySoundMain.class));
	}

}
