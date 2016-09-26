package com.gattaca.bitalinoecgchart.tracker.db;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * Created by Artem on 24.09.2016.
 */

@RealmClass
public class TaskAction implements RealmModel {
    private boolean completed;

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

