package com.hrj.mojian.service;

import android.os.Binder;


public class MJBinder extends Binder {

	private MJService mojianService;

	public MJBinder(MJService mojianService) {
		this.mojianService = mojianService;
	};

	public MJService getService() {
		return mojianService;
	}
}
