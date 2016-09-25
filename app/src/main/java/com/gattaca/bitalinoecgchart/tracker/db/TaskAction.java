package com.gattaca.bitalinoecgchart.tracker.db;

import io.realm.RealmObject;

/**
 * Created by Artem on 24.09.2016.
 */

public class TaskAction extends RealmObject {
    private boolean completed;

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

