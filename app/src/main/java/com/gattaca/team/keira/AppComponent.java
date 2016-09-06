package com.gattaca.team.keira;

import com.gattaca.team.keira.ui.activity.MainActivity;
import com.gattaca.team.keira.data.DataLayerFactory;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Robert on 17.08.2016.
 */
@Singleton
@Component(modules = DataLayerFactory.class)
public interface AppComponent {
    void inject(MainActivity activity);
}
