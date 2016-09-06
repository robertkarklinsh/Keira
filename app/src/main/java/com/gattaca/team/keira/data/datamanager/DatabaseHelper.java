package com.gattaca.team.keira.data.datamanager;

import com.gattaca.team.keira.data.model.MonitorObservation;


import java.util.Date;
import java.util.List;

import rx.Observable;
/**
 * Created by Robert on 14.08.2016.
 */
public interface DatabaseHelper {

    Observable<MonitorObservation> loadObservation(int id);

    Observable<List<MonitorObservation>> loadObservations(Date from, Date to);

    void saveObservationToDb(MonitorObservation monitorObservation);

}
