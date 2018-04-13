package com.example.arek.baking.model;

import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 13.04.18.
 */

public class ApiResult {
    private List<Recipe> mRecipes;

    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.mRecipes = recipes;
    }

}
