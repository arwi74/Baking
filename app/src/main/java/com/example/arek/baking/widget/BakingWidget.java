package com.example.arek.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.arek.baking.R;
import com.example.arek.baking.recipeDetails.RecipeDetailActivity;
import com.example.arek.baking.service.BakingService;
import com.example.arek.baking.utils.Utils;

import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        views.setTextViewText(R.id.appwidget_text, recipeName);

        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        appIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, Utils.getRecipeForWidget(context));
        PendingIntent appPendingIntent = PendingIntent.getActivity(
                context,
                0,
                appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        views.setOnClickPendingIntent(R.id.widget, appPendingIntent);
      //  views.setOnClickPendingIntent(R.id.appwidget_list_view, appPendingIntent);
        Timber.d("update widget");
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.appwidget_list_view, intent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        BakingService.startUpdateWidgets(context);
        Timber.d("onUpdate widget");
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.appwidget_list_view);
//        for (int appWidgetId : appWidgetIds) {
//            BakingWidget.updateAppWidget(context, appWidgetManager, appWidgetId, "test");
//        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

