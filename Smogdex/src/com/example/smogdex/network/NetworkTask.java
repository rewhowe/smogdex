package com.example.smogdex.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class NetworkTask extends AsyncTask<URL, Void, Void> {

	private static final String TAG = NetworkTask.class.getSimpleName();

	public static abstract class NetworkResponseHandler {
		public abstract void onReceive(int which, InputStream is);
		public abstract void onError(int which);
		public abstract void onSuccess();
		public abstract void onFailure(int numErrors);
	}

	private final NetworkResponseHandler mHandler;

	public NetworkTask(NetworkResponseHandler Handler) {
		mHandler = Handler;
	}

	@Override
	protected Void doInBackground(URL... urls) {
		int numErrors = 0;
		for (int i = 0; i < urls.length; i++) {
			URL url = urls[i];
			HttpURLConnection urlConnection = null;
			try {
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream is = new BufferedInputStream(urlConnection.getInputStream());
				mHandler.onReceive(i, is);
			} catch (IOException e) {
				e.printStackTrace();
				numErrors++;
				mHandler.onError(i);
			} finally {
				urlConnection.disconnect();
			}
		}
		if (numErrors == 0) {
			mHandler.onSuccess();
		} else {
			mHandler.onFailure(numErrors);
		}
		return null;
	}


}
