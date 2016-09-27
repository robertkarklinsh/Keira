package com.gattaca.team.annotation;

import android.support.annotation.IntDef;

import com.gattaca.team.root.AppConst;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({GraphPeriod.period_5min,
        GraphPeriod.period_15min,
        GraphPeriod.period_30min,
        GraphPeriod.period_1hour})
@Retention(RetentionPolicy.SOURCE)
public @interface GraphPeriod {
    long period_5min = AppConst.graphPeriod5Min;
    long period_15min = AppConst.graphPeriod15Min;
    long period_30min = AppConst.graphPeriod30Min;
    long period_1hour = AppConst.graphPeriod1Hour;
}