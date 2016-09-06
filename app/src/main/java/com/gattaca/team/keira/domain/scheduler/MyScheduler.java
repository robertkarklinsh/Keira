package com.gattaca.team.keira.domain.scheduler;

import rx.Scheduler;

/**
 * Created by Robert on 06.09.2016.
 */
public class MyScheduler extends Scheduler {
    @Override
    public Worker createWorker() {
        return null;
    }
}
