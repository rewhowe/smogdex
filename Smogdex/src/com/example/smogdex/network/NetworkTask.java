package com.example.smogdex.network;

import java.net.URL;

import android.os.AsyncTask;

public class NetworkTask extends AsyncTask<URL, Void, Void> {

	public static interface NetworkResponseCallback {
		// TODO: fill in params
		public void onSuccess();
		public void onError();
	}

	private final NetworkResponseCallback mCallback;

	public NetworkTask(NetworkResponseCallback callback) {
		mCallback = callback;
	}

	@Override
	protected Void doInBackground(URL... urls) {
		for (URL url : urls) {
			// pass
		}
		mCallback.onSuccess();
		return null;
	}


}
