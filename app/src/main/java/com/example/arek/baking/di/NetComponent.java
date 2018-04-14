package com.example.arek.baking.di;

import com.example.arek.baking.RecipeListActivity;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Arkadiusz Wilczek on 14.04.18.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(RecipeListActivity activity);
}
