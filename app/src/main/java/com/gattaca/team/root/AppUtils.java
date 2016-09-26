package com.gattaca.team.root;

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
}
