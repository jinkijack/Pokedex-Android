package br.edu.ifsuldeminas.pokedex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    List<Pokemon> pokemonList = new ArrayList<>();
    List<Pokemon> filteredPokemonList = new ArrayList<>();
    RecyclerView recyclerView;
    PokemonAdapter pokemonAdapter;
    SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("POKEMON QUERY", "Searching for: ");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Chamado quando o usuário pressiona o botão de pesquisa no teclado
                searchPokemons(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Chamado quando o texto no SearchView muda (à medida que o usuário digita)
                searchPokemons(newText);
                return true;
            }
        });

        return true;
    }

    private void searchPokemons(String query) {
        filteredPokemonList.clear();
        for (Pokemon pokemon : pokemonList) {
            String pokemonName = pokemon.getName().toLowerCase();
            String pokemonId = String.valueOf(pokemon.getId());

            if (pokemonName.contains(query.toLowerCase()) || pokemonId.contains(query.toLowerCase())) {
                filteredPokemonList.add(pokemon);
            }
        }
        pokemonAdapter = new PokemonAdapter(filteredPokemonList);
        recyclerView.setAdapter(pokemonAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        sharedPreferences = getSharedPreferences("pokemon_cache", MODE_PRIVATE);


        recyclerView = findViewById(R.id.rv_pokemons);

        pokemonAdapter = new PokemonAdapter(pokemonList);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pokemonAdapter);
        //this.filteredPokemonList = this.pokemonList;

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra("ID", pokemonList.get(position).getId());
                i.putExtra("ID", filteredPokemonList.get(position).getId());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        addData();
    }

    private void addData() {
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