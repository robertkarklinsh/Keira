package com.gattaca.team.ui.model.impl;

import android.util.Log;
import android.util.Pair;

import com.gattaca.team.ui.model.IContainerModel;
import com.gattaca.team.ui.view.TimeStump;

import java.util.ArrayList;
import java.util.List;

public final class BpmModel implements IContainerModel {
    private final List<Pair<Float, Long>> data = new ArrayList<>();

    public void addPoint(float value, long timestump) {
        data.add(new Pair<>(value, timestump));
    }

    public void print() {
        Log.e(getClass().getSimpleName(), "" + data.size());
    }

    public ArrayList<String> formatTimes(final int period, int shift) {
        final ArrayList<String> list = new ArrayList<>();
        final long start = data.get(0).second, allTime = data.get(data.size() - 1).second - start;
        for (int i = 0; i < period; i++) {
            list.add(TimeStump.convert(start + (i - shift) * allTime / period, "HH:mm"));
        }
        return list;
    }
}
