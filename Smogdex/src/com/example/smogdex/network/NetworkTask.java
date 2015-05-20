package com.example.smogdex.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;

public class NetworkTask extends AsyncTask<URL, Void, Void> {

	private static final String TAG = NetworkTask.class.getSimpleName();

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
		ArrayList<InputStream> results = new ArrayList<InputStream>();
		for (URL url : urls) {
			HttpURLConnection urlConnection = null;
			try {
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				results.add(in);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				urlConnection.disconnect();
			}
		}
		Log.d(TAG, "resuts = " + results.size());
		mCallback.onSuccess();
		return null;
	}


}
