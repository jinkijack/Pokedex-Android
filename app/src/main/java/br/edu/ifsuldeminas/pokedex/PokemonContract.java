package br.edu.ifsuldeminas.pokedex;

import android.provider.BaseColumns;

public final class PokemonContract {

    private PokemonContract() {}

    public static class PokemonEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_POKEMON_ID = "pokemon_id";
    }
}
