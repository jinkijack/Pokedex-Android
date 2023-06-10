package br.edu.ifsuldeminas.pokedex;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PokemonApiService {

    private static final String API_URL = "https://pokeapi.co/api/v2/pokemon/";

    public interface Callback {
        void onPokemonLoaded(Pokemon pokemon);
        void onFailure(String errorMessage);
    }

    public static void fetchPokemon(String pokemonName, Callback callback) {
        new FetchPokemonTask(callback).execute(pokemonName);
    }

    private static class FetchPokemonTask extends AsyncTask<String, Void, String> {
        private Callback callback;

        public FetchPokemonTask(Callback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String pokemonName = params[0];
            String urlString = API_URL + pokemonName;

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                reader.close();
                inputStream.close();
                connection.disconnect();

                return stringBuilder.toString();
            } catch (IOException e) {
                Log.e("FetchPokemonTask", "Error fetching Pokemon: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String json) {
            if (json != null) {
                Gson gson = new Gson();
                Type pokemonType = new TypeToken<Pokemon>() {}.getType();
                Pokemon pokemon = gson.fromJson(json, pokemonType);
                callback.onPokemonLoaded(pokemon);
            } else {
                callback.onFailure("Failed to fetch Pokemon");
            }
        }
    }
}
