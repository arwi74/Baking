package com.example.arek.baking.recipeDetails;

import android.support.v7.view.menu.BaseMenuPresenter;

import com.example.arek.baking.BasePresenter;
import com.example.arek.baking.model.Ingredient;
import com.example.arek.baking.model.Step;

import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 17.04.18.
 */

public interface RecipeDetailContract {

    interface View {

        void showRecipeSteps(List<Step> steps);

        void showIngredients(List<Ingredient> ingredients);

        void showProgressBar();

        void hideProgressBar();

        void openRecipeStep(int  recipeStepId);

        void showErrorMessage();

        void showTitle(String title);
    }

    interface Presenter extends BasePresenter<View> {

        void start(long recipeId);
    }
}
