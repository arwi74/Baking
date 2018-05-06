package com.example.arek.baking.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.arek.baking.R;
import com.example.arek.baking.model.Step;

import java.util.List;

import timber.log.Timber;

/**
 * Created by Arkadiusz Wilczek on 24.04.18.
 */

public class Utils {
    private static final String PREF_RECIPE_FOR_WIDGET = "recipe_for_widget";

    public static int findStepIndex(List<Step> steps, long stepId) {
        for(int i=0; i<steps.size(); i++){
            if ( steps.get(i).getId() == stepId ) return i;
        }
        return -1;
    }

    public static long getRecipeForWidget(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong(PREF_RECIPE_FOR_WIDGET, 1);
    }

    public static void setRecipeForWidget(Context context, long id) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putLong(PREF_RECIPE_FOR_WIDGET, id);
        edit.commit();
    }

    public static boolean isTwoPane(Context context) {
        return context.getResources().getBoolean(R.bool.two_pane);
    }

    public static void hideStatusBar(AppCompatActivity activity) {
        Timber.d("hide toolbar");
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN|
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }



}
