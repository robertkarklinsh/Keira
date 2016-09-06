package com.gattaca.team.keira.data;

import java.util.Date;

import android.app.Application;

import com.gattaca.team.keira.data.datamanager.DatabaseHelper;
import com.gattaca.team.keira.data.datasource.realm.RealmHelper;
import com.gattaca.team.keira.data.model.MonitorObservation;
import com.gattaca.team.keira.utils.ObservationInfo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Robert on 17.08.2016.
 */

@Module
public class DataLayerFactory {

    private Application app;

    public DataLayerFactory(Application app) {
        this.app = app;
    }

    @Provides
    DatabaseHelper provideRealmDataMapper(Realm realm) {
        return new RealmHelper(realm);
    }

    @Provides
    Realm provideRealm(RealmConfiguration config) {
        return Realm.getInstance(config);
    }

    @Singleton
    @Provides
    RealmConfiguration provideRealmConfig() {
        return new RealmConfiguration.Builder(app.getApplicationContext())
                .name("keira_debug.realm")
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        MonitorObservation initialObservation = realm.createObject(MonitorObservation.class);
                        initialObservation.setStartDateTime(new Date(System.currentTimeMillis()));
                        initialObservation.setEndDateTime(new Date(System.currentTimeMillis() + 6000));
                        initialObservation.setId(1);
                        initialObservation.setObservationType(ObservationInfo.TYPE_ECG_OBSERVATION);
                    }
                })
                .build();
    }


}
