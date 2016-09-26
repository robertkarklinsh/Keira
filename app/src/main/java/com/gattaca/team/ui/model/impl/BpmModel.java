package com.gattaca.team.ui.model.impl;

import android.util.Log;
import android.util.Pair;

import com.gattaca.team.ui.model.IContainerModel;

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
}
