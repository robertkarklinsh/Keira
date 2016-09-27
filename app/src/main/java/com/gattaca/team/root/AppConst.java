package com.gattaca.team.root;

import android.text.format.DateUtils;

public final class AppConst {
    public static final int minBPM = 20;
    public static final int maxBPM = 140;
    public static final long graphPeriod5Min = 5 * DateUtils.MINUTE_IN_MILLIS;
    public static final long graphPeriod15Min = 15 * DateUtils.MINUTE_IN_MILLIS;
    public static final long graphPeriod30Min = 30 * DateUtils.MINUTE_IN_MILLIS;
    public static final long graphPeriod1Hour = DateUtils.HOUR_IN_MILLIS;
    private static final int angleGraph = 2;
    static final int pointsGraph = 360 / angleGraph;

}
