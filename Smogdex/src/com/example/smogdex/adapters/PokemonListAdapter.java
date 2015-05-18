package com.example.smogdex.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.smogdex.FuzzySearcher;
import com.example.smogdex.PokemonListInitializer;
import com.example.smogdex.PokemonListItem;
import com.example.smogdex.views.PokemonListItemView;

public class PokemonListAdapter extends ArrayAdapter<PokemonListItemView> {

	private static final String TAG = PokemonListAdapter.class.getSimpleName();

	private List<PokemonListItem> POKEMON_ITEMS;

	private Context mContext;
	private ArrayList<PokemonListItem> mDisplayList;

	public PokemonListAdapter(Context context, int resource) {
		super(context, resource);
		mContext = context;
		POKEMON_ITEMS = new ArrayList<PokemonListItem>();
		PokemonListInitializer.initialize(POKEMON_ITEMS, context);
		mDisplayList = new ArrayList<PokemonListItem>();
		mDisplayList.addAll(POKEMON_ITEMS);
	}

	public void updateDisplayList(String query) {
		mDisplayList.clear();
		if (query.isEmpty()) {
			mDisplayList.addAll(POKEMON_ITEMS);
		} else {
			Log.d(TAG, "searching for " + query);
//			FuzzySearcher.quickSearch(query, POKEMON_ITEMS, mDisplayList);
			FuzzySearcher.sortedSearch(query, POKEMON_ITEMS, mDisplayList);
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PokemonListItemView view;
		if (convertView != null) {
			view = (PokemonListItemView) convertView;
		} else {
			view = new PokemonListItemView(mContext);
		}
		PokemonListItem item = mDisplayList.get(position);

		view.setupForItem(item, mContext, 2);
		return view;
	}

	@Override
	public int getCount() {
		return mDisplayList.size();
	}

	public PokemonListItem getPokemon(int position) {
		return mDisplayList.get(position);
	}
}
