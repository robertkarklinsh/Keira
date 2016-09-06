package com.gattaca.team.keira.data.datasource.realm;

import com.gattaca.team.keira.data.datamanager.DatabaseHelper;
import com.gattaca.team.keira.data.model.MonitorObservation;


import java.util.Date;
import java.util.List;

import io.realm.Realm;
import rx.Observable;

/**
 * Created by Robert on 14.08.2016.
 */
public class RealmHelper implements DatabaseHelper {


    private Realm realm;


    public RealmHelper(Realm realm) {
        this.realm = realm;
    }


    @Override
    public Observable<MonitorObservation> loadObservation(int id) {
        return realm.where(MonitorObservation.class).equalTo("id", id).findFirstAsync().asObservable()
                .filter(observation -> observation.isLoaded())
                .map(observation -> (MonitorObservation) observation);

    }


    @Override
    public Observable<List<MonitorObservation>> loadObservations(Date from, Date to) {

        return realm.where(MonitorObservation.class)
                .between("startDateTime", from, to)
                .or()
                .between("endDateTime", from, to)
                .findAllAsync().asObservable()
                .filter(observations -> observations.isLoaded())
                .flatMap(observations -> Observable.from(observations))
                .toList();
    }

    @Override
    public void saveObservationToDb(MonitorObservation monitorObservation) {

    }

}
