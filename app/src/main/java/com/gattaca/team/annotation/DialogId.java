package com.gattaca.team.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({DialogId.SessionActions})
@Retention(RetentionPolicy.SOURCE)
public @interface DialogId {
    int SessionActions = 0;
}
