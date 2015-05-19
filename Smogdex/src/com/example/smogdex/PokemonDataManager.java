package com.example.smogdex;

public class PokemonDataManager {

	public static interface PokemonDataRequest {
		// Uncomment when PokemonData is implemented
		public void onReceive(/*PokemonData data*/);
	}

	// Uncomment when NetworkTask is implemented
	// private static NetworkTask mNetworkTask;

	public static void getPokemonData(String alias, PokemonDataRequest request) {
		/*
		PokemonData[] data = SmogdexDatabaseHelper.get(alias);
		if (data != null) {
		    request.onReceive(data);
		}

		if (mNetworkTask != null || mNetworkTask.getStatus == Status.FINISHED) {
			// TODO: create a NetworkResponseHandler
			//	the handler will probably take a list of input streams or something
			//	will need to save them to the database
			//  after everything is done, call request.onReceive()
			// TODO: create a new NetworkTask with the response handler
		} else {
			// TODO: make some sort of queue to get the data
		}
		 */
	}
}
