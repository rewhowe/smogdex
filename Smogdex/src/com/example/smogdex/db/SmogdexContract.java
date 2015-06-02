package com.example.smogdex.db;

import android.provider.BaseColumns;

/**
 * Defines the database schema. If this is modified, you MUST update SmogdexDBHelper.DATABASE_VERSION.
 * @author rewhowe
 *
 */
public final class SmogdexContract {

	public static final String CREATE_TABLE = "CREATE TABLE ";
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

	public static final String PRIMARY_KEY = " PRIMARY KEY";
	public static final String FOREIGN_KEY = " FOREIGN KEY";
	public static final String REFERENCES = " REFERENCES ";
	public static final String AUTO_INCREMENT = " AUTOINCREMENT";

	public static final String DATA_TYPE_TEXT = " TEXT";
	public static final String DATA_TYPE_INT = " INTEGER";
	public static final String NOT_NULL = " NOT NULL";

	public static final String BRACKET_OPEN = " (";
	public static final String BRACKET_CLOSE = ")";
	public static final String COMMA_SEP = ", ";

	public static abstract class PokemonTable implements BaseColumns {
		public static final String TABLE_NAME = "Pokemon";
		public static final String COLUMN_NAME_ALIAS = "alias";
		public static final String COLUMN_NAME_HP = "hp";
		public static final String COLUMN_NAME_ATK = "atk";
		public static final String COLUMN_NAME_DEF = "def";
		public static final String COLUMN_NAME_SPA = "spa";
		public static final String COLUMN_NAME_SPD = "spd";
		public static final String COLUMN_NAME_SPE = "spe";

		public static final String CREATE =
				CREATE_TABLE + TABLE_NAME + BRACKET_OPEN
				+ COLUMN_NAME_ALIAS + DATA_TYPE_TEXT + PRIMARY_KEY + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_HP  + DATA_TYPE_INT + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_ATK + DATA_TYPE_INT + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_DEF + DATA_TYPE_INT + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_SPA + DATA_TYPE_INT + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_SPD + DATA_TYPE_INT + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_SPE + DATA_TYPE_INT + NOT_NULL + BRACKET_CLOSE;

		public static final String DROP =
				DROP_TABLE + TABLE_NAME;
	}

	public static abstract class UsageTable implements BaseColumns {
		public static final String TABLE_NAME = "Usage";
		public static final String COLUMN_NAME_ALIAS = "alias";
		public static final String COLUMN_NAME_FORMAT = "format";
		public static final String COLUMN_NAME_USAGE = "usage";

		public static final String CREATE =
				CREATE_TABLE + TABLE_NAME + BRACKET_OPEN
				+ COLUMN_NAME_ALIAS + DATA_TYPE_TEXT + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_FORMAT + DATA_TYPE_INT + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_USAGE + DATA_TYPE_TEXT + NOT_NULL + COMMA_SEP
				+ PRIMARY_KEY + BRACKET_OPEN + COLUMN_NAME_ALIAS + COMMA_SEP + COLUMN_NAME_FORMAT + BRACKET_CLOSE + COMMA_SEP
				+ FOREIGN_KEY + BRACKET_OPEN + COLUMN_NAME_ALIAS + BRACKET_CLOSE
				+ REFERENCES + PokemonTable.TABLE_NAME + BRACKET_OPEN + PokemonTable.COLUMN_NAME_ALIAS + BRACKET_CLOSE + BRACKET_CLOSE;

		public static final String DROP =
				DROP_TABLE + TABLE_NAME;
	}

	public static abstract class MovesetTable implements BaseColumns {
		public static final String TABLE_NAME = "Moveset";
		public static final String COLUMN_NAME_ALIAS = "alias";
		public static final String COLUMN_NAME_FORMAT = "format";
		public static final String COLUMN_NAME_SECTION = "section";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_USAGE = "usage";

		public static final int SECTION_BUILD = 0;
		public static final int SECTION_ABILITY = 1;
		public static final int SECTION_MOVE = 2;
		public static final int SECTION_ITEM = 3;
		public static final int SECTION_COUNTER = 4;

		public static final String CREATE =
				CREATE_TABLE + TABLE_NAME + BRACKET_OPEN
				+ _ID + DATA_TYPE_INT + PRIMARY_KEY + AUTO_INCREMENT + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_ALIAS + DATA_TYPE_TEXT + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_FORMAT + DATA_TYPE_INT + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_SECTION + DATA_TYPE_INT + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_NAME + DATA_TYPE_TEXT + NOT_NULL + COMMA_SEP
				+ COLUMN_NAME_USAGE + DATA_TYPE_TEXT + NOT_NULL + COMMA_SEP
				+ FOREIGN_KEY + BRACKET_OPEN + COLUMN_NAME_ALIAS + BRACKET_CLOSE
				+ REFERENCES + PokemonTable.TABLE_NAME + BRACKET_OPEN + PokemonTable.COLUMN_NAME_ALIAS + BRACKET_CLOSE + BRACKET_CLOSE;

		public static final String DROP =
				DROP_TABLE + TABLE_NAME;
	}

	private SmogdexContract() {}
}