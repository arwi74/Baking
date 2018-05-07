package com.example.arek.baking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.arek.baking.R;
import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.utils.GlideApp;

import java.util.List;

import timber.log.Timber;

/**
 * Created by Arkadiusz Wilczek on 14.04.18.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> mRecipes;
    private RecipeAdapterListener mListener;
    private Context mContext;

    public interface RecipeAdapterListener {
        void onRecipeSelect(long recipeId);
    }

    public RecipeAdapter(RecipeAdapterListener listener, Context context) {
        mListener = listener;
        mContext = context;
    }

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
        holder.recipeServings.setText(String.valueOf(recipe.getServings()));

        GlideApp.with(mContext)
                .load(recipe.getImage())
                .placeholder(R.drawable.ic_cake_black_24dp)
                .into(holder.recipeImage);
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
        TextView recipeServings;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipe_item_image);
            recipeName = (TextView) itemView.findViewById(R.id.recipe_item_name_text_view);
            recipeServings = (TextView) itemView.findViewById(R.id.recipe_item_servings);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onRecipeSelect(mRecipes.get(getAdapterPosition()).getId());
                    }
                }
            });
        }
    }
}
