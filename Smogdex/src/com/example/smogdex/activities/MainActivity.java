package com.example.smogdex.activities;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import com.example.smogdex.R;
import com.example.smogdex.adapters.PokemonListAdapter;


public class MainActivity extends Activity implements TextWatcher {

	private static final String TAG = MainActivity.class.getSimpleName();

	private PokemonListAdapter mPokemonListAdapter;
	private Locale mCurrentLocale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// hide keyboard
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		((EditText) findViewById(R.id.search_bar)).addTextChangedListener(this);

		mPokemonListAdapter = new PokemonListAdapter(this.getApplicationContext(), 0);
		ListView plv = (ListView) findViewById(R.id.pokemon_list);
		plv.setAdapter(mPokemonListAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// locale is set onResume because MAYBE the user changes it while the app is open
		mCurrentLocale = getResources().getConfiguration().locale;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		mPokemonListAdapter.updateDisplayList(arg0.toString(), mCurrentLocale);
	}

}