package com.example.arek.baking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arek.baking.R;
import com.example.arek.baking.model.Step;

import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 16.04.18.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeViewHolder> {
    private List<Step> mRecipeSteps;
    RecipeStepListener mRecipeStepListener;

    public interface RecipeStepListener {
        void onRecipeStepSelect(long stepId);
    }

    public RecipeStepAdapter(RecipeStepListener stepListener) {
        mRecipeStepListener = stepListener;
    }

    public void swapRecipeStep(List<Step> recipeSteps) {
        mRecipeSteps = recipeSteps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_step_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Step step = mRecipeSteps.get(position);
        holder.recipeStepName.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if ( mRecipeSteps == null ) return 0;
        else return mRecipeSteps.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        TextView recipeStepName;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeStepName = itemView.findViewById(R.id.recipe_step_item_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( mRecipeStepListener == null ) return;
                    mRecipeStepListener.onRecipeStepSelect(mRecipeSteps.get(getAdapterPosition()).getId());
                }
            });
        }
    }
}
