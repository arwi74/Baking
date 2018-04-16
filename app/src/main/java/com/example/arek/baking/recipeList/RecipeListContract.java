package com.example.arek.baking.recipeList;

import com.example.arek.baking.BasePresenter;
import com.example.arek.baking.model.Recipe;

import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 16.04.18.
 */

public interface RecipeListContract {

    interface View {

        void showProgressBar();

        void hideProgressBar();

        void showRecipes(List<Recipe> recipes);

        void showErrorMessage();
    }

    interface Presenter extends BasePresenter<RecipeListContract.View> {

        void getRecipes();

    }
}
