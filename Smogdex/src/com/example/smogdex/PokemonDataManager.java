package com.example.smogdex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.example.smogdex.SmogdexConstants.NetworkConstants;
import com.example.smogdex.models.PokemonData;
import com.example.smogdex.network.NetworkTask;
import com.example.smogdex.network.NetworkTask.NetworkResponseHandler;

public class PokemonDataManager {

	private static final String TAG = PokemonDataManager.class.getSimpleName();

	public static interface PokemonDataRequest {
		public void onReceive(PokemonData data);
	}

	private static HashMap<String, PokemonData> mDataMap;
	private static PokemonDataRequest mPendingRequest;
	private static PokemonListItem mRequestedPokemon;
	private static NetworkTask mNetworkTask;

	public static void getPokemonData(final PokemonListItem selectedPokemon, final PokemonDataRequest request) {
		mRequestedPokemon = selectedPokemon;
		mPendingRequest = request;

		// TODO: instead, just load the datamap from the db
//		PokemonData[] data = SmogdexDatabaseHelper.get(alias);
//		if (data != null) {
//		    request.onReceive(data);
//		}
		// TODO: replace with above, after the db is implemented
		if (mDataMap != null && mDataMap.containsKey(mRequestedPokemon.getAlias())) {
			Log.d(TAG, "receive from data map");
			request.onReceive(mDataMap.get(mRequestedPokemon.getAlias()));
			return;
		}

		if (mNetworkTask == null) {
			mDataMap = new HashMap<String, PokemonData>();
			fetchUsageData();
		} else {
			// TODO: is this scenario important?
		}
	}

	private static void fetchUsageData() {
		URL[] urls = null;
		try {
			urls = new URL[]{
					new URL(NetworkConstants.URL_BASE + NetworkConstants.OU + NetworkConstants.USAGE),
					new URL(NetworkConstants.URL_BASE + NetworkConstants.UU + NetworkConstants.USAGE),
					new URL(NetworkConstants.URL_BASE + NetworkConstants.LC + NetworkConstants.USAGE)
			};
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		mNetworkTask = new NetworkTask(new NetworkResponseHandler() {
			@Override
			public void onReceive(int which, InputStream is) {
				Log.d(TAG, "fetchUsageData onReceive");
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line;
				try {
					Pattern p = Pattern.compile("\\[ \"\\d+\", \"(.+)\", \"(.+)\" \\]");
					while ((line = reader.readLine()) != null) {
						Matcher m = p.matcher(line);
						if (m.find()) {
							String name = m.group(1);
							String usage = m.group(2);
							Log.d(TAG, name + " " + usage);

							if (!mDataMap.containsKey(name)) {
								mDataMap.put(name, new PokemonData(name));
							}
							mDataMap.get(name).mUsage[which] = usage;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(int which) {
				Log.d(TAG, "fetchUsageData error: " + which);
			}

			@Override
			public void onSuccess() {
				Log.d(TAG, "fetchUsageData success");
				fetchMovesetData();
			}

			@Override
			public void onFailure(int numErrors) {
				Log.d(TAG, "fetchUsageData failure");
			}
		});

		mNetworkTask.execute(urls);
	}

	private static void fetchMovesetData() {
		URL[] urls = null;
		try {
			urls = new URL[]{
					new URL(NetworkConstants.URL_BASE + NetworkConstants.OU + NetworkConstants.MOVESET),
					new URL(NetworkConstants.URL_BASE + NetworkConstants.UU + NetworkConstants.MOVESET),
					new URL(NetworkConstants.URL_BASE + NetworkConstants.LC + NetworkConstants.MOVESET)
			};
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		mNetworkTask = new NetworkTask(new NetworkResponseHandler() {
			@Override
			public void onReceive(int which, InputStream is) {
				Log.d(TAG, "fetchMovesetData onReceive");
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line;
				Pattern pAlias = Pattern.compile("^\\s+\"(.+)\" : \\{");
				Pattern pAttrName = Pattern.compile("'(.+)' : \\[");
				Pattern pAttrVal = Pattern.compile("\\[ \"(.+)\", \"(.+)\" \\]");

				try {
					line = reader.readLine(); // skip first line

					while (true) {
						line = reader.readLine();
						Matcher m = pAlias.matcher(line);
						if (m.find()) {
							String name = m.group(1);
							if (!mDataMap.containsKey(name)) {
								mDataMap.put(name, new PokemonData(name));
							}

							while (true) {
								line = reader.readLine();
								m = pAttrName.matcher(line);
								if (m.find()) {
									String attr = m.group(1);

									while (true) {
										line = reader.readLine();
										m = pAttrVal.matcher(line);
										if (m.find()) {
											String key = m.group(1);
											String val = m.group(2);

											if (attr.equals("abilities")) {
												mDataMap.get(name).mMovesetData[which].addAbility(key, val);
											} else if (attr.equals("items")) {
												mDataMap.get(name).mMovesetData[which].addItem(key, val);
											} else if (attr.equals("spreads")) {
												mDataMap.get(name).mMovesetData[which].addBuild(key, val);
											} else if (attr.equals("moves")) {
												mDataMap.get(name).mMovesetData[which].addMove(key, val);
											} else if (attr.equals("checks")) {
												mDataMap.get(name).mMovesetData[which].addCounter(key, val);
											} else {
												// skip
											}
										} else { // ],
											break;
										}
									}
								} else { // },
									break;
								}
							}
						} else { // };
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(int which) {
				Log.d(TAG, "fetchMovesetData error: " + which);
			}

			@Override
			public void onSuccess() {
				Log.d(TAG, "fetchMovesetData success");
				mPendingRequest.onReceive(mDataMap.get(mRequestedPokemon.getAlias()));
				// TODO: store datamap in db
			}

			@Override
			public void onFailure(int numErrors) {
				Log.d(TAG, "fetchMovesetData failure");
			}
		});

		mNetworkTask.execute(urls);
	}
}
