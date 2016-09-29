package com.gattaca.team.db.tracker;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Artem on 24.09.2016.
 */

@RealmClass
public class TaskAction implements RealmModel {

    @PrimaryKey
    private long primaryKey;
    public long getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(long primaryKey) {
        this.primaryKey = primaryKey;
    }
    private boolean completed;

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

