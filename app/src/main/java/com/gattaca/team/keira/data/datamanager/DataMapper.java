package com.gattaca.team.keira.data.datamanager;

import com.gattaca.team.keira.data.model.Observation;

import java.util.List;

import rx.Observable;
/**
 * Created by Robert on 14.08.2016.
 */
public interface DataMapper {

    Observable<List<Observation>> getObservations();

}
