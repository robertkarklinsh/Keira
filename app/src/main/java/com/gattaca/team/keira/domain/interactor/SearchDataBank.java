package com.gattaca.team.keira.domain.interactor;

import com.gattaca.team.keira.data.datamanager.DataManager;


import rx.Observable;
import rx.Scheduler;

/**
 * Created by Robert on 18.08.2016.
 */
public class SearchDataBank extends UseCase {

    private DataManager dataManager;

    public SearchDataBank(DataManager dataManager, Scheduler executionScheduler,
                          Scheduler postExecutionScheduler) {
        super(executionScheduler, postExecutionScheduler);
        this.dataManager = dataManager;
    }

    @Override
    protected Observable buildUseCaseObservable() {

        return dataManager.loadTodayObservations();
    }
}
