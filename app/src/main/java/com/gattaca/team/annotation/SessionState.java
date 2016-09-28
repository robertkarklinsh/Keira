package com.gattaca.team.annotation;

import android.support.annotation.IntDef;

import com.gattaca.team.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({SessionState.Start, SessionState.Finish})
@Retention(RetentionPolicy.SOURCE)
public @interface SessionState {
    int Start = R.string.settings_main_session_start;
    int Finish = R.string.settings_main_session_stop;
}
