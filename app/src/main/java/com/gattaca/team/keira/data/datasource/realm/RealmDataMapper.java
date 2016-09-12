package com.gattaca.team.keira.data.datasource.realm;

import com.gattaca.team.keira.data.datamanager.DataMapper;
import com.gattaca.team.keira.data.model.Observation;

import java.util.List;

import rx.Observable;

/**
 * Created by Robert on 14.08.2016.
 */
public class RealmDataMapper implements DataMapper {
    @Override
    public Observable<List<Observation>> getObservations() {
        return null;
    }
}
