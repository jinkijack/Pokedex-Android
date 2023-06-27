package br.edu.ifsuldeminas.pokedex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PokemonDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pokemon.db";
    private static final int DATABASE_VERSION = 1;

    public PokemonDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crie a tabela de favoritos
        String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + PokemonContract.PokemonEntry.TABLE_NAME + " ("
                + PokemonContract.PokemonEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PokemonContract.PokemonEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + PokemonContract.PokemonEntry.COLUMN_POKEMON_ID + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Atualizações do esquema do banco de dados, se necessário
        // Implemente este método de acordo com suas necessidades
    }
}