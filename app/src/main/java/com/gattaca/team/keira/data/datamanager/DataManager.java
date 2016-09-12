package com.gattaca.team.keira.data.datamanager;

import com.gattaca.team.keira.data.model.Observation;
import com.gattaca.team.keira.data.model.User;
import com.gattaca.team.keira.domain.Repository;

import java.util.List;

import rx.Observable;

/**
 * Created by Robert on 14.08.2016.
 */
public class DataManager implements Repository {

    @Override
    public Observable<User> getUser() {
        return null;
    }

    @Override
    public Observable<List<Observation>> getObservations() {
        return null;
    }
}
