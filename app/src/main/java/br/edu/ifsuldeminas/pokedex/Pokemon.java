package br.edu.ifsuldeminas.pokedex;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("height")
    private int height;
    @SerializedName("stats")
    private List<PokeStats> stats = new ArrayList<>();
    @SerializedName("types")
    private List<PokeType> types = new ArrayList<>();
    @SerializedName("sprites")
    private PokeSprite sprites;
    @SerializedName("weight")
    private int weight;

    public Pokemon(int id, String name, int height, List<PokeStats> stats, List<PokeType> types, PokeSprite sprites, int weight) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.stats = stats;
        this.types = types;
        this.sprites = sprites;
        this.weight = weight;
    }

    public Pokemon() {
    }

    public Pokemon(String nome, List<PokeType> types) {
        this.name = nome;
        this.types = types;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PokeStats> getStats() {
        return stats;
    }

    public void setStats(List<PokeStats> stats) {
        this.stats = stats;
    }

    public List<PokeType> getTypes() {
        return types;
    }

    public void setTypes(List<PokeType> types) {
        this.types = types;
    }
    public void setSprites(PokeSprite sprites) {
        this.sprites = sprites;
    }
    public PokeSprite getSprites() {
        return sprites;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
