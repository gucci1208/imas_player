package com.gucci1208.imas_player.model;

public class Const {
	final static public String[] SOUND_CODE = {
		"change",
		"brandnewday",
	};

	final static public String[] CHARA_CODE = {
		"rtk",
		"hbk",
		"tkn",
		"ior",
		"yyi",
		"chy",

		"hrk",
		"mk",
		"mkt",
		"ykh",
		"ammm",
		"azs",
	};

	//計算でリソース割かれるの嫌なので事前に計算しておく
	final static public float[] PAN_VOLUME = {
		1.0f,
		0.909f,
		//0.818f,
		0.727f,
		//0.636f,
		0.545f,
		//0.454f,
		0.363f,
		//0.272f,
		0.181f,
		//0.090f,
		0.0f,
	};
}
