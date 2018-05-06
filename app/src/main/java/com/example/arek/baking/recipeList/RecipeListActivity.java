package com.example.arek.baking.recipeList;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.arek.baking.BakingApp;
import com.example.arek.baking.R;
import com.example.arek.baking.adapter.RecipeAdapter;
import com.example.arek.baking.databinding.ActivityRecipeListBinding;
import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.recipeDetails.RecipeDetailActivity;
import com.example.arek.baking.repository.RecipeRepository;
import com.example.arek.baking.test.SimpleIdlingResource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public class RecipeListActivity extends AppCompatActivity implements RecipeListContract.View{
    @Inject
    RecipeRepository mRecipeRepository;
    RecipeAdapter mAdapter;
    private ActivityRecipeListBinding mBinding;
    private RecipeListContract.Presenter mPresenter;
    @Nullable
    private SimpleIdlingResource mSimpleIdlingresource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);
        ((BakingApp)getApplication()).getRepositoryComponent().inject(this);

        getIdlingResource();
        mSimpleIdlingresource.setIdleState(false);

        mPresenter = new RecipeListPresenter(mRecipeRepository);
        mAdapter = new RecipeAdapter((RecipeAdapter.RecipeAdapterListener)mPresenter, this);
        setRecyclerView();
        mPresenter.takeView(this);
        mPresenter.getRecipes();
    }

    private void setRecyclerView() {
        int spanCount = 1;
        if (isTwoPane()) spanCount = 3;

        RecyclerView recycler = mBinding.recipeListRecyclerView;
        recycler.setLayoutManager(new GridLayoutManager(this, spanCount));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

    @Override
    public void showProgressBar() {
        mBinding.recipeListProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mBinding.recipeListProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        mAdapter.swapRecipes(recipes);
        mSimpleIdlingresource.setIdleState(true);
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this, R.string.recipe_list_error_downloading, Toast.LENGTH_LONG);
    }

    @Override
    public void openRecipe(long recipeId) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID,recipeId);
        startActivity(intent);
    }

    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        if ( mSimpleIdlingresource != null ) return mSimpleIdlingresource;
        mSimpleIdlingresource = new SimpleIdlingResource();
        return mSimpleIdlingresource;
    }

    @VisibleForTesting
    public boolean isTwoPane() {
        return getResources().getBoolean(R.bool.two_pane);
    }
}
