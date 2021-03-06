
package com.example.arek.baking.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Ingredient {

    @SerializedName("ingredient")
    private String mIngredient;
    @SerializedName("measure")
    private String mMeasure;
    @SerializedName("quantity")
    private double mQuantity;

    public String getIngredient() {
        return mIngredient;
    }

    public void setIngredient(String ingredient) {
        mIngredient = ingredient;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public void setMeasure(String measure) {
        mMeasure = measure;
    }

    public double getQuantity() {
        return mQuantity;
    }

    public void setQuantity(double quantity) {
        mQuantity = quantity;
    }

}
