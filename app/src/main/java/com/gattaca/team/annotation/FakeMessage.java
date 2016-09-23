package com.gattaca.team.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({FakeMessage.Start, FakeMessage.Finish})
@Retention(RetentionPolicy.SOURCE)
public @interface FakeMessage {
    int Start = 0;
    int Finish = Start + 1;
}
