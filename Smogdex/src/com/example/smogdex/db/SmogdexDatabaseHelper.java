package com.example.smogdex.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.smogdex.db.SmogdexDatabaseContract.MovesetTable;
import com.example.smogdex.db.SmogdexDatabaseContract.PokemonTable;
import com.example.smogdex.db.SmogdexDatabaseContract.UsageTable;
import com.example.smogdex.models.PokemonData;
import com.example.smogdex.models.PokemonData.Format;
import com.example.smogdex.models.PokemonData.MovesetData;
import com.example.smogdex.models.PokemonData.StatsData;

public class SmogdexDatabaseHelper extends SQLiteOpenHelper {

	private static final String TAG = SmogdexDatabaseHelper.class.getSimpleName();

	public static final String DATABASE_NAME = "Smogdex.db";
	public static final int DATABASE_VERSION = 4;

	private static SmogdexDatabaseHelper mInstance;

	public static void initialize(Context context) {
		mInstance = new SmogdexDatabaseHelper(context);
	}

	public static SmogdexDatabaseHelper getInstance() {
		if (mInstance == null) {
			throw new IllegalStateException(SmogdexDatabaseHelper.class.getSimpleName() + " has not been initialized");
		}
		return mInstance;
	}

	private SmogdexDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(PokemonTable.CREATE);
		db.execSQL(UsageTable.CREATE);
		db.execSQL(MovesetTable.CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(MovesetTable.DROP);
		db.execSQL(UsageTable.DROP);
		db.execSQL(PokemonTable.DROP);
	}

	public static String makeSelection(String... params) {
		if (params.length == 0) {
			return null;
		}
		String EQUALS_ARG = "=?";
		String AND = " AND ";

		StringBuilder selection = new StringBuilder(params[0] + EQUALS_ARG);
		for (int i = 1; i < params.length; i++) {
			selection.append(AND).append(params[i]).append(EQUALS_ARG);
		}

		return selection.toString();
	}

	public void post(PokemonData data) {
		synchronized(this) {
			SQLiteDatabase db = getWritableDatabase();

			try {
				// VERY important - we're doing a huge batch insertion
				db.beginTransaction();

				// PokemonTable
				ContentValues pokemonValues = new ContentValues();
				pokemonValues.put(PokemonTable.COLUMN_NAME_ALIAS, data.mAlias);
				pokemonValues.put(PokemonTable.COLUMN_NAME_INIT, data.mStatsDataRetrieved ? 1 : 0);
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

					for (int i = 0; i < movesetData.mBuilds.size(); i++) {
						ContentValues values = new ContentValues();
						values.put(MovesetTable.COLUMN_NAME_ALIAS, data.mAlias);
						values.put(MovesetTable.COLUMN_NAME_FORMAT, format);
						values.put(MovesetTable.COLUMN_NAME_SECTION, MovesetTable.SECTION_BUILD);
						values.put(MovesetTable.COLUMN_NAME_NAME, movesetData.mBuilds.get(i).first);
						values.put(MovesetTable.COLUMN_NAME_USAGE, movesetData.mBuilds.get(i).second);

						db.insertOrThrow(MovesetTable.TABLE_NAME, null, values);
					}

					for (int i = 0; i < movesetData.mAbilities.size(); i++) {
						ContentValues values = new ContentValues();
						values.put(MovesetTable.COLUMN_NAME_ALIAS, data.mAlias);
						values.put(MovesetTable.COLUMN_NAME_FORMAT, format);
						values.put(MovesetTable.COLUMN_NAME_SECTION, MovesetTable.SECTION_ABILITY);
						values.put(MovesetTable.COLUMN_NAME_NAME, movesetData.mAbilities.get(i).first);
						values.put(MovesetTable.COLUMN_NAME_USAGE, movesetData.mAbilities.get(i).second);

						db.insertOrThrow(MovesetTable.TABLE_NAME, null, values);
					}

					for (int i = 0; i < movesetData.mMoves.size(); i++) {
						ContentValues values = new ContentValues();
						values.put(MovesetTable.COLUMN_NAME_ALIAS, data.mAlias);
						values.put(MovesetTable.COLUMN_NAME_FORMAT, format);
						values.put(MovesetTable.COLUMN_NAME_SECTION, MovesetTable.SECTION_MOVE);
						values.put(MovesetTable.COLUMN_NAME_NAME, movesetData.mMoves.get(i).first);
						values.put(MovesetTable.COLUMN_NAME_USAGE, movesetData.mMoves.get(i).second);

						db.insertOrThrow(MovesetTable.TABLE_NAME, null, values);
					}

					for (int i = 0; i < movesetData.mItems.size(); i++) {
						ContentValues values = new ContentValues();
						values.put(MovesetTable.COLUMN_NAME_ALIAS, data.mAlias);
						values.put(MovesetTable.COLUMN_NAME_FORMAT, format);
						values.put(MovesetTable.COLUMN_NAME_SECTION, MovesetTable.SECTION_ITEM);
						values.put(MovesetTable.COLUMN_NAME_NAME, movesetData.mItems.get(i).first);
						values.put(MovesetTable.COLUMN_NAME_USAGE, movesetData.mItems.get(i).second);

						db.insertOrThrow(MovesetTable.TABLE_NAME, null, values);
					}

					for (int i = 0; i < movesetData.mCounters.size(); i++) {
						ContentValues values = new ContentValues();
						values.put(MovesetTable.COLUMN_NAME_ALIAS, data.mAlias);
						values.put(MovesetTable.COLUMN_NAME_FORMAT, format);
						values.put(MovesetTable.COLUMN_NAME_SECTION, MovesetTable.SECTION_COUNTER);
						values.put(MovesetTable.COLUMN_NAME_NAME, movesetData.mCounters.get(i).first);
						values.put(MovesetTable.COLUMN_NAME_USAGE, movesetData.mCounters.get(i).second);

						db.insert(MovesetTable.TABLE_NAME, null, values);
					}
				}

				db.setTransactionSuccessful();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
			db.close();
		}
	}


