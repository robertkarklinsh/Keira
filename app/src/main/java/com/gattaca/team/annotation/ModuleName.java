package com.gattaca.team.annotation;

import android.support.annotation.IntDef;

import com.gattaca.team.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({ModuleName.Tracker,
        ModuleName.Monitor,
        ModuleName.DataBank})
@Retention(RetentionPolicy.SOURCE)
public @interface ModuleName {
    int Tracker = R.string.navigation_item_2;
    int Monitor = R.string.navigation_item_3;
    int DataBank = R.string.navigation_item_4;
}
