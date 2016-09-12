package com.gattaca.team.keira.domain;

import com.gattaca.team.keira.data.model.User;
import com.gattaca.team.keira.data.model.Observation;

import java.util.List;
import rx.Observable;

/**
 * Created by Robert on 14.08.2016.
 */
public interface Repository {
    Observable<User> getUser();
    Observable<List<Observation>> getObservations();



}
