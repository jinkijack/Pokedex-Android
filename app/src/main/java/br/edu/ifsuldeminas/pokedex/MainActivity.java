package br.edu.ifsuldeminas.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    List<Pokemon> pokemonList = new ArrayList<>();
    RecyclerView recyclerView;
    PokemonAdapter pokemonAdapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("pokemon_cache", MODE_PRIVATE);


        recyclerView = findViewById(R.id.rv_pokemons);

        pokemonAdapter = new PokemonAdapter(pokemonList);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pokemonAdapter);

        //addData();
        addDataTeste();
    }
    private void addData() {
        for(int i = 1; i <= 151; i++) {
            PokemonApiService.fetchPokemon(String.valueOf(i), new PokemonApiService.Callback() {
                @Override
                public void onPokemonLoaded(Pokemon pokemon) {
                    pokemonList.add(pokemon);
                    pokemonAdapter.notifyDataSetChanged();

                    Log.i("SPRITE", pokemon.getSprites().getFront_default());
                    Log.i("POKEMON", "Name: " + pokemon.getName());
                    Log.i("POKEMON", pokemon.getStats().get(0).getStat().getName() + pokemon.getStats().get(0).getBaseStat());
                    Log.i("POKEMON", pokemon.getStats().get(1).getStat().getName() + pokemon.getStats().get(1).getBaseStat());
                    Log.i("POKEMON", pokemon.getStats().get(2).getStat().getName() + pokemon.getStats().get(2).getBaseStat());
                    Log.i("POKEMON", pokemon.getStats().get(3).getStat().getName() + pokemon.getStats().get(3).getBaseStat());
                    Log.i("POKEMON", pokemon.getStats().get(4).getStat().getName() + pokemon.getStats().get(4).getBaseStat());
                    Log.i("POKEMON", pokemon.getStats().get(5).getStat().getName() + pokemon.getStats().get(5).getBaseStat());
                    Log.i("POKEMON", "Height: " + pokemon.getHeight());
                    Log.i("POKEMON", "Weight: " + pokemon.getWeight());
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("MainActivity", "Failed to fetch Pokemon: " + errorMessage);
                }
            });
        }
    }

    private void addDataTeste() {
        for (int i = 1; i <= 151; i++) {
            //String pokemonJson ="";
            String pokemonJson = sharedPreferences.getString("pokemon_" + i, "");
            if (!pokemonJson.isEmpty()) {
                // Converter o JSON de volta para o objeto Pokemon
                Gson gson = new Gson();
                Pokemon pokemon = gson.fromJson(pokemonJson, Pokemon.class);

                pokemonList.add(pokemon);
                pokemonAdapter.notifyDataSetChanged();

                Log.i("["+pokemon.getId()+"]"+"-SPRITE CACHE", pokemon.getSprites().getFront_default());
                Log.i("["+pokemon.getId()+"]"+"-POKEMON CACHE", "Name: " + pokemon.getName());
                Log.i("["+pokemon.getId()+"]"+"-POKEMON CACHE", pokemon.getStats().get(0).getStat().getName() + pokemon.getStats().get(0).getBaseStat());
                Log.i("["+pokemon.getId()+"]"+"-POKEMON CACHE", pokemon.getStats().get(1).getStat().getName() + pokemon.getStats().get(1).getBaseStat());
                Log.i("["+pokemon.getId()+"]"+"-POKEMON CACHE", pokemon.getStats().get(2).getStat().getName() + pokemon.getStats().get(2).getBaseStat());
                Log.i("["+pokemon.getId()+"]"+"-POKEMON CACHE", pokemon.getStats().get(3).getStat().getName() + pokemon.getStats().get(3).getBaseStat());
                Log.i("["+pokemon.getId()+"]"+"-POKEMON CACHE", pokemon.getStats().get(4).getStat().getName() + pokemon.getStats().get(4).getBaseStat());
                Log.i("["+pokemon.getId()+"]"+"-POKEMON CACHE", pokemon.getStats().get(5).getStat().getName() + pokemon.getStats().get(5).getBaseStat());
                Log.i("["+pokemon.getId()+"]"+"-POKEMON CACHE", "Height: " + pokemon.getHeight());
                Log.i("["+pokemon.getId()+"]"+"-POKEMON CACHE", "Weight: " + pokemon.getWeight());
            } else {
                // Chamar a API para obter os dados do Pokémon
                PokemonApiService.fetchPokemon(String.valueOf(i), new PokemonApiService.Callback() {
                    @Override
                    public void onPokemonLoaded(Pokemon pokemon) {
                        savePokemonToCache(pokemon);
                        pokemonList.add(pokemon);
                        pokemonAdapter.notifyDataSetChanged();

                        Log.i("["+pokemon.getId()+"]"+"-SPRITE", pokemon.getSprites().getFront_default());
                        Log.i("["+pokemon.getId()+"]"+"-POKEMON", "Name: " + pokemon.getName());
                        Log.i("["+pokemon.getId()+"]"+"-POKEMON", pokemon.getStats().get(0).getStat().getName() + pokemon.getStats().get(0).getBaseStat());
                        Log.i("["+pokemon.getId()+"]"+"-POKEMON", pokemon.getStats().get(1).getStat().getName() + pokemon.getStats().get(1).getBaseStat());
                        Log.i("["+pokemon.getId()+"]"+"-POKEMON", pokemon.getStats().get(2).getStat().getName() + pokemon.getStats().get(2).getBaseStat());
                        Log.i("["+pokemon.getId()+"]"+"-POKEMON", pokemon.getStats().get(3).getStat().getName() + pokemon.getStats().get(3).getBaseStat());
                        Log.i("["+pokemon.getId()+"]"+"-POKEMON", pokemon.getStats().get(4).getStat().getName() + pokemon.getStats().get(4).getBaseStat());
                        Log.i("["+pokemon.getId()+"]"+"-POKEMON", pokemon.getStats().get(5).getStat().getName() + pokemon.getStats().get(5).getBaseStat());
                        Log.i("["+pokemon.getId()+"]"+"-POKEMON", "Height: " + pokemon.getHeight());
                        Log.i("["+pokemon.getId()+"]"+"-POKEMON", "Weight: " + pokemon.getWeight());
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e("MainActivity", "Failed to fetch Pokemon: " + errorMessage);
                    }
                });
            }
        }
    }
    private void savePokemonToCache(Pokemon pokemon) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String pokemonJson = gson.toJson(pokemon);
        editor.putString("pokemon_" + pokemon.getId(), pokemonJson);
        editor.apply();
    }
}