package com.example.arek.baking.repository;

import com.example.arek.baking.api.BakingApi;
import com.example.arek.baking.model.Recipe;

import java.util.List;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Arkadiusz Wilczek on 14.04.18.
 */

public class RecipeRepository {
    BakingApi mBakingApi;

    public RecipeRepository(BakingApi bakingApi) {
        mBakingApi = bakingApi;
    }

    public Observable<List<Recipe>> getRecipes() {
        return mBakingApi.getRecipes()
                .subscribeOn(Schedulers.io());
    }
}
