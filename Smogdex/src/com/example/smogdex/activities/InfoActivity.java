package com.example.smogdex.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.example.smogdex.PokemonDataManager;
import com.example.smogdex.PokemonDataManager.PokemonDataRequest;
import com.example.smogdex.PokemonListItem;
import com.example.smogdex.R;
import com.example.smogdex.models.PokemonData;
import com.example.smogdex.models.PokemonData.Format;
import com.example.smogdex.views.PokemonListItemView;

public class InfoActivity extends Activity {

	private static final String TAG = InfoActivity.class.getSimpleName();

	private PokemonListItem mSelectedPokemon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		PokemonListItemView infoDisplay = (PokemonListItemView) findViewById(R.id.info_display_temp);

		mSelectedPokemon = getIntent().getParcelableExtra(PokemonListItem.TAG);
		infoDisplay.setupForItem(mSelectedPokemon, this.getApplicationContext(), 6);

		Log.d(TAG, "sIS=" + savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle(mSelectedPokemon.getFormattedNumber() + " " + mSelectedPokemon.mName);

		PokemonDataManager.getPokemonData(mSelectedPokemon, new PokemonDataRequest() {
			@Override
			public void onReceive(final PokemonData data) {
				InfoActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						InfoActivity.this.findViewById(R.id.loading_spinner).setVisibility(View.GONE);

						TextView tv = (TextView) InfoActivity.this.findViewById(R.id.temp_text);
						StringBuilder builder = new StringBuilder();
						builder.append("Usage = ").append(data.mUsage[Format.OU]).append("\n");
						builder.append("HP = ").append(data.mStatsData.mHP).append("\n")
						.append("Atk = ").append(data.mStatsData.mAtk).append("\n")
						.append("Def = ").append(data.mStatsData.mDef).append("\n")
						.append("SpA = ").append(data.mStatsData.mSpA).append("\n")
						.append("SpD = ").append(data.mStatsData.mSpD).append("\n")
						.append("Spe = ").append(data.mStatsData.mSpe).append("\n");
						builder.append("Builds:").append("\n");
						for (Pair<String, String> build : data.mMovesetData[Format.OU].mBuilds) {
							builder.append("  ").append(build.first).append("\n");
						}
						builder.append("Abilities:").append("\n");
						for (Pair<String, String> ability : data.mMovesetData[Format.OU].mAbilities) {
							builder.append("  ").append(ability.first).append("\n");
						}

						tv.setText(builder.toString());
						tv.setVisibility(View.VISIBLE);

					}
				});
			}
		});
	}
}
