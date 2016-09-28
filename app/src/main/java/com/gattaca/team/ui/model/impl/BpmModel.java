package com.gattaca.team.ui.model.impl;

import android.text.format.DateUtils;
import android.util.Pair;

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
    private final Random rnd = new Random();
    private final boolean isRealTime;

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
        return data.get(position).first.intValue();
    }

    public String getStringValueByPosition(int position) {
        return "" + getIntValueByPosition(position);
    }

    public int getPointsSize() {
        return isRealTime() ? pointsInRealTimeMode : getData().size();
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
        final long
                start = data.get(0).second,
                allTime = compileEndTime();
        for (int i = 0; i < period; i++) {
            list.add(TimeStump.convert(start + (i) * allTime / period, "HH:mm:ss"));
        }
        return list;
    }

    public List<BpmColorRegion> getGreenData() {
        return green;
    }

    public List<BpmColorRegion> getRedData() {
        return red;
    }

    public List<Float> getData() {
        final List<Float> floats = new ArrayList<>();
        for (Pair<Float, Long> item : this.data) {
            floats.add(item.first);
        }
        return floats;
    }

    public boolean isRealTime() {
        return isRealTime;
    }

    public long compileEndTime() {
        if (isRealTime()) {
            return 5 * DateUtils.MINUTE_IN_MILLIS;
        } else {
            return data.get(data.size() - 1).second - data.get(0).second;
        }
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
