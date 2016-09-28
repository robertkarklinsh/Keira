package com.gattaca.team.db.sensor.optimizing;


import android.util.Pair;

import com.gattaca.team.annotation.GraphPeriod;
import com.gattaca.team.root.AppUtils;

import java.util.ArrayList;

class BpmOptimizing {
    private final float collapseTime;
    private final ArrayList<Pair<Float, Long>> data = new ArrayList<>();
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
            collapsePoints();
            b = true;
        }
        data.add(new Pair<>(value, globalTimeStump));
        return b;
    }

    void collapsePoints() {
        float value = 0;
        for (Pair<Float, Long> a : data) {
            value += a.first;
        }
        value /= data.size();
        instance.setValue(value);
        instance.setTime(data.get(0).second);
        data.clear();
    }

    interface IBpmOptimizing {
        void setValue(float value);

        void setTime(long time);

        boolean addPoint(long globalTimeStump, double time, float value);

        void collapsePoints();
    }
}
