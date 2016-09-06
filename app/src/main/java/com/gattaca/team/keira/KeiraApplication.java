package com.gattaca.team.keira;


import android.app.Application;

import com.facebook.stetho.Stetho;
import com.gattaca.team.keira.ui.activity.MainActivity;
import com.gattaca.team.keira.data.DataLayerFactory;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

/**
 * Created by Robert on 17.08.2016.
 */
public class KeiraApplication extends Application {

    private static AppComponent component;


    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        AndroidThreeTen.init(this);
        component = DaggerAppComponent.builder().dataLayerFactory(new DataLayerFactory(this)).build();
    }

    public static void inject(MainActivity target){
        component.inject(target);
    }
}
