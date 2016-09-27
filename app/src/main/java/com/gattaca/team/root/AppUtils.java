package com.gattaca.team.root;

import com.gattaca.team.annotation.GraphPeriod;

import java.util.List;

public final class AppUtils {

    public static float convertListToAvrValue(List<Float> values) {
        float value = 0;
        for (float a : values) {
            value += a;
        }
        value /= values.size();
        return value;
    }

    public static float getCollapseTimeForPeriod(@GraphPeriod long period) {
        return period / AppConst.pointsGraph;
    }
}
