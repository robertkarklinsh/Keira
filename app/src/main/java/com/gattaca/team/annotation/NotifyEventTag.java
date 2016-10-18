package com.gattaca.team.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({NotifyEventTag.Red, NotifyEventTag.Yellow, NotifyEventTag.Starving, NotifyEventTag.Green})
@Retention(RetentionPolicy.SOURCE)
public @interface NotifyEventTag {
    int Red = 0;
    int Yellow = 1;
    int Starving = 2;
    int Green = 3;
}
