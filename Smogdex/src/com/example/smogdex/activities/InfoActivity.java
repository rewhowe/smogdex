package com.example.smogdex.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.smogdex.PokemonDataManager;
import com.example.smogdex.PokemonDataManager.PokemonDataRequest;
import com.example.smogdex.PokemonListItem;
import com.example.smogdex.R;
import com.example.smogdex.views.PokemonListItemView;

public class InfoActivity extends Activity {

	private PokemonListItem mSelectedPokemon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		PokemonListItemView infoDisplay = (PokemonListItemView) findViewById(R.id.info_display_temp);

		mSelectedPokemon = getIntent().getParcelableExtra(PokemonListItem.TAG);
		infoDisplay.setupForItem(mSelectedPokemon, this.getApplicationContext(), 6);
	}

	@Override
	protected void onStart() {
		super.onStart();
		PokemonDataManager.getPokemonData(mSelectedPokemon.getAlias(), new PokemonDataRequest() {
			@Override
			public void onReceive() {
				InfoActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						InfoActivity.this.findViewById(R.id.loading_spinner).setVisibility(View.GONE);
					}
				});
			}
		});
	}
}
