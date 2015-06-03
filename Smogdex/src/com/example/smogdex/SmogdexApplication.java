package com.example.smogdex;

import android.app.Application;

import com.example.smogdex.db.SmogdexDatabaseHelper;

public class SmogdexApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		SmogdexDatabaseHelper.initialize(this);
	}
}
