package com.warmtel.news;

import android.app.Application;

public class NewsApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		RequestManager.init(this);
	}
}
