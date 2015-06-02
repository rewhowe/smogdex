package com.example.smogdex.models;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

public class PokemonData /*extends Serializable*/ {

	public static abstract class Format {
		public static final int OU = 0;
		public static final int UU = 1;
		public static final int LC = 2;
		public static final int NUM_FORMATS = 3;
	}

	public static class StatsData {
		// TODO: convert to gson model
		public Integer mHP = 0;
		public Integer mAtk = 0;
		public Integer mDef = 0;
		public Integer mSpA = 0;
		public Integer mSpD = 0;
		public Integer mSpe = 0;
	}

	/*
	 * It might be a good idea to change these to String:Integer pairs in order to
	 * allow sorting. They should be in-order right now, but technically it'll be
	 * undefined when retrieving from the db.
	 */
	public class MovesetData {

		private MovesetData() {
			mBuilds = new ArrayList<Pair<String, String>>();
			mAbilities = new ArrayList<Pair<String, String>>();
			mMoves = new ArrayList<Pair<String, String>>();
			mItems = new ArrayList<Pair<String, String>>();
			mCounters = new ArrayList<Pair<String, String>>();
		}

		public final List<Pair<String, String>> mBuilds;
		public final List<Pair<String, String>> mAbilities;
		public final List<Pair<String, String>> mMoves;
		public final List<Pair<String, String>> mItems;
		public final List<Pair<String, String>> mCounters;

		public void addBuild(String build, String usage) {
			mBuilds.add(new Pair<String, String>(build, usage));
		}

		public void addAbility(String ability, String usage) {
			mAbilities.add(new Pair<String, String>(ability, usage));
		}

		public void addMove(String move, String usage) {
			mMoves.add(new Pair<String, String>(move, usage));
		}

		public void addItem(String item, String usage) {
			mItems.add(new Pair<String, String>(item, usage));
		}

		public void addCounter(String counter, String usage) {
			mCounters.add(new Pair<String, String>(counter, usage));
		}
	}

	public final String mAlias;
	public final String mUsage[];
	public final MovesetData mMovesetData[];
	public StatsData mStatsData;

	public PokemonData(String alias) {
		mAlias = alias;
		mUsage = new String[Format.NUM_FORMATS];
		mStatsData = new StatsData();
		mMovesetData = new MovesetData[] {
				new MovesetData(),
				new MovesetData(),
				new MovesetData()
		};
	}

}
