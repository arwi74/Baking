package com.example.arek.baking.di;

import com.example.arek.baking.api.BakingApi;
import com.example.arek.baking.repository.RecipeRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Arkadiusz Wilczek on 14.04.18.
 */

@Module
public class RecipeRepositoryModule {



    @Singleton
    @Provides
    RecipeRepository provideRecipeRepository(BakingApi bakingApi) {
        return new RecipeRepository(bakingApi);
    }
}
