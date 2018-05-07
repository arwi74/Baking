package com.example.arek.baking.recipeList;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.example.arek.baking.adapter.RecipeAdapter;
import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.repository.RecipeRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

/**
 * Created by Arkadiusz Wilczek on 16.04.18.
 */

public class RecipeListPresenter implements RecipeListContract.Presenter,
        RecipeAdapter.RecipeAdapterListener {
    private RecipeRepository mRecipeRepository;
    private RecipeListContract.View mView;
    private DisposableObserver mDisposable;

    public RecipeListPresenter(@NonNull RecipeRepository recipeRepository) {
        mRecipeRepository = recipeRepository;
    }

    @Override
    public void takeView(RecipeListContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        if ( mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mView = null;
    }

    @Override
    public void getRecipes() {
        mDisposable = getDisposableObserver();
        mView.showProgressBar();
        mRecipeRepository.getRecipes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mDisposable);
    }

    private DisposableObserver<List<Recipe>> getDisposableObserver(){
        return new DisposableObserver<List<Recipe>>() {
            @Override
            public void onNext(List<Recipe> recipeList) {
                mView.hideProgressBar();
                mView.showRecipes(recipeList);
            }

            @Override
            public void onError(Throwable e) {
                mView.showErrorMessage();
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public void onRecipeSelect(long recipeId) {
        mView.openRecipe(recipeId);
    }
}
