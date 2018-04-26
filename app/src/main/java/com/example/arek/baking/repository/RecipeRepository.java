package com.example.arek.baking.repository;

import com.example.arek.baking.api.BakingApi;
import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.model.Step;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Arkadiusz Wilczek on 14.04.18.
 */

public class RecipeRepository {
    BakingApi mBakingApi;
    List<Recipe> mRecipes = new ArrayList<>();

    public RecipeRepository(BakingApi bakingApi) {
        mBakingApi = bakingApi;
    }

    public synchronized Observable<List<Recipe>> getRecipes() {
        Timber.d("getRecipes");
        if ( !mRecipes.isEmpty() ) {
            Timber.d("getRecipes from mRecipe");
            return Observable.just(mRecipes);
        } else {
            Timber.d("getRecipes from network");
            return mBakingApi.getRecipes()
                    .subscribeOn(Schedulers.io())
                    .map(recipes -> {
                                if ( mRecipes.isEmpty() ) {
                                    mRecipes.addAll(recipes);
                                }
                                return recipes;
                    });
        }
    }

    public Observable<Recipe> getRecipe(long recipeId) {
        return getRecipes()
                .map(recipes -> filterRecipe(recipes, recipeId));
    }

    private Recipe filterRecipe(List<Recipe> recipes, long recipeId) {
        for (Recipe recipe: recipes) {
            if ( recipe.getId() == recipeId ) {
                return recipe;
            }
        }
        return null;
    }

    public Observable<Step> getRecipeStep(long recipeId, long recipeStepId) {
        return getRecipe(recipeId)
                .map(recipe -> filterRecipeStep(recipe,recipeStepId));
    }

    private Step filterRecipeStep(Recipe recipe, long stepId) {
        for (Step step: recipe.getSteps()) {
            if ( step.getId() == stepId )
                return step;
        }
        return null;
    }

    public Observable<Long> getStepsSize(long recipeId) {
        return getRecipe(recipeId)
                .map(recipe -> (long)recipe.getSteps().size());
    }

}
