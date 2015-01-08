package com.gucci1208.imas_player.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UpdateReceiver extends BroadcastReceiver {

	public static Handler handler;

	@Override
	public void onReceive(Context context, Intent intent) {
		//�N���b�N�C�x���g���琔�l���擾����
		Bundle bundle = intent.getExtras();
		int pos_id = bundle.getInt("POS_ID");
		boolean isPlaying = bundle.getBoolean("PLAYING");

		if (handler != null) {
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putInt("POS_ID", pos_id);
			data.putBoolean("PLAYING", isPlaying);

			msg.setData(data);
			handler.sendMessage(msg);
		}
	}

	/**
	 * Service���X�V
	 */
	public void registerHandler(Handler locationUpdateHandler) {
		handler = locationUpdateHandler;
	}

}