package com.example.smogdex;

public abstract class SmogdexConstants {

	public static final int LONGEST_NAME = 11; // src: http://bulbapedia.bulbagarden.net/wiki/Fletchinder_%28Pok%C3%A9mon%29

	public static abstract class NetworkConstants {
		public static final String SMOGON_URL_BASE = "http://sweepercalc.com/stats/data/";
		public static final String POKEAPI_URL_BASE = "http://pokeapi.co/api/v1/pokemon/";

		public static final String OU = "ou";
		public static final String UU = "uu";
		public static final String LC = "lc";

		public static final String USAGE = "usagedata.js";
		public static final String MOVESET = "moveset.js";
	}
}
