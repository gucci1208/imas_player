package com.gucci1208.imas_player.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class WrapperShared {
	Context context;

	SharedPreferences pref;
	final String FILE_NAME = "file_name";

	public static final String KEY_FIRST		= "key_first";
	public static final String KEY_SLEEP_ON		= "key_sleep";

	public WrapperShared(Context context) {
		this.context = context;
		this.pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
	}

	// データの保存
	public void saveInt(String key, int value) {
		Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void saveString(String key, String value) {
		Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void saveBoolean(String key, boolean value) {
		Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	// データの取得
	public int getInt(String key, int default_value) {
		return pref.getInt(key, default_value);
	}

	public String getString(String key, String default_value) {
		return pref.getString(key, default_value);
	}

	public boolean getBoolean(String key, boolean default_value) {
		return pref.getBoolean(key, default_value);
	}
}
