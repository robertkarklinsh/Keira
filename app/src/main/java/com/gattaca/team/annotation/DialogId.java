package com.gattaca.team.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({DialogId.SessionActions,
        DialogId.GenerationData,
        DialogId.StartFirstTick})
@Retention(RetentionPolicy.SOURCE)
public @interface DialogId {
    int SessionActions = 0;
    int GenerationData = SessionActions + 1;
    int StartFirstTick = GenerationData + 1;
}
