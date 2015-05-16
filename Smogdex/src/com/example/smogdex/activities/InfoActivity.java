package com.example.smogdex.activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.smogdex.PokemonListItem;
import com.example.smogdex.R;
import com.example.smogdex.views.PokemonListItemView;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		PokemonListItemView infoDisplay = (PokemonListItemView) findViewById(R.id.info_display_temp);

		PokemonListItem selectedPokemon = getIntent().getParcelableExtra(PokemonListItem.TAG);
		infoDisplay.setupForItem(selectedPokemon, this.getApplicationContext(), 6);
	}
}
