package com.example.smogdex;

import android.graphics.Color;
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

	public enum Type {
		NORMAL("#AAAA77"),
		FIRE("#FF6622"),
		FIGHTING("#CC3320"),
		WATER("#5577FF"),
		FLYING("#AAAAFF"),
		GRASS("#55CC33"),
		POISON("#AA44AA"),
		ELECTRIC("#FFDD33"),
		GROUND("#EECC66"),
		PSYCHIC("#FF5588"),
		ROCK("#AA7711"),
		ICE("#99DDDD"),
		BUG("#AAAA33"),
		DRAGON("#9955EE"),
		GHOST("#775599"),
		DARK("#665544"),
		STEEL("#CCDDDD"),
		FAIRY("#EE99AA");

		public final int mColor;

		Type(String color) {
			mColor = Color.parseColor(color);
		}

		public String toString() {
			return name();
		};
	}

	public final int mImage;
	public final String mName;
	private String mAlias;
	public final int mNumber;
	public final Type mType1;
	public final Type mType2;

	public PokemonListItem(int number, int image, String name, String alias, Type type1, Type type2) {
		mNumber = number;
		mImage = image;
		mName = name;
		mAlias = alias;
		mType1 = type1;
		mType2 = type2;
	}

	private PokemonListItem(Parcel source) {
		mNumber = source.readInt();
		mImage = source.readInt();
		mName = source.readString();
		mAlias = source.readString();
		mType1 = Type.valueOf(source.readString());
		String type2 = source.readString();
		mType2 = type2 != null ? Type.valueOf(type2) : null;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mNumber);
		dest.writeInt(mImage);
		dest.writeString(mName);
		dest.writeString(getAlias());
		dest.writeString(mType1.name());
		dest.writeString(mType2 != null ? mType2.name() : null);
	}

	public String getAlias() {
		return mAlias == null ? mName : mAlias;
	}

	public String getFormattedNumber() {
		return "#" + String.format("%03d", mNumber);
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
