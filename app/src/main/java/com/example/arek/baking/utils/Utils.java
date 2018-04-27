package com.example.arek.baking.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.arek.baking.R;
import com.example.arek.baking.model.Step;

import java.util.List;

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


}
