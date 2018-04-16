package com.example.arek.baking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.arek.baking.adapter.RecipeAdapter;
import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public class RecipeListActivity extends AppCompatActivity {
    @Inject
    RecipeRepository mRecipeRepository;
    RecipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BakingApp)getApplication()).getRepositoryComponent().inject(this);
        setContentView(R.layout.activity_recipe_list);

        mRecipeRepository.getRecipes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDisposableObserver());
        mAdapter = new RecipeAdapter();
        RecyclerView recycler = (RecyclerView)findViewById(R.id.recipe_list_recycler_view);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false);
        recycler.setLayoutManager(layout);
        recycler.setHasFixedSize(true);
        recycler.setAdapter(mAdapter);
    }

    DisposableObserver<List<Recipe>> getDisposableObserver(){
        return new DisposableObserver<List<Recipe>>() {
            @Override
            public void onNext(List<Recipe> recipeList) {
                mAdapter.swapRecipes(recipeList);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("***", e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
