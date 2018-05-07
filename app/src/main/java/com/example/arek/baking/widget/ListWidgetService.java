package com.example.arek.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.arek.baking.BakingApp;
import com.example.arek.baking.R;
import com.example.arek.baking.di.AppModule;
import com.example.arek.baking.di.DaggerRepositoryComponent;
import com.example.arek.baking.di.NetModule;
import com.example.arek.baking.di.RecipeRepositoryModule;
import com.example.arek.baking.model.Ingredient;
import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.repository.RecipeRepository;
import com.example.arek.baking.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

/**
 * Created by Arkadiusz Wilczek on 25.04.18.
 */

public class ListWidgetService extends RemoteViewsService {
    @Inject
    RecipeRepository mRepository;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        if (mRepository == null) {
            ((BakingApp) getApplication()).getRepositoryComponent().inject(this);
        }
        return new ListRemoteViewsFactory(this.getApplicationContext(),mRepository);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    private RecipeRepository mRepository;
    private List<Ingredient> mIngredients=new ArrayList<>();

    public ListRemoteViewsFactory(Context applicationContext, RecipeRepository repository) {
        mContext = applicationContext;
        mRepository = repository;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        long recipeId = Utils.getRecipeForWidget(mContext);
        mIngredients.clear();
        mRepository.getRecipe(recipeId)
        .subscribe(recipe -> mIngredients.addAll(recipe.getIngredients()));
    }

    DisposableObserver<Recipe> getDisposableObserver(){
        return new DisposableObserver<Recipe>() {
            @Override
            public void onNext(Recipe recipe) {
                mIngredients = recipe.getIngredients();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients == null ? 0 : mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mIngredients == null || mIngredients.size() == 0) return null;
        Ingredient ingredient = mIngredients.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String name = ingredient.getIngredient();
        String quantity = decimalFormat.format(ingredient.getQuantity());
        String measure = ingredient.getMeasure();
        Timber.d("get view at position: " + position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget_item);
        views.setTextViewText(R.id.widget_ingredient_name,name);
        views.setTextViewText(R.id.widget_ingredient_quantity,quantity);
        views.setTextViewText(R.id.widget_ingredient_measure,measure);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
