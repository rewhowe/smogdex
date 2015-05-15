package com.example.smogdex.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import com.example.smogdex.R;
import com.example.smogdex.adapters.PokemonListAdapter;


public class MainActivity extends Activity implements TextWatcher {

	private static final String TAG = MainActivity.class.getSimpleName();

	private PokemonListAdapter mPokemonListAdapter;
	private EditText mSearchBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// hide keyboard
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mSearchBar = (EditText) findViewById(R.id.search_bar);
		mSearchBar.addTextChangedListener(this);
		findViewById(R.id.search_clear).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearchBar.setText("");
			}
		});

		mPokemonListAdapter = new PokemonListAdapter(this.getApplicationContext(), 0);
		ListView plv = (ListView) findViewById(R.id.pokemon_list);
		plv.setAdapter(mPokemonListAdapter);
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
		mPokemonListAdapter.updateDisplayList(arg0.toString());
	}

}