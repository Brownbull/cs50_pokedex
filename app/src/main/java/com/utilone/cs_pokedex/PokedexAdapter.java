package com.utilone.cs_pokedex;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> {
    public static class PokedexViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView textView;

        PokedexViewHolder(View view){
            super(view);
            containerView = view.findViewById(R.id.pokedex_row);
            textView = view.findViewById(R.id.pokedex_row_textview);

            containerView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Pokemon current = (Pokemon) containerView.getTag();
                    Intent intent = new Intent(v.getContext(), PokemonActivity.class);
                    intent.putExtra("name", current.getName());
//                    intent.putExtra("number", current.getNumber());

                    v.getContext().startActivity(intent);
                }
            });
        } // eof PokedexViewHolder
    } // eof PokedexViewHolder

    private List<Pokemon> pokemon = Arrays.asList();
    private RequestQueue requestQueue;

    PokedexAdapter(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        loadPokemon();
    }

    public void loadPokemon() {
        String url = "https://pokeapi.co/api/v2/pokemon?limit=151";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");

                    int i;
                    for (i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        pokemon.add(new Pokemon(
                                result.getString("name"),
                                result.getString("url")
                        ));
                    }
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("loadPokemon", "Json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("loadPokemon", "Error Listener");
            }
        });

        requestQueue.add(request);
    } // eof loadPokemon

    @NonNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokedex_row, parent, false);

        return new PokedexViewHolder(view);
    } // eof onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
        Pokemon current = pokemon.get(position);
        holder.textView.setText(current.getName());
        holder.containerView.setTag(current);

    } // eof onBindViewHolder

    @Override
    public int getItemCount() {
        return pokemon.size();
    } // eof getItemCount
} // eof PokedexAdapter
