package com.gucci1208.imas_player;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gucci1208.imas_player.model.Const;
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
		setContentView(R.layout.lo_play);

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

		//Service���J�n
		serviceIntent = new Intent(this, PlaySoundMain.class);
		serviceIntent.putExtra("PAGE", now_page);
		startService(serviceIntent);
	}

	private void setButtonImage() {
		//�{�^���ɉ摜���Z�b�g����
		for (int i = 0; i < bt.length; i++) {
			//�����񂩂�摜��drawable��ID���擾����
			int image_id_on		= getResources().getIdentifier(Const.CHARA_CODE[i] + "_on", "drawable", getPackageName());
			int image_id_off	= getResources().getIdentifier(Const.CHARA_CODE[i] + "_off", "drawable", getPackageName());
			bt[i].setImageResource(image_id_on);

			if (!playing[i]) {
				bt[i].setImageResource(image_id_off);
			}

			//�N���b�N���X�i�[
			final int inner_id = i;
			bt[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (playing[inner_id]) {
						playing[inner_id] = false;

						sendBroadCast(inner_id, false);
					}
					else {
						playing[inner_id] = true;

						sendBroadCast(inner_id, true);
					}

					setButtonImage();
				}
			});
		}
	}

	//BroadcastReceiver����ă��X�i�[�C�x���g�𑗂�
	protected void sendBroadCast(int pos_id, boolean isPlaying) {
		Intent broadcastIntent = new Intent();
		broadcastIntent.putExtra("POS_ID", pos_id);
		broadcastIntent.putExtra("PLAYING", isPlaying);
		broadcastIntent.setAction("UPDATE_ACTION");
		sendBroadcast(broadcastIntent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		//�I�����ɒ�~������
		stopService(new Intent(getApplicationContext(), PlaySoundMain.class));
	}
}
