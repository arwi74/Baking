package com.example.arek.baking.recipeDetails.recipeStep;

import com.example.arek.baking.BasePresenter;
import com.example.arek.baking.model.Step;

/**
 * Created by Arkadiusz Wilczek on 24.04.18.
 */

public interface RecipeStepContract {

    interface View {

        public void showRecipeStep(Step step);

        public void showErrorMessage();
    }

    interface Presenter extends BasePresenter<View> {

        public void getRecipeStep(long recipeId, long recipeStepId);
    }

}
