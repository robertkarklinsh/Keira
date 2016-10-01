package com.gattaca.team.ui.model.impl;

import android.util.Pair;

import com.gattaca.team.annotation.GraphPeriod;
import com.gattaca.team.ui.model.IContainerModel;
import com.gattaca.team.ui.view.TimeStump;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BpmModel implements IContainerModel {
    public final static int pointsInRealTimeMode = 180;

    private final List<Pair<Float, Long>> data = new ArrayList<>();
    private final List<BpmColorRegion> red = new ArrayList<>();
    private final List<BpmColorRegion> green = new ArrayList<>();
    private final boolean isRealTime;
    private
    @GraphPeriod
    long period = GraphPeriod.period_5min;

    public BpmModel(boolean isRealTime) {
        this.isRealTime = isRealTime;
    }

    private static void fill(List<BpmColorRegion> src) {
        final List<BpmColorRegion> color = new ArrayList<>();
        for (int i = 0; i < src.size() - 1; i++) {
            color.add(src.get(i));
            color.add(BpmColorRegion.merge(src.get(i), src.get(i + 1)));
        }
        color.add(src.get(src.size() - 1));
        src.clear();
        src.addAll(color);
    }

    public int getIntValueByPosition(int position) {
        return data.get(position >= data.size() ? data.size() - 1 : position).first.intValue();
    }

    public String getStringValueByPosition(int position) {
        return "" + getIntValueByPosition(position);
    }

    public int getPointsSize() {
        return isRealTime() ? pointsInRealTimeMode : getData().size();
    }

    public double getAngle(long time) {
        return (double) (time - data.get(0).second) * 360 / period;
    }

    public long getPeriod() {
        return this.period;
    }

    public void setPeriod(@GraphPeriod long period) {
        this.period = period;
    }

    public void addPoint(float value, long timestump) {
        data.add(new Pair<>(value, timestump));
    }

    public void addGreenPoint(float top, float bottom, long timestump) {
        green.add(new BpmColorRegion(top, bottom, timestump));
    }

    public void addRedPoint(float top, float bottom, long timestump) {
        red.add(new BpmColorRegion(top, bottom, timestump));
    }

    public ArrayList<String> formatTimes(final int period) {
        final ArrayList<String> list = new ArrayList<>();
        final long start = data.get(0).second;
        for (int i = 0; i < period; i++) {
            list.add(TimeStump.convert(start + (i) * this.period / period, "HH:mm:ss"));
        }
        return list;
    }

    public List<BpmColorRegion> getGreenData() {
        return green;
    }

    public List<BpmColorRegion> getRedData() {
        return red;
    }

    public boolean isRealTime() {
        return isRealTime;
    }

    public BpmModel fillPoints() {
        if (green.size() < 30) {
            fill(green);
        }
        if (red.size() < 30) {
            fill(red);
        }
        return this;
    }

    public List<Pair<Float, Long>> getData() {
        return this.data;
    }

    public long getDiff(int pos) {
        return data.get(pos).second - data.get(0).second;
    }

    public static class BpmColorRegion {
        private final float top;
        private final float bottom;
        private final long time;

        BpmColorRegion(final float top, final float bottom, final long time) {
            this.top = top;
            this.bottom = bottom;
            this.time = time;
        }

        static BpmColorRegion merge(BpmColorRegion one, BpmColorRegion two) {
            Random rnd = new Random();
            return new BpmColorRegion(
                    one.top + rnd.nextInt(10) * Math.abs(two.top - one.top) / 2,
                    one.bottom + rnd.nextInt(10) * Math.abs(two.bottom - one.bottom) / 2,
                    one.time + Math.abs(two.time - one.time) / 2);
        }

        public float getTop() {
            return top;
        }

        public float getBottom() {
            return bottom;
        }

        public long getTime() {
            return time;
        }
    }
}
