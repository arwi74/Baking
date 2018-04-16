package com.example.arek.baking.recipeList;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.repository.RecipeRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Arkadiusz Wilczek on 16.04.18.
 */

public class RecipeListPresenter implements RecipeListContract.Presenter {
    private RecipeRepository mRecipeRepository;
    RecipeListContract.View mView;

    public RecipeListPresenter(@NonNull RecipeRepository recipeRepository) {
        mRecipeRepository = recipeRepository;
    }

    @Override
    public void takeView(RecipeListContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public void getRecipes() {
        mRecipeRepository.getRecipes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDisposableObserver());
    }

    private DisposableObserver<List<Recipe>> getDisposableObserver(){
        return new DisposableObserver<List<Recipe>>() {
            @Override
            public void onNext(List<Recipe> recipeList) {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("***", e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
