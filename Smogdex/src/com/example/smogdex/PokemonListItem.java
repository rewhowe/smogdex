package com.example.smogdex;

import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

public class PokemonListItem implements Parcelable {

	public static final String TAG = PokemonListItem.class.getSimpleName();

	public static final Creator<PokemonListItem> CREATOR = new Creator<PokemonListItem>() {
		@Override
		public PokemonListItem createFromParcel(Parcel source) {
			return new PokemonListItem(source);
		}

		@Override
		public PokemonListItem[] newArray(int size) {
			return new PokemonListItem[size];
		}
	};

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

	private PokemonListItem(Parcel source) {
		mNumber = source.readInt();
		mImage = source.readInt();
		mName = source.readString();
		mAlias = source.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mNumber);
		dest.writeInt(mImage);
		dest.writeString(mName);
		dest.writeString(getAlias());
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

	@Override
	public int describeContents() {
		return 0;
	}
}
