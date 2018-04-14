package com.example.arek.baking.api;

import com.example.arek.baking.model.Recipe;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Arkadiusz Wilczek on 13.04.18.
 */

public interface BakingApi {
    String BASE_URL = "http://go.udacity.com/";

    @GET("android-baking-app-json")
    Observable<List<Recipe>> getRecipes();
}
