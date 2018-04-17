package com.example.arek.baking.recipeDetails.recipeStep;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arek.baking.BakingApp;
import com.example.arek.baking.R;
import com.example.arek.baking.databinding.FragmentRecipeStepBinding;
import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.model.Step;
import com.example.arek.baking.recipeDetails.RecipeDetailActivity;
import com.example.arek.baking.repository.RecipeRepository;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeStepActivityFragment extends Fragment {
    private long mRecipeId;
    private long mRecipeStepId;
    private FragmentRecipeStepBinding mBinding;
    @Inject
    RecipeRepository mRepository;

    public RecipeStepActivityFragment() {
    }

    public static RecipeStepActivityFragment newInstance(long recipeId, long recipeStepId) {
        RecipeStepActivityFragment fragment = new RecipeStepActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(RecipeDetailActivity.EXTRA_RECIPE_ID, recipeId);
        bundle.putLong(RecipeStepActivity.EXTRA_RECIPE_STEP_ID, recipeStepId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BakingApp)getActivity().getApplication()).getRepositoryComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_recipe_step, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if ( bundle != null && hasArguments(bundle) ) {
            mRecipeId = bundle.getLong(RecipeDetailActivity.EXTRA_RECIPE_ID);
            mRecipeStepId = bundle.getLong(RecipeStepActivity.EXTRA_RECIPE_STEP_ID);
        }
        Timber.d("Recipe id: "+mRecipeId+" step: "+mRecipeStepId);
        getRecipeStep();
    }

    private boolean hasArguments(Bundle bundle) {
        return bundle.containsKey(RecipeDetailActivity.EXTRA_RECIPE_ID) &&
                bundle.containsKey(RecipeStepActivity.EXTRA_RECIPE_STEP_ID);
    }

    public void showRecipeStep(Step step) {
        mBinding.recipeStepDescription.setText(step.getDescription());
        mBinding.recipeStepShortDescription.setText(step.getShortDescription());
    }

    public void getRecipeStep() {
        mRepository.getRecipeStep(mRecipeId,mRecipeStepId)
        .subscribe(getDisposableObserver());
    }


    DisposableObserver<Step> getDisposableObserver() {
        return new DisposableObserver<Step>() {
            @Override
            public void onNext(Step step) {
                showRecipeStep(step);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

}
