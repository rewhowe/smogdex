package com.example.smogdex;

import java.util.Locale;

public class PokemonListItem {
	public final int mImage;
	public final String mName;
	private String mAlias;
	public final int mNumber;

	public PokemonListItem(int number, int image, String name, String alias) {
		mNumber = number;
		mImage = image;
		mName = name;
		mAlias = alias;
	}

	public String getAlias() {
		if (mAlias == null) {
			return mName.toLowerCase(Locale.ENGLISH);
		} else {
			return mAlias;
		}
	}

	@Override
	public String toString() {
		return mName;
	}
}
