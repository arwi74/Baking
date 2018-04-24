package com.example.arek.baking.utils;

import com.example.arek.baking.model.Step;

import java.util.List;

/**
 * Created by Arkadiusz Wilczek on 24.04.18.
 */

public class Utils {

    public static int findStepIndex(List<Step> steps, long stepId) {
        for(int i=0; i<steps.size(); i++){
            if ( steps.get(i).getId() == stepId ) return i;
        }
        return -1;
    }
}
