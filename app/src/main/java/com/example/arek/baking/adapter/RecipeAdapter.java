package com.example.arek.baking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arek.baking.R;
import com.example.arek.baking.model.Recipe;

import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 14.04.18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> mRecipes;

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        String name = recipe.getName();
        holder.recipeName.setText(name);
    }

    @Override
    public int getItemCount() {
        if ( mRecipes == null ) return 0;
        return mRecipes.size();
    }

    public void swapRecipes(List<Recipe> recipes){
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        ImageView recipeImage;
        TextView recipeName;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipe_item_image);
            recipeName = (TextView) itemView.findViewById(R.id.recipe_item_name_text_view);
        }
    }
}
