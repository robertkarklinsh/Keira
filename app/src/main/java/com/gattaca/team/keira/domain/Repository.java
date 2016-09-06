package com.gattaca.team.keira.domain;

import com.gattaca.team.keira.data.model.MonitorObservation;

import java.util.List;

import rx.Observable;

/**
 * Created by Robert on 14.08.2016.
 */
public interface Repository {
    Observable<MonitorObservation> loadObservation(int id);
    Observable<List<MonitorObservation>> loadTodayObservations();

    void saveObservationToDb(MonitorObservation observation);

}
