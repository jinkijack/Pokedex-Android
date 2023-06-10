package br.edu.ifsuldeminas.pokedex;

import com.google.gson.annotations.SerializedName;

public class PokeSprite {
    @SerializedName("front_default")
    private String front_default;

    public PokeSprite(String front_default) {
        this.front_default = front_default;
    }

    public String getFront_default() {
        return front_default;
    }

    public void setFront_default(String front_default) {
        this.front_default = front_default;
    }

}
