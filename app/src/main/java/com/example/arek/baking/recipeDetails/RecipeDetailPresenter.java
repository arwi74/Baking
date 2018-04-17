package com.example.arek.baking.recipeDetails;

import com.example.arek.baking.adapter.RecipeStepAdapter;
import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.repository.RecipeRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Arkadiusz Wilczek on 17.04.18.
 */

public class RecipeDetailPresenter implements
        RecipeDetailContract.Presenter,
        RecipeStepAdapter.RecipeStepListener {

    RecipeDetailContract.View mView;
    RecipeRepository mRepository;
    DisposableObserver<Recipe> mDisposable;

    public RecipeDetailPresenter(RecipeRepository repository) {
        mRepository = repository;
    }

    @Override
    public void takeView(RecipeDetailContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public void start(long recipeId) {
        getRecipeSteps(recipeId);
    }

    @Override
    public void onRecipeStepSelect(long stepId) {
        mView.openRecipeStep((int)stepId);
    }

    private void getRecipeSteps(long recipeId) {
        mRepository.getRecipe(recipeId)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(getDisposableObserver());

    }

    private DisposableObserver<Recipe> getDisposableObserver() {
        return new DisposableObserver<Recipe>() {
            @Override
            public void onNext(Recipe recipe) {
                Timber.d("recipe :"+recipe);
                mView.hideProgressBar();
                mView.showRecipeSteps(recipe.getSteps());
                mView.showRecipeIngredientsButton();
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e);
                mView.showErrorMessage();
            }

            @Override
            public void onComplete() {

            }
        };
    }

}