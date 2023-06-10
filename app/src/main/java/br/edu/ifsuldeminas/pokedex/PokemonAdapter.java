package br.edu.ifsuldeminas.pokedex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokeViewHolder> {

    private List<Pokemon> pokemonList;

    public class PokeViewHolder extends RecyclerView.ViewHolder {
        public TextView name, type;

        public PokeViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            type = (TextView) itemView.findViewById(R.id.tv_type);
        }
    }

    public PokemonAdapter(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    @Override
    public PokeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.pokemon_row, parent, false);

        return new PokeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PokeViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.name.setText(pokemon.getName());

        StringBuilder typesBuilder = new StringBuilder();
        List<PokeType> types = pokemon.getTypes();

        for (int i = 0; i < types.size(); i++) {
            PokeTypeDetails typeDetails = types.get(i).getType();
            typesBuilder.append(typeDetails.getName());

            if (i < types.size() - 1) {
                typesBuilder.append(", ");
            }
        }

        holder.type.setText(typesBuilder.toString());
    }


    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
}
