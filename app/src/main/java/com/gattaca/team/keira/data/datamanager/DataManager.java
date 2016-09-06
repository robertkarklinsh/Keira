package com.gattaca.team.keira.data.datamanager;

import com.gattaca.team.keira.data.model.MonitorObservation;
import com.gattaca.team.keira.domain.Repository;

import com.gattaca.team.keira.utils.DateTimeUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Robert on 14.08.2016.
 */
public class DataManager implements Repository {

    private DatabaseHelper dataMapper;

    @Inject
    public DataManager(DatabaseHelper dataMapper) {
        this.dataMapper = dataMapper;
    }


    @Override
    public Observable<MonitorObservation> loadObservation(int id) {
        return dataMapper.loadObservation(id);
    }


    @Override
    public Observable<List<MonitorObservation>> loadTodayObservations() {
        return dataMapper.loadObservations(DateTimeUtils.startOfToday(),DateTimeUtils.endOfToday());

    }

    @Override
    public void saveObservationToDb(MonitorObservation observation) {
    }
}
