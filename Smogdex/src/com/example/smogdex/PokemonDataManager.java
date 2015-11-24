package com.example.smogdex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.example.smogdex.SmogdexConstants.NetworkConstants;
import com.example.smogdex.db.SmogdexDatabaseHelper;
import com.example.smogdex.models.PokemonData;
import com.example.smogdex.models.PokemonData.StatsData;
import com.example.smogdex.network.NetworkTask;
import com.example.smogdex.network.NetworkTask.NetworkResponseHandler;
import com.google.gson.Gson;

public class PokemonDataManager {

	private static final String TAG = PokemonDataManager.class.getSimpleName();

	public static interface PokemonDataRequest {
		public void onReceive(PokemonData data);
	}

	private static HashMap<String, PokemonData> mDataCache = new HashMap<String, PokemonData>();
	private static ArrayList<Runnable> mJobs = new ArrayList<Runnable>();

	private static PokemonDataRequest mPendingRequest;
	private static PokemonListItem mRequestedPokemon;
	private static NetworkTask mNetworkTask;

	public static synchronized void getPokemonData(final PokemonListItem selectedPokemon, final PokemonDataRequest request) {
		mRequestedPokemon = selectedPokemon;
		mPendingRequest = request;

		// attempt to load from data map
		if (mDataCache.containsKey(mRequestedPokemon.getAlias())) {
			Log.d(TAG, "receive from data map");
			PokemonData data = mDataCache.get(mRequestedPokemon.getAlias());
			if (data.mStatsDataRetrieved) {
				mPendingRequest.onReceive(data);
			} else {
				updateStatsData();
			}
			return;
		}

		// attempt to load from database
		SmogdexDatabaseHelper helper = SmogdexDatabaseHelper.getInstance();
		PokemonData data = helper.get(mRequestedPokemon.getAlias());
		if (data != null) {
			Log.d(TAG, "receive from database");
			if (data.mStatsDataRetrieved) {
				mDataCache.put(data.mAlias, data);
				mPendingRequest.onReceive(data);
			} else {
				updateStatsData();
			}
			return;
		}

		// fetch data from network
		if (mNetworkTask == null) {
			Log.d(TAG, "fetching data");
			fetchNewData();
		} else {
			Log.d(TAG, "network task not null; doing nothing");
			// TODO: is this scenario important?
		}
	}

	private static void runNextJob() {
		if (mJobs.size() > 0) {
			Runnable r = mJobs.remove(0);
			r.run();
		} else {
			mNetworkTask = null;
		}
	}

	private static void fetchNewData() {
		mJobs.add(fetchStatsData);
		mJobs.add(fetchUsageData);
		mJobs.add(fetchMovesetData);
		mJobs.add(deliverData);
		mJobs.add(saveDataMap);
		runNextJob();
	}

	private static void updateStatsData() {
		mJobs.add(fetchStatsData);
		mJobs.add(deliverData);
		final String requestedPokemon = mRequestedPokemon.getAlias();
		mJobs.add(new Runnable() {
			@Override
			public void run() {
				SmogdexDatabaseHelper helper = SmogdexDatabaseHelper.getInstance();
				helper.put(mDataCache.get(requestedPokemon));
			}
		});
		runNextJob();
	}

	private static Runnable fetchStatsData = new Runnable() {
		@Override
		public void run() {
			URL url = null;
			try {
				url = new URL(NetworkConstants.POKEAPI_URL_BASE + mRequestedPokemon.mNumber);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return;
			}

			mNetworkTask = new NetworkTask(new NetworkResponseHandler() {
				@Override
				public void onReceive(int which, InputStream is) {
					Log.d(TAG, "fetchStatsData onReceive");
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					StringBuilder builder = new StringBuilder();
					String line;
					try {
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					Gson gson = new Gson();
					StatsData stats = gson.fromJson(builder.toString(), StatsData.class);

					if (!mDataCache.containsKey(mRequestedPokemon.getAlias())) {
						mDataCache.put(mRequestedPokemon.getAlias(), new PokemonData(mRequestedPokemon.getAlias()));
					}
					mDataCache.get(mRequestedPokemon.getAlias()).mStatsData = stats;
					mDataCache.get(mRequestedPokemon.getAlias()).mStatsDataRetrieved = true;
				}

				@Override
				public void onError(int which) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onSuccess() {
					runNextJob();
				}

				@Override
				public void onFailure(int numErrors) {
					// TODO Auto-generated method stub
				}
			});

			mNetworkTask.execute(url);
		}
	};

	private static Runnable fetchUsageData = new Runnable() {
		@Override
		public void run() {
			URL[] urls = null;
			try {
				urls = new URL[]{
						new URL(NetworkConstants.SMOGON_URL_BASE + NetworkConstants.OU + NetworkConstants.USAGE),
						new URL(NetworkConstants.SMOGON_URL_BASE + NetworkConstants.UU + NetworkConstants.USAGE),
						new URL(NetworkConstants.SMOGON_URL_BASE + NetworkConstants.LC + NetworkConstants.USAGE)
				};
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return;
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

								if (!mDataCache.containsKey(name)) {
									mDataCache.put(name, new PokemonData(name));
								}
								mDataCache.get(name).mUsage[which] = usage;
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
					runNextJob();
				}

				@Override
				public void onFailure(int numErrors) {
					Log.d(TAG, "fetchUsageData failure");
				}
			});

			mNetworkTask.execute(urls);
		};
	};

	private static Runnable fetchMovesetData = new Runnable() {
		@Override
		public void run() {
			URL[] urls = null;
			try {
				urls = new URL[]{
						new URL(NetworkConstants.SMOGON_URL_BASE + NetworkConstants.OU + NetworkConstants.MOVESET),
						new URL(NetworkConstants.SMOGON_URL_BASE + NetworkConstants.UU + NetworkConstants.MOVESET),
						new URL(NetworkConstants.SMOGON_URL_BASE + NetworkConstants.LC + NetworkConstants.MOVESET)
				};
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return;
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
								if (!mDataCache.containsKey(name)) {
									mDataCache.put(name, new PokemonData(name));
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
													mDataCache.get(name).mMovesetData[which].addAbility(key, val);
												} else if (attr.equals("items")) {
													mDataCache.get(name).mMovesetData[which].addItem(key, val);
												} else if (attr.equals("spreads")) {
													mDataCache.get(name).mMovesetData[which].addBuild(key, val);
												} else if (attr.equals("moves")) {
													mDataCache.get(name).mMovesetData[which].addMove(key, val);
												} else if (attr.equals("checks")) {
													mDataCache.get(name).mMovesetData[which].addCounter(key, val);
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
					runNextJob();
				}

				@Override
				public void onFailure(int numErrors) {
					Log.d(TAG, "fetchMovesetData failure");
				}
			});

			mNetworkTask.execute(urls);
		}
	};

	private static Runnable deliverData = new Runnable() {
		@Override
		public void run() {
			mPendingRequest.onReceive(mDataCache.get(mRequestedPokemon.getAlias()));
			runNextJob();
		}
	};

	private static Runnable saveDataMap = new Runnable() {
		@Override
		public void run() {
			Log.d(TAG, "saving data map to database...");
			SmogdexDatabaseHelper helper = SmogdexDatabaseHelper.getInstance();
			// each call to post is one full transaction... it would be more efficient
			// to do all of this as one transaction, however, since there are 700+
			// pokemon to store, if something goes wrong, we'd like to have some of them at least
			for (Entry<String, PokemonData> entry : mDataCache.entrySet()) {
				helper.post(entry.getValue());
			}
			Log.d(TAG, "... finished");
		}
	};
}
