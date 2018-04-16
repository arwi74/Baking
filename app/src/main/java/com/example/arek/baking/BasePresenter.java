package com.example.arek.baking;

/**
 * Created by Arkadiusz Wilczek on 16.04.18.
 */

public interface BasePresenter<T> {

    void takeView(T view);

    void dropView();

}
