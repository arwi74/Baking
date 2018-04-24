package com.example.arek.baking.recipeDetails.recipeStep;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.arek.baking.BakingApp;
import com.example.arek.baking.R;
import com.example.arek.baking.databinding.ActivityRecipeStepBinding;
import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.model.Step;
import com.example.arek.baking.recipeDetails.RecipeDetailActivity;
import com.example.arek.baking.repository.RecipeRepository;
import com.example.arek.baking.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

public class RecipeStepActivity extends AppCompatActivity implements
        RecipeStepActivityFragment.RecipeStepActivityFragmentHandler {
    public static final String EXTRA_RECIPE_STEP_ID = "extra_recipe_step_id";
    public static final String STATE_RECIPE_ID = "state_recipe_id";
    public static final String STATE_RECIPE_STEP_ID = "state_recipe_step_id";
    private long mRecipeId;
    private long mRecipeStepId;
    private int mStepsListIndex;
    private DisposableObserver mDisposable;
    private ActivityRecipeStepBinding mBinding;
    private List<Step> mSteps;

    @Inject
    RecipeRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_step);
        Toolbar toolbar = mBinding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if ( intent == null || !intentHasExtraData(intent) ) finish();

        ((BakingApp)getApplication()).getRepositoryComponent().inject(this);
        mRecipeId = intent.getLongExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, 0);
        mRecipeStepId = intent.getLongExtra(EXTRA_RECIPE_STEP_ID, 0);
        if ( savedInstanceState == null ) {
            openStepFragment();
            openStepSelectFragment();
        } else {
            if ( savedInstanceState.containsKey(STATE_RECIPE_STEP_ID) ) {
                mRecipeId = savedInstanceState.getLong(STATE_RECIPE_ID);
                mRecipeStepId = savedInstanceState.getLong(STATE_RECIPE_STEP_ID);
            }
        }
        setButtonsListeners();
        getRecipe();
    }

    private void openStepSelectFragment() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(STATE_RECIPE_ID, mRecipeId);
        outState.putLong(STATE_RECIPE_STEP_ID, mRecipeStepId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private void openStepFragment() {
        RecipeStepActivityFragment fragment =
                RecipeStepActivityFragment.newInstance(mRecipeId, mRecipeStepId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_recipe_step, fragment)
                .commit();
    }

    private boolean intentHasExtraData(Intent intent) {
        return intent.hasExtra(EXTRA_RECIPE_STEP_ID)
                && intent.hasExtra(RecipeDetailActivity.EXTRA_RECIPE_ID);
    }

    private void getRecipe() {
        mDisposable = getDisposableObserver();
        mRepository
                .getRecipe(mRecipeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mDisposable);

    }

   private DisposableObserver<Recipe> getDisposableObserver() {
        return new DisposableObserver<Recipe>() {
            @Override
            public void onNext(Recipe recipe) {
                mSteps = recipe.getSteps();
                mStepsListIndex = Utils.findStepIndex(mSteps, mRecipeStepId);
                updateButtons();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
   }

    private void setEnabledNextButton(boolean enabled) {
        mBinding.content.recipeStepNextButton.setEnabled(enabled);
    }

    private void setEnabledPreviousButton(boolean enabled) {
        mBinding.content.recipeStepPreviousButton.setEnabled(enabled);
    }

    private void updateButtons() {
        setEnabledNextButton(mStepsListIndex < mSteps.size()-1);
        setEnabledPreviousButton(mStepsListIndex > 0);
    }

    private void setButtonsListeners() {
        mBinding.content.recipeStepNextButton.setOnClickListener(view -> {
           mStepsListIndex++;
           mRecipeStepId = mSteps.get(mStepsListIndex).getId();
           openStepFragment();
           updateButtons();
        });

        mBinding.content.recipeStepPreviousButton.setOnClickListener(view -> {
            mStepsListIndex--;
            mRecipeStepId = mSteps.get(mStepsListIndex).getId();
            openStepFragment();
            updateButtons();
        });
    }

    @Override
    public void setTitle(String title) {
        if ( getSupportActionBar() != null ) {
            getSupportActionBar().setTitle(title);
        }
    }
}
