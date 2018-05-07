package com.example.arek.baking.recipeDetails.recipeStep;

import com.example.arek.baking.model.Step;
import com.example.arek.baking.repository.RecipeRepository;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

/**
 * Created by Arkadiusz Wilczek on 24.04.18.
 */

public class RecipeStepPresenter implements RecipeStepContract.Presenter {
    private RecipeStepContract.View mView;
    private RecipeRepository mRecipeRepository;
    private DisposableObserver mDisposable;

    public RecipeStepPresenter(RecipeRepository repository) {
        mRecipeRepository = repository;
    }

    @Override
    public void takeView(RecipeStepContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        if ( mDisposable != null && !mDisposable.isDisposed() ) {
            mDisposable.dispose();
        }
        mView = null;
    }

    @Override
    public void getRecipeStep(long recipeId, long recipeStepId) {
        mDisposable = getDisposableObserver();
        Timber.d("Recipe id:" + recipeId + " step id :" + recipeStepId);
        mRecipeRepository.getRecipeStep(recipeId,recipeStepId)
                .subscribe(mDisposable);
    }

    DisposableObserver<Step> getDisposableObserver() {
        return new DisposableObserver<Step>() {
            @Override
            public void onNext(Step step) {
                if ( mView != null ) {
                    mView.showRecipeStep(step);
                    mView.showTitle(step.getShortDescription());
                }
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

}
