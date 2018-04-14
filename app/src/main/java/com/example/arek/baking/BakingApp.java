package com.example.arek.baking;

import android.app.Application;

import com.example.arek.baking.di.AppModule;

import com.example.arek.baking.di.DaggerRepositoryComponent;
import com.example.arek.baking.di.RecipeRepositoryModule;
import com.example.arek.baking.di.RepositoryComponent;
import com.example.arek.baking.di.NetModule;

/**
 * Created by Arkadiusz Wilczek on 14.04.18.
 */

public class BakingApp extends Application {
    private RepositoryComponent mRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mRepositoryComponent = DaggerRepositoryComponent.builder()
        //DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .recipeRepositoryModule(new RecipeRepositoryModule())
                .build();
    }

    public RepositoryComponent getRepositoryComponent() { return mRepositoryComponent; };
}
