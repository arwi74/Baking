package com.example.arek.baking.test;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.annotations.Nullable;

/**
 * Created by Arkadiusz Wilczek on 03.05.18.
 */

public class SimpleIdlingResource implements IdlingResource {
    @Nullable private volatile ResourceCallback mCallback;
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        mIsIdleNow.set(isIdleNow);
        if ( isIdleNow && mCallback != null ) {
            mCallback.onTransitionToIdle();
        }
    }
}
