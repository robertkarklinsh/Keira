package com.gattaca.bitalinoecgchart.tracker.model;

/**
 * Created by Artem on 12.09.2016.
 */
public class Measurement implements Completable {
    private boolean completed = false;

    @Override
    public boolean isCompleted() {
        return completed;
    }
}
