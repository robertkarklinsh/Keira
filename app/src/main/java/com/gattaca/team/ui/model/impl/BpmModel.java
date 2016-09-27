package com.gattaca.team.ui.model.impl;

import android.util.Log;
import android.util.Pair;

import com.gattaca.team.ui.model.IContainerModel;
import com.gattaca.team.ui.view.TimeStump;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BpmModel implements IContainerModel {
    private final List<Pair<Float, Long>> data = new ArrayList<>();
    private final List<BpmColorRegion> red = new ArrayList<>();
    private final List<BpmColorRegion> green = new ArrayList<>();
    private final Random rnd = new Random();

    public int getIntValueByPosition(int position) {
        return data.get(position).first.intValue();
    }

    public String getStringValueByPosition(int position) {
        return "" + getIntValueByPosition(position);
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

    public void print() {
        Log.e(getClass().getSimpleName(), "" + data.size());
    }

    public ArrayList<String> formatTimes(final int period) {
        final ArrayList<String> list = new ArrayList<>();
        final long start = data.get(0).second, allTime = data.get(data.size() - 1).second - start;
        for (int i = 0; i < period; i++) {
            list.add(TimeStump.convert(start + (i) * allTime / period, "HH:mm"));
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

    public static class BpmColorRegion {
        private final float top;
        private final float bottom;
        private final long time;

        public BpmColorRegion(final float top, final float bottom, final long time) {
            this.top = top;
            this.bottom = bottom;
            this.time = time;
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