	public PokemonData get(String alias) {
		PokemonData data = new PokemonData(alias);

		synchronized(this) {

			SQLiteDatabase db = getReadableDatabase();

			try {
				Cursor pokemonCursor = db.query(
						PokemonTable.TABLE_NAME,
						new String[] {
								PokemonTable.COLUMN_NAME_INIT,
								PokemonTable.COLUMN_NAME_HP,
								PokemonTable.COLUMN_NAME_ATK,
								PokemonTable.COLUMN_NAME_DEF,
								PokemonTable.COLUMN_NAME_SPA,
								PokemonTable.COLUMN_NAME_SPD,
								PokemonTable.COLUMN_NAME_SPE,
						},
						makeSelection(PokemonTable.COLUMN_NAME_ALIAS),
						new String[] {alias},
						null,
						null,
						null);
				boolean exists = pokemonCursor.moveToFirst();
				if (!exists) {
					return null;
				}
				StatsData stats = new StatsData();
				data.mStatsDataRetrieved = (pokemonCursor.getInt(0) == 1);
				stats.mHP = pokemonCursor.getInt(1);
				stats.mAtk = pokemonCursor.getInt(2);
				stats.mDef = pokemonCursor.getInt(3);
				stats.mSpA = pokemonCursor.getInt(4);
				stats.mSpD = pokemonCursor.getInt(5);
				stats.mSpe = pokemonCursor.getInt(6);
				pokemonCursor.close();
				data.mStatsData = stats;

				Cursor usageCursor = db.query(
						UsageTable.TABLE_NAME,
						new String[] {
								UsageTable.COLUMN_NAME_FORMAT,
								UsageTable.COLUMN_NAME_USAGE
						},
						makeSelection(UsageTable.COLUMN_NAME_ALIAS),
						new String[] {alias},
						null,
						null,
						null);
				usageCursor.moveToPosition(-1);
				while (usageCursor.moveToNext()) {
					data.mUsage[usageCursor.getInt(0)] = usageCursor.getString(1);
				}
				usageCursor.close();

				Cursor movesetCursor = db.query(
						MovesetTable.TABLE_NAME,
						new String[] {
								MovesetTable.COLUMN_NAME_FORMAT,
								MovesetTable.COLUMN_NAME_SECTION,
								MovesetTable.COLUMN_NAME_NAME,
								MovesetTable.COLUMN_NAME_USAGE
						},
						makeSelection(MovesetTable.COLUMN_NAME_ALIAS),
						new String[] {alias},
						null,
						null,
						MovesetTable.COLUMN_NAME_SECTION); // TODO: may need to sort by usage; see PokemonData.MovesetData
				movesetCursor.moveToPosition(-1);
				while (movesetCursor.moveToNext()) {
					int format = movesetCursor.getInt(0);
					int section = movesetCursor.getInt(1);
					String name = movesetCursor.getString(2);
					String usage = movesetCursor.getString(3);

					switch (section) {
					case MovesetTable.SECTION_BUILD:
						data.mMovesetData[format].addBuild(name, usage);
						break;
					case MovesetTable.SECTION_ABILITY:
						data.mMovesetData[format].addAbility(name, usage);
						break;
					case MovesetTable.SECTION_MOVE:
						data.mMovesetData[format].addMove(name, usage);
						break;
					case MovesetTable.SECTION_ITEM:
						data.mMovesetData[format].addItem(name, usage);
						break;
					case MovesetTable.SECTION_COUNTER:
						data.mMovesetData[format].addCounter(name, usage);
					default:
						break;
					}
				}

			} catch (Exception e) {
				// if anything goes wrong, just return null
				return null;
			}
			db.close();
		}

		return data;
	}


	public void put(PokemonData data) {
		if (!data.mStatsDataRetrieved) {
			Log.d(TAG, "something has gone horribly wrong");
			return;
		}

		SQLiteDatabase db = getWritableDatabase();

		try {
			// TODO: this should theoretically update everything, but for now
			// we only need to update stats
			db.beginTransaction();

			// PokemonTable
			ContentValues pokemonValues = new ContentValues();
			pokemonValues.put(PokemonTable.COLUMN_NAME_ALIAS, data.mAlias);
			pokemonValues.put(PokemonTable.COLUMN_NAME_INIT, 1);
			pokemonValues.put(PokemonTable.COLUMN_NAME_HP, data.mStatsData.mHP);
			pokemonValues.put(PokemonTable.COLUMN_NAME_ATK, data.mStatsData.mAtk);
			pokemonValues.put(PokemonTable.COLUMN_NAME_DEF, data.mStatsData.mDef);
			pokemonValues.put(PokemonTable.COLUMN_NAME_SPA, data.mStatsData.mSpA);
			pokemonValues.put(PokemonTable.COLUMN_NAME_SPD, data.mStatsData.mSpD);
			pokemonValues.put(PokemonTable.COLUMN_NAME_SPE, data.mStatsData.mSpe);

			db.replace(PokemonTable.TABLE_NAME, null, pokemonValues);

			db.setTransactionSuccessful();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		db.close();
	}


	public void delete(PokemonData data) {
		// pass
	}
}
