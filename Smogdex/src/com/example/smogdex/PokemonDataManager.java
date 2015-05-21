package com.example.smogdex;

import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask.Status;
import android.util.Log;

import com.example.smogdex.SmogdexConstants.NetworkConstants;
import com.example.smogdex.network.NetworkTask;
import com.example.smogdex.network.NetworkTask.NetworkResponseCallback;

public class PokemonDataManager {

	private static final String TAG = PokemonDataManager.class.getSimpleName();

	public static interface PokemonDataRequest {
		// Uncomment when PokemonData is implemented
		public void onReceive(/*PokemonData data*/);
	}

	private static NetworkTask mNetworkTask;

	public static void getPokemonData(String alias, final PokemonDataRequest request) {
		/*
		PokemonData[] data = SmogdexDatabaseHelper.get(alias);
		if (data != null) {
		    request.onReceive(data);
		}
		 */
		if (mNetworkTask == null || mNetworkTask.getStatus() == Status.FINISHED) {
			mNetworkTask = new NetworkTask(new NetworkResponseCallback() {
				@Override
				public void onSuccess() {
					Log.d(TAG, "success");
					request.onReceive();
				}

				@Override
				public void onError() {
					Log.d(TAG, "error");
				}
			});

			URL[] urls = null;
			try {
				urls = new URL[]{
						new URL(NetworkConstants.URL_BASE + NetworkConstants.OU + NetworkConstants.USAGE),
						new URL(NetworkConstants.URL_BASE + NetworkConstants.UU + NetworkConstants.USAGE),
						new URL(NetworkConstants.URL_BASE + NetworkConstants.LC + NetworkConstants.USAGE),
						new URL(NetworkConstants.URL_BASE + NetworkConstants.OU + NetworkConstants.MOVESET),
						new URL(NetworkConstants.URL_BASE + NetworkConstants.UU + NetworkConstants.MOVESET),
						new URL(NetworkConstants.URL_BASE + NetworkConstants.LC + NetworkConstants.MOVESET)
				};
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			mNetworkTask.execute(urls);
		} else {
			// TODO: make some sort of queue to get the data
		}
	}
}
