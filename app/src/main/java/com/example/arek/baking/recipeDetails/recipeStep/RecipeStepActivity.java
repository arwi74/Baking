package com.example.arek.baking.recipeDetails.recipeStep;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.arek.baking.R;
import com.example.arek.baking.recipeDetails.RecipeDetailActivity;

public class RecipeStepActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE_STEP_ID = "extra_recipe_step_id";
    private long mRecipeId;
    private long mRecipeStepId;
    private long mTotalSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if ( intent == null || !intentHasExtraData(intent) ) finish();

        mRecipeId = intent.getLongExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, 0);
        mRecipeStepId = intent.getLongExtra(EXTRA_RECIPE_STEP_ID, 0);
        if ( savedInstanceState == null ) {
            openStepFragment();
            openStepSelectFragment();
        }
    }

    private void openStepSelectFragment() {
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

}
