package com.example.arek.baking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.arek.baking.api.BakingApi;
import com.example.arek.baking.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BakingApi.BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        BakingApi bakingApi = retrofit.create(BakingApi.class);

        bakingApi.getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDisposableObserver());

    }

    DisposableObserver<List<Recipe>> getDisposableObserver(){
        return new DisposableObserver<List<Recipe>>() {
            @Override
            public void onNext(List<Recipe> recipeList) {
                Log.e("***",recipeList.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.e("***", e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
