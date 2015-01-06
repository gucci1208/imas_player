package com.gucci1208.imas_player.model;

import android.content.Context;
import android.widget.ImageView;

public class ModelSound {
	int id;
	String name;
	int image_id;
	ImageView img;

	public ModelSound(Context context, int id, String name, String sound_code) {
		super();
		this.id = id;
		this.name = name;

		//�����񂩂�摜��drawable��ID���擾����
		this.image_id = context.getResources().getIdentifier("jacket_" + sound_code, "drawable", context.getPackageName());

		// �R���X�g���N�^�ł�null
		img = null;
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getImage() {
		return image_id;
	}

	public void setImageView(ImageView img) {
		this.img = img;
	}
}
