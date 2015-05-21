package com.example.smogdex.models;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

public class PokemonData /*extends Serializable*/ {

	public static class Format {
		public static final int OU = 0;
		public static final int UU = 1;
		public static final int LC = 2;
		public static final int NUM_FORMATS = 3;

		private Format() {}
	}

	public class MovesetData {
		private MovesetData() {
			mBuilds = new ArrayList<Pair<String, String>>();
			mAbilities = new ArrayList<Pair<String, String>>();
			mMoves = new ArrayList<Pair<String, String>>();
			mItems = new ArrayList<Pair<String, String>>();
			mCounters = new ArrayList<Pair<String, String>>();
		}

		private final List<Pair<String, String>> mBuilds;
		private final List<Pair<String, String>> mAbilities;
		private final List<Pair<String, String>> mMoves;
		private final List<Pair<String, String>> mItems;
		private final List<Pair<String, String>> mCounters;

		public List<Pair<String, String>> getBuilds() {
			return mBuilds;
		}

		public List<Pair<String, String>> getAbilities() {
			return mAbilities;
		}

		public List<Pair<String, String>> getMoves() {
			return mMoves;
		}

		public List<Pair<String, String>> getItems() {
			return mItems;
		}

		public List<Pair<String, String>> getCounters() {
			return mCounters;
		}

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

	private final String mAlias;
	private final String mUsage[];
	private final MovesetData mMovesetData[];

	public PokemonData(String alias) {
		mAlias = alias;
		mUsage = new String[Format.NUM_FORMATS];
		mMovesetData = new MovesetData[] {
				new MovesetData(),
				new MovesetData(),
				new MovesetData()
		};
	}

	public String getUsage(int format) {
		return mUsage[format];
	}

	public MovesetData getMovesetData(int format) {
		return mMovesetData[format];
	}

	public void setUsage(int format, String usage) {
		mUsage[format] = usage;
	}

	public void setMovesetData(int format, MovesetData data) {
		mMovesetData[format] = data;
	}

}
