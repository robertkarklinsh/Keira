package com.gattaca.bitalinoecgchart.tracker.model;

/**
 * Created by Artem on 12.09.2016.
 */
public class Measurement implements Completable {
    private boolean completed = false;
    private String name ;
    private String duration;
    private String units;


    @Override
    public boolean isCompleted() {
        return completed;
    }
}
