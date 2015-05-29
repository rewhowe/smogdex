package com.example.smogdex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smogdex.db.SmogdexContract.AbilityTable;
import com.example.smogdex.db.SmogdexContract.BuildTable;
import com.example.smogdex.db.SmogdexContract.CounterTable;
import com.example.smogdex.db.SmogdexContract.ItemTable;
import com.example.smogdex.db.SmogdexContract.MoveTable;
import com.example.smogdex.db.SmogdexContract.PokemonTable;
import com.example.smogdex.db.SmogdexContract.UsageTable;
import com.example.smogdex.models.PokemonData;
import com.example.smogdex.models.PokemonData.Format;
import com.example.smogdex.models.PokemonData.MovesetData;

public class SmogdexDBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "Smogdex.db";
	public static final int DATABASE_VERSION = 1;

	public SmogdexDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(PokemonTable.CREATE);
		db.execSQL(UsageTable.CREATE);
		db.execSQL(BuildTable.CREATE);
		db.execSQL(AbilityTable.CREATE);
		db.execSQL(MoveTable.CREATE);
		db.execSQL(ItemTable.CREATE);
		db.execSQL(CounterTable.CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(CounterTable.DROP);
		db.execSQL(ItemTable.DROP);
		db.execSQL(MoveTable.DROP);
		db.execSQL(AbilityTable.DROP);
		db.execSQL(BuildTable.DROP);
		db.execSQL(UsageTable.DROP);
		db.execSQL(PokemonTable.DROP);
	}

	public void post(PokemonData data) {
		SQLiteDatabase db = getWritableDatabase();

		try {
			// VERY important - we're doing a huge batch insertion
			db.beginTransaction();

			// PokemonTable
			ContentValues pokemonValues = new ContentValues();
			pokemonValues.put(PokemonTable.COLUMN_NAME_ALIAS, data.mAlias);
			pokemonValues.put(PokemonTable.COLUMN_NAME_HP, data.mStatsData.mHP);
			pokemonValues.put(PokemonTable.COLUMN_NAME_ATK, data.mStatsData.mAtk);
			pokemonValues.put(PokemonTable.COLUMN_NAME_DEF, data.mStatsData.mDef);
			pokemonValues.put(PokemonTable.COLUMN_NAME_SPA, data.mStatsData.mSpA);
			pokemonValues.put(PokemonTable.COLUMN_NAME_SPD, data.mStatsData.mSpD);
			pokemonValues.put(PokemonTable.COLUMN_NAME_SPE, data.mStatsData.mSpe);

			db.insertOrThrow(PokemonTable.TABLE_NAME, null, pokemonValues);

			// UsageTable
			for (int format = 0; format < Format.NUM_FORMATS; format++) {
				if (data.mUsage[format] != null) {
					ContentValues usageValues = new ContentValues();
					usageValues.put(UsageTable.COLUMN_NAME_ALIAS, data.mAlias);
					usageValues.put(UsageTable.COLUMN_NAME_FORMAT, format);
					usageValues.put(UsageTable.COLUMN_NAME_USAGE, data.mUsage[format]);

					db.insertOrThrow(UsageTable.TABLE_NAME, null, usageValues);
				}
			}

			// Moveset
			for (int format = 0; format < Format.NUM_FORMATS; format++) {
				MovesetData movesetData = data.mMovesetData[format];

				// BuildTable
				for (int i = 0; i < movesetData.mBuilds.size(); i++) {
					ContentValues values = new ContentValues();
					values.put(BuildTable.COLUMN_NAME_ALIAS, data.mAlias);
					values.put(BuildTable.COLUMN_NAME_FORMAT, format);
					values.put(BuildTable.COLUMN_NAME_NAME, movesetData.mBuilds.get(i).first);
					values.put(BuildTable.COLUMN_NAME_USAGE, movesetData.mBuilds.get(i).second);

					db.insertOrThrow(BuildTable.TABLE_NAME, null, values);
				}

				// AbilityTable
				for (int i = 0; i < movesetData.mAbilities.size(); i++) {
					ContentValues values = new ContentValues();
					values.put(AbilityTable.COLUMN_NAME_ALIAS, data.mAlias);
					values.put(AbilityTable.COLUMN_NAME_FORMAT, format);
					values.put(AbilityTable.COLUMN_NAME_NAME, movesetData.mAbilities.get(i).first);
					values.put(AbilityTable.COLUMN_NAME_USAGE, movesetData.mAbilities.get(i).second);

					db.insertOrThrow(AbilityTable.TABLE_NAME, null, values);
				}

				// MoveTable
				for (int i = 0; i < movesetData.mMoves.size(); i++) {
					ContentValues values = new ContentValues();
					values.put(MoveTable.COLUMN_NAME_ALIAS, data.mAlias);
					values.put(MoveTable.COLUMN_NAME_FORMAT, format);
					values.put(MoveTable.COLUMN_NAME_NAME, movesetData.mMoves.get(i).first);
					values.put(MoveTable.COLUMN_NAME_USAGE, movesetData.mMoves.get(i).second);

					db.insertOrThrow(MoveTable.TABLE_NAME, null, values);
				}

				// ItemTable
				for (int i = 0; i < movesetData.mItems.size(); i++) {
					ContentValues values = new ContentValues();
					values.put(ItemTable.COLUMN_NAME_ALIAS, data.mAlias);
					values.put(ItemTable.COLUMN_NAME_FORMAT, format);
					values.put(ItemTable.COLUMN_NAME_NAME, movesetData.mItems.get(i).first);
					values.put(ItemTable.COLUMN_NAME_USAGE, movesetData.mItems.get(i).second);

					db.insertOrThrow(ItemTable.TABLE_NAME, null, values);
				}

				// CounterTable
				for (int i = 0; i < movesetData.mCounters.size(); i++) {
					ContentValues values = new ContentValues();
					values.put(CounterTable.COLUMN_NAME_ALIAS, data.mAlias);
					values.put(CounterTable.COLUMN_NAME_FORMAT, format);
					values.put(CounterTable.COLUMN_NAME_NAME, movesetData.mCounters.get(i).first);
					values.put(CounterTable.COLUMN_NAME_USAGE, movesetData.mCounters.get(i).second);

					db.insert(CounterTable.TABLE_NAME, null, values);
				}
			}

			db.setTransactionSuccessful();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

}
