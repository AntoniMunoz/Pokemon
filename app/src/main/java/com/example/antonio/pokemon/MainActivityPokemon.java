package com.example.antonio.pokemon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.antonio.pokemon.models.Pokemon;
import com.example.antonio.pokemon.models.PokemonRespuesta;
import com.example.antonio.pokemon.pokeapi.ListaPokemonAdapter;
import com.example.antonio.pokemon.pokeapi.PokeapiService;

import java.lang.annotation.Target;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivityPokemon extends AppCompatActivity {
    private static final String TAG = "POKEDEX";
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ListaPokemonAdapter listaPokemonAdapter;
    private int offset;
    private boolean aptoParaCargar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pokemon);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listaPokemonAdapter = new ListaPokemonAdapter(this);
        recyclerView.setAdapter(listaPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0){
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int postVisibleItems= layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (aptoParaCargar)
                    {
                        if ((visibleItemCount+postVisibleItems)>=totalItemCount)
                        {
                            Log.i(TAG,"Llegamos al final...");
                            aptoParaCargar = false;
                            offset+=20;
                            obtenerDatos(offset);
                        }
                    }
                }

            }
        });

        retrofit = new Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/").addConverterFactory(GsonConverterFactory.create()).build();
        aptoParaCargar=true;
        offset=0;
        obtenerDatos(offset);

    }

    private void obtenerDatos(int offset) {
        PokeapiService service = retrofit.create(PokeapiService.class);
        Call<PokemonRespuesta> pokemonRespuestaCall=service.obtenerListaPokemon(20,offset);

        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                aptoParaCargar=true;
                if (response.isSuccessful())
                {
                    PokemonRespuesta pokemonRespuesta = response.body();
                    ArrayList<Pokemon> listaPokemon = pokemonRespuesta.getResults();

                    /*for(int i=0; i<listaPokemon.size(); i++)
                    {
                        Pokemon p = listaPokemon.get(i);
                        Log.i(TAG, "Pokemon    " + p.getName());
                    }*/
                    listaPokemonAdapter.adicionarListaPokemon(listaPokemon);

                }
                else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                aptoParaCargar=true;
                Log.e(TAG, "onFailure" + t.getMessage());
            }
        });

    }
}
