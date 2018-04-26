package com.example.arek.baking.recipeDetails;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RemoteViews;

import com.example.arek.baking.R;
import com.example.arek.baking.databinding.ActivityRecipeDetailBinding;
import com.example.arek.baking.recipeDetails.recipeStep.RecipeStepActivity;
import com.example.arek.baking.service.BakingService;
import com.example.arek.baking.utils.Utils;
import com.example.arek.baking.widget.BakingWidget;

import timber.log.Timber;

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailActivityFragment.RecipeDetailActivityListener {
    ActivityRecipeDetailBinding mBinding;
    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";
    private long mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);
        Toolbar toolbar = mBinding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        if ( intent != null && intent.hasExtra(EXTRA_RECIPE_ID) ) {
            mRecipeId = intent.getLongExtra(EXTRA_RECIPE_ID,1);
        }
        if ( savedInstanceState!= null && savedInstanceState.containsKey(EXTRA_RECIPE_ID)) {
            mRecipeId = savedInstanceState.getLong(EXTRA_RECIPE_ID);
        }
        Utils.setRecipeForWidget(this, mRecipeId);
        BakingService.startUpdateWidgets(this);


        Timber.d("onCreate activity:" + this.toString());
      //  if ( savedInstanceState == null ) {
            openRecipeDetailFragment();
//            Timber.d("savedInstanceState null");
//        } else {
//            Timber.d("savedInstanceState not null");
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_RECIPE_ID, mRecipeId);
    }

    private void openRecipeDetailFragment() {
        Timber.d("open RecipeDetailFragment, recipe id: " + mRecipeId);
        RecipeDetailActivityFragment listFragment = RecipeDetailActivityFragment.newInstance(mRecipeId);
        getSupportFragmentManager().beginTransaction().replace(
                mBinding.content.fragmentDetailRecipe.getId(),
                listFragment
        ).commit();
    }


    @Override
    public void onIngredientsClick() {
        Timber.d("ingredients click");
    }

    @Override
    public void onRecipeStepClick(long recipeStepId) {
        Timber.d("recipe step click: "+recipeStepId);
        Intent intent = new Intent(this, RecipeStepActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, mRecipeId);
        intent.putExtra(RecipeStepActivity.EXTRA_RECIPE_STEP_ID, recipeStepId);
        startActivity(intent);
    }

    @Override
    public void onTitleSet(String title) {
        if ( getSupportActionBar() != null ) {
            getSupportActionBar().setTitle(title);
        }
    }


}
