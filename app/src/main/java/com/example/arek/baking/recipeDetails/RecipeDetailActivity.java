package com.example.arek.baking.recipeDetails;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.arek.baking.R;
import com.example.arek.baking.databinding.ActivityRecipeDetailBinding;

public class RecipeDetailActivity extends AppCompatActivity {
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
        Intent intent = getIntent();
        if ( intent != null && intent.hasExtra(EXTRA_RECIPE_ID) ) {
            mRecipeId = intent.getLongExtra(EXTRA_RECIPE_ID,1);
        }

        RecipeDetailActivityFragment listFragment = RecipeDetailActivityFragment.newInstance(mRecipeId);
        getSupportFragmentManager().beginTransaction().replace(
                mBinding.content.fragmentDetailRecipe.getId(),
                listFragment
        ).commit();
    }

}
