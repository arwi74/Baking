package com.example.arek.baking.recipeDetails.recipeStep;

import com.example.arek.baking.BasePresenter;
import com.example.arek.baking.model.Step;

/**
 * Created by Arkadiusz Wilczek on 24.04.18.
 */

public interface RecipeStepContract {

    interface View {

        void showRecipeStep(Step step);

        void showErrorMessage();

        void showTitle(String title);
    }

    interface Presenter extends BasePresenter<View> {

        void getRecipeStep(long recipeId, long recipeStepId);
    }

}
