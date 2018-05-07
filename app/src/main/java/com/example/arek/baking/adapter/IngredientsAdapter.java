package com.example.arek.baking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arek.baking.R;
import com.example.arek.baking.model.Ingredient;

import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 24.04.18.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder>{
    private List<Ingredient> mIngredients;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredients_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);
        holder.name.setText(ingredient.getIngredient());
        holder.quantity.setText(""+ingredient.getQuantity());
        holder.measure.setText(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
       return  mIngredients == null ? 0 : mIngredients.size();
    }

    public void swapData(List<Ingredient> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView quantity;
        TextView measure;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_ingredient_name);
            quantity = itemView.findViewById(R.id.item_ingredient_quantity);
            measure = itemView.findViewById(R.id.item_ingredient_measure);
        }
    }
}
