package br.edu.ifsuldeminas.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    private TextView tvName, tvTypes, tvAttack, tvDefense, tvSpeed;
    private ImageView ivPokemon;
    private SharedPreferences sharedPreferences;

    Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        sharedPreferences = getSharedPreferences("pokemon_cache", MODE_PRIVATE);

        tvName = (TextView) findViewById(R.id.tv_detail_name);
        tvTypes = (TextView) findViewById(R.id.tv_detail_types);
        tvAttack = (TextView) findViewById(R.id.tv_detail_attack);
        tvDefense = (TextView) findViewById(R.id.tv_detail_defense);
        tvSpeed = (TextView) findViewById(R.id.tv_detail_speed);
        ivPokemon = (ImageView) findViewById(R.id.iv_detail_pokemon);

        Toolbar toolbar = findViewById(R.id.toolbar);

        // Configurar a Toolbar como a ActionBar da Activity
        setSupportActionBar(toolbar);

        int id = getIntent().getIntExtra("ID", 0);
        requestData(id);

        Intent i = getIntent();
        Toast.makeText(DetailActivity.this, i.getIntExtra("ID", 0) + "", Toast.LENGTH_SHORT).show();

        Button btnFavoritos = findViewById(R.id.btnFavoritos);
        btnFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarPokemonFavorito();
            }
        });


    }

    private void requestData(int id) {

        String pokemonJson = sharedPreferences.getString("pokemon_" + id, "");
        // Converter o JSON de volta para o objeto Pokemon
        Gson gson = new Gson();
        Pokemon pokemon = gson.fromJson(pokemonJson, Pokemon.class);

        this.pokemon = pokemon;
        tvName.setText(pokemon.getName());

        List<PokeType> types = pokemon.getTypes();
        StringBuilder typesBuilder = new StringBuilder();
        for (int i = 0; i < types.size(); i++) {
            PokeTypeDetails typeDetails = types.get(i).getType();
            typesBuilder.append(typeDetails.getName());

            if (i < types.size() - 1) {
                typesBuilder.append(", ");
            }
        }
        tvTypes.setText(typesBuilder.toString());

        StringBuilder tvAttackString = new StringBuilder();
        tvAttackString.append(pokemon.getStats().get(0).getStat().getName());
        tvAttackString.append(": ");
        tvAttackString.append(pokemon.getStats().get(0).getBaseStat());
        tvAttack.setText(tvAttackString);

        StringBuilder tvDefenseString = new StringBuilder();
        tvDefenseString.append(pokemon.getStats().get(1).getStat().getName());
        tvDefenseString.append(": ");
        tvDefenseString.append(pokemon.getStats().get(1).getBaseStat());
        tvDefense.setText(tvDefenseString);

        StringBuilder tvSpeedString = new StringBuilder();
        tvSpeedString.append(pokemon.getStats().get(5).getStat().getName());
        tvSpeedString.append(": ");
        tvSpeedString.append(pokemon.getStats().get(5).getBaseStat());
        tvSpeed.setText(tvSpeedString);

        String sprite = pokemon.getSprites().getFront_default();
        Picasso.with(ivPokemon.getContext())
                .load(sprite)
                .resize(128,128)
                .into(ivPokemon);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_back) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void adicionarPokemonFavorito() {
        // Crie uma instância do helper do banco de dados
        PokemonDBHelper dbHelper = new PokemonDBHelper(this);

        // Obtenha uma referência ao banco de dados em modo de escrita
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Verifique se o Pokémon já existe na tabela de favoritos
        if (isPokemonFavorite(db, pokemon.getId())) {
            Toast.makeText(this, "Este Pokémon já foi adicionado aos favoritos.", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        // Crie um objeto ContentValues para armazenar os valores a serem inseridos
        ContentValues values = new ContentValues();
        values.put(PokemonContract.PokemonEntry.COLUMN_NAME, pokemon.getName());
        values.put(PokemonContract.PokemonEntry.COLUMN_POKEMON_ID, pokemon.getId());
        // Adicione os outros campos do Pokémon à ContentValues, se necessário

        // Insira os valores na tabela de favoritos
        long newRowId = db.insert(PokemonContract.PokemonEntry.TABLE_NAME, null, values);

        // Verifique se a inserção foi bem-sucedida
        if (newRowId != -1) {
            Toast.makeText(this, "Pokémon adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao adicionar Pokémon aos favoritos.", Toast.LENGTH_SHORT).show();
        }

        // Feche a conexão com o banco de dados
        db.close();
    }

    private boolean isPokemonFavorite(SQLiteDatabase db, int pokemonId) {
        String[] projection = {PokemonContract.PokemonEntry._ID};
        String selection = PokemonContract.PokemonEntry.COLUMN_POKEMON_ID + " = ?";
        String[] selectionArgs = {String.valueOf(pokemonId)};
        String sortOrder = null;

        Cursor cursor = db.query(
                PokemonContract.PokemonEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        boolean isFavorite = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        return isFavorite;
    }



}