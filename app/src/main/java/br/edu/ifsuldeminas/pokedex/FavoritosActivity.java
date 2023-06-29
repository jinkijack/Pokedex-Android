package br.edu.ifsuldeminas.pokedex;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FavoritosActivity extends AppCompatActivity {

    List<Pokemon> pokemonList = new ArrayList<>();
    List<Pokemon> filteredPokemonList = new ArrayList<>();
    RecyclerView recyclerView;
    PokemonAdapter pokemonAdapter;
    PokemonDBHelper dbHelper;
    SharedPreferences sharedPreferences;

    private static final int REQUEST_CODE_DETAIL = 1;

    private ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadPokemonList();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        sharedPreferences = getSharedPreferences("pokemon_cache", MODE_PRIVATE);
        Log.i("FavoritosActivity", "onCreate: " + sharedPreferences.getString("pokemon_list", "[]"));
        recyclerView = findViewById(R.id.rv_pokemons);
        pokemonAdapter = new PokemonAdapter(pokemonList);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);

        // Remove a sincronização do estado do toggle
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        // Configurar o ícone de menu na action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Fecha o menu lateral ao iniciar a atividade
        drawerLayout.closeDrawer(GravityCompat.START);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pokemonAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(FavoritosActivity.this, DetailActivity.class);
                if (filteredPokemonList.isEmpty()) {
                    i.putExtra("ID", pokemonList.get(position).getId());
                } else {
                    i.putExtra("ID", filteredPokemonList.get(position).getId());
                }
                //startActivity(i);
                //startActivityForResult(i, REQUEST_CODE_DETAIL);
                detailLauncher.launch(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        dbHelper = new PokemonDBHelper(this);
        loadPokemonList();
    }
    private void loadPokemonList() {
        pokemonList.clear();
        filteredPokemonList.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                PokemonContract.PokemonEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(PokemonContract.PokemonEntry.COLUMN_POKEMON_ID));

                // Buscar o JSON do Pokémon no SharedPreferences
                String pokemonJson = sharedPreferences.getString("pokemon_" + id, "");
                if (!pokemonJson.isEmpty()) {
                    // Converter o JSON de volta para o objeto Pokemon
                    Gson gson = new Gson();
                    Pokemon pokemon = gson.fromJson(pokemonJson, Pokemon.class);
                    pokemonList.add(pokemon);
                } else {
                    // Chamar a API para obter os dados do Pokémon
                    PokemonApiService.fetchPokemon(String.valueOf(id), new PokemonApiService.Callback() {
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
                            Log.e("FavoritosActivity", "Failed to fetch Pokemon: " + errorMessage);
                        }
                    });
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        pokemonAdapter.notifyDataSetChanged();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void savePokemonToCache(Pokemon pokemon) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String pokemonJson = gson.toJson(pokemon);
        editor.putString("pokemon_" + pokemon.getId(), pokemonJson);
        editor.apply();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DETAIL && resultCode == RESULT_OK) {
            Log.i("FavoritosActivity", "Pokemon recarregado");
            loadPokemonList();
        }
    }

}