package com.jaydi.wtd.application;

import android.app.Application;

public class WtdApplication extends Application {
	private static WtdApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		initApplication();
	}

	private void initApplication() {
		instance = this;
	}

	public final static WtdApplication getInstance() {
		return instance;
	}

	@Override
	public void onTerminate() {
		instance = null;
		super.onTerminate();
	}
}
