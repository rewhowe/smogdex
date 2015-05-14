package com.example.smogdex;

import java.util.Locale;

public class PokemonListItem {
	public int image;
	public String name;
	private String alias;
	public int number;

	public PokemonListItem(int number, int image, String name, String alias) {
		this.number = number;
		this.image = image;
		this.name = name;
		this.alias = alias;
	}

	public String getAlias() {
		if (alias == null) {
			return name.toLowerCase(Locale.ENGLISH);
		} else {
			return alias;
		}
	}
}
