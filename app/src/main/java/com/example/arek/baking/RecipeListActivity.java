package com.example.arek.baking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

public class RecipeListActivity extends AppCompatActivity {
    @Inject
    RecipeRepository mRecipeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BakingApp)getApplication()).getRepositoryComponent().inject(this);
        setContentView(R.layout.activity_recipe_list);


        mRecipeRepository.getRecipes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDisposableObserver());

    }

    DisposableObserver<List<Recipe>> getDisposableObserver(){
        return new DisposableObserver<List<Recipe>>() {
            @Override
            public void onNext(List<Recipe> recipeList) {
                Log.e("***",recipeList.toString());
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
