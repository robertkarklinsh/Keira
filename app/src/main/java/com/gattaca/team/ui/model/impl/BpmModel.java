package com.gattaca.team.ui.model.impl;

import android.util.Log;
import android.util.Pair;

import com.gattaca.team.ui.model.IContainerModel;
import com.gattaca.team.ui.view.TimeStump;

import java.util.ArrayList;
import java.util.List;

public final class BpmModel implements IContainerModel {
    private final List<Pair<Float, Long>> data = new ArrayList<>();
    private final List<BpmGreenRegion> green = new ArrayList<>();

    public void addPoint(float value, long timestump) {
        data.add(new Pair<>(value, timestump));
    }

    public void addGreenPoint(float top, float bottom, long timestump) {
        green.add(new BpmGreenRegion(top, bottom, timestump));
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

    public List<BpmGreenRegion> getGreenData() {
        return green;
    }
    public List<Float> getData() {
        final List<Float> floats = new ArrayList<>();
        for (Pair<Float, Long> item : this.data) {
            floats.add(item.first);
        }
        return floats;
    }

    public static class BpmGreenRegion {
        private final float top;
        private final float bottom;
        private final long time;

        public BpmGreenRegion(final float top, final float bottom, final long time) {
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
