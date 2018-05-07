package com.example.arek.baking.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.arek.baking.BakingApp;
import com.example.arek.baking.R;
import com.example.arek.baking.model.Recipe;
import com.example.arek.baking.repository.RecipeRepository;
import com.example.arek.baking.utils.Utils;
import com.example.arek.baking.widget.BakingWidget;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;


public class BakingService extends IntentService {
    @Inject
    RecipeRepository mRepository;

    private static final String ACTION_UPDATE_WIDGETS = "com.example.arek.baking.service.action.update_widgets";

    public BakingService() {
        super("BakingService");
    }

    public static void startUpdateWidgets(Context context) {
        Intent intent = new Intent(context, BakingService.class);
        intent.setAction(ACTION_UPDATE_WIDGETS);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O ) {
            context.startService(intent);
        } else {
            context.startForegroundService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Update baking widget")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ((BakingApp)getApplication()).getRepositoryComponent().inject(this);
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGETS.equals(action)) {
                handleUpdateWidgets();
            }
        }
    }

    private void handleUpdateWidgets() {
        final Recipe recipe;
        DisposableObserver disposable = getObserver();
        mRepository.getRecipe(Utils.getRecipeForWidget(getApplicationContext()))
                .subscribe(disposable);
      //  disposable.dispose();

    }

    DisposableObserver<Recipe> getObserver() {
        return new DisposableObserver<Recipe>() {
            @Override
            public void onNext(Recipe recipe) {
                Timber.d("update widget");
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(BakingService.this);
                int[] appWidgetIds = appWidgetManager
                        .getAppWidgetIds(new ComponentName(getApplicationContext(), BakingWidget.class));

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list_view);
                for (int appWidgetId : appWidgetIds) {
                    BakingWidget.updateAppWidget(BakingService.this, appWidgetManager, appWidgetId, recipe.getName());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }
}
