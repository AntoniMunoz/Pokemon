package com.example.antonio.pokemon.models;

import java.util.ArrayList;

public class PokemonRespuesta {
    public ArrayList<Pokemon> getResults() {
        return results;
    }

    public void setResults(ArrayList<Pokemon> results) {
        this.results = results;
    }

    private ArrayList<Pokemon> results;
}
