package com.example.arek.baking.di;

import com.example.arek.baking.recipeDetails.RecipeDetailActivityFragment;
import com.example.arek.baking.recipeDetails.recipeStep.RecipeStepActivity;
import com.example.arek.baking.recipeDetails.recipeStep.RecipeStepActivityFragment;
import com.example.arek.baking.recipeList.RecipeListActivity;
import com.example.arek.baking.service.BakingService;
import com.example.arek.baking.widget.ListWidgetService;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Arkadiusz Wilczek on 14.04.18.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class, RecipeRepositoryModule.class})
public interface RepositoryComponent {
    void inject(RecipeListActivity activity);
    void inject(RecipeDetailActivityFragment fragment);
    void inject(RecipeStepActivityFragment fragment);
    void inject(RecipeStepActivity activity);
    void inject(ListWidgetService widget);
    void inject(BakingService service);
}
