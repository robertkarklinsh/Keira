package com.gattaca.bitalinoecgchart.tracker.model;

import io.realm.RealmObject;

/**
 * Created by Artem on 12.09.2016.
 */
public class Measurement extends RealmObject {
    private boolean completed = false;
    private String name ;
    private String duration;
    private String units;

    public boolean isCompleted() {
        return completed;
    }
}
