package br.edu.ifsuldeminas.pokedex;

import com.google.gson.annotations.SerializedName;

public class PokeStatDetails {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
