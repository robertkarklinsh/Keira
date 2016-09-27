package com.gattaca.team.db.sensor.optimizing;


import com.gattaca.team.annotation.GraphPeriod;
import com.gattaca.team.root.AppUtils;

import java.util.ArrayList;

class BpmOptimizing {
    private final float collapseTime;
    private final ArrayList<Float> data = new ArrayList<>();
    private final IBpmOptimizing instance;
    private double time = 0;

    BpmOptimizing(IBpmOptimizing me, @GraphPeriod long period) {
        this.collapseTime = AppUtils.getCollapseTimeForPeriod(period);
        this.instance = me;
    }

    boolean addPoint(long globalTimeStump, double time, float value) {
        boolean b = false;
        this.time += time;
        if (this.time > collapseTime) {
            this.time -= collapseTime;
            collapsePoints(globalTimeStump);
            b = true;
        }
        data.add(value);
        return b;
    }

    void collapsePoints(long globalTimeStump) {
        instance.setValue(AppUtils.convertListToAvrValue(data));
        instance.setTime(globalTimeStump);
        data.clear();
    }

    interface IBpmOptimizing {
        void setValue(float value);

        void setTime(long time);

        boolean addPoint(long globalTimeStump, double time, float value);

        void collapsePoints(long globalTimeStump);
    }
}
