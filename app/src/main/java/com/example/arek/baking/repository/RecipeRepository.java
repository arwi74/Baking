package com.example.arek.baking.repository;

import com.example.arek.baking.api.BakingApi;
import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.model.Step;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Arkadiusz Wilczek on 14.04.18.
 */

public class RecipeRepository {
    BakingApi mBakingApi;
    List<Recipe> mRecipes = new ArrayList<>();

    public RecipeRepository(BakingApi bakingApi) {
        mBakingApi = bakingApi;
    }

    public Observable<List<Recipe>> getRecipes() {
        if ( !mRecipes.isEmpty() ) {
            return Observable.just(mRecipes);
        } else {
            return mBakingApi.getRecipes()
                    .subscribeOn(Schedulers.io())
                    .map(recipes -> {
                                mRecipes.addAll(recipes);
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

}
