package com.example.arek.baking.recipeList;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.arek.baking.repository.RecipeRepository;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);
        ((BakingApp)getApplication()).getRepositoryComponent().inject(this);
        mAdapter = new RecipeAdapter();
        RecyclerView recycler = mBinding.recipeListRecyclerView;
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false);
        recycler.setLayoutManager(layout);
        recycler.setHasFixedSize(true);
        recycler.setAdapter(mAdapter);
        mPresenter = new RecipeListPresenter(mRecipeRepository);
        mPresenter.takeView(this);
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
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this, R.string.recipe_list_error_downloading, Toast.LENGTH_LONG);
    }
}
