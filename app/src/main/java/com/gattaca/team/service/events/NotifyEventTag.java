package com.gattaca.team.service.events;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({NotifyEventTag.Red, NotifyEventTag.Yellow, NotifyEventTag.Starving})
@Retention(RetentionPolicy.SOURCE)
public @interface NotifyEventTag {
    int Red = 0;
    int Yellow = 1;
    int Starving = 2;
}
