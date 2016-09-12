package com.gattaca.bitalinoecgchart.tracker.model;

import java.util.List;

/**
 * Created by Artem on 12.09.2016.
 */
public class Task implements Completable {
    boolean completed = false;
    @Override
    public boolean isCompleted() {
        return completed;
    }
    private List<TaskAction> actions;


    private static class TaskAction {
        private boolean completed;

    }
}
