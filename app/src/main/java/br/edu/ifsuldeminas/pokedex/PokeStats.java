package br.edu.ifsuldeminas.pokedex;

import com.google.gson.annotations.SerializedName;

public class PokeStats {
    @SerializedName("base_stat")
    private int base_stat;

    @SerializedName("stat")
    private PokeStatDetails stat;

    public int getBaseStat() {
        return base_stat;
    }

    public void setBaseStat(int base_stat) {
        this.base_stat = base_stat;
    }

    public PokeStatDetails getStat() {
        return stat;
    }

    public void setStat(PokeStatDetails stat) {
        this.stat = stat;
    }
}
