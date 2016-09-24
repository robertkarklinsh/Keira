package com.gattaca.bitalinoecgchart.tracker.model;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by Artem on 12.09.2016.
 */
public class Task extends RealmObject {
    boolean completed = false;

    public boolean isCompleted() {
        return completed;
    }
    private List<TaskAction> actions;


    private static class TaskAction extends RealmObject {
        private boolean completed;

    }
}
