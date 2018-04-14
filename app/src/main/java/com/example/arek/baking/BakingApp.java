package com.example.arek.baking;

import android.app.Application;

import com.example.arek.baking.di.AppModule;
import com.example.arek.baking.di.DaggerNetComponent;
import com.example.arek.baking.di.NetComponent;
import com.example.arek.baking.di.NetModule;

/**
 * Created by Arkadiusz Wilczek on 14.04.18.
 */

public class BakingApp extends Application {
    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();
    }

    public NetComponent getNetComponent() { return mNetComponent; };
}
