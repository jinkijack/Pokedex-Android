package br.edu.ifsuldeminas.pokedex;

import com.google.gson.annotations.SerializedName;

public class PokeType {

    @SerializedName("slot")
    private int slot;
    @SerializedName("type")
    private PokeTypeDetails type;

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public PokeTypeDetails getType() {
        return type;
    }

    public void setType(PokeTypeDetails type) {
        this.type = type;
    }

}