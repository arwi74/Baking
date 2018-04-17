package com.example.arek.baking.recipeDetails;

import android.support.v7.view.menu.BaseMenuPresenter;

import com.example.arek.baking.BasePresenter;
import com.example.arek.baking.model.Step;

import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 17.04.18.
 */

public interface RecipeDetailContract {

    interface View {

        void showRecipeSteps(List<Step> steps);

        void showProgressBar();

        void hideProgressBar();

        void openRecipeStep(int  recipeStepId);

        void showErrorMessage();
    }

    interface Presenter extends BasePresenter<View> {

        void start(long recipeId);
    }
}
