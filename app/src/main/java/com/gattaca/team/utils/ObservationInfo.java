package com.gattaca.team.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Robert on 16.08.2016.
 */
public abstract class ObservationInfo {

    public static final int TYPE_ECG_OBSERVATION = 1;

    @IntDef({
            TYPE_ECG_OBSERVATION


    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ObservationType {}


}
