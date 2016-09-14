package com.gattaca.team.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gattaca.team.R;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.container.MenuItem;
import com.gattaca.team.ui.container.impl.DataBankContainer;
import com.gattaca.team.ui.container.impl.MonitorContainer;
import com.gattaca.team.ui.container.impl.NotificationCenterContainer;
import com.gattaca.team.ui.container.impl.TrackerContainer;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.otto.Subscribe;

public final class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {
    private IContainer trackerContainer, notificationCenterContainer, monitorContainer, dataBankContainer, currentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        trackerContainer = new TrackerContainer(this);
        notificationCenterContainer = new NotificationCenterContainer(this);
        monitorContainer = new MonitorContainer(this);
        dataBankContainer = new DataBankContainer(this);

        final DrawerBuilder drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withOnDrawerItemClickListener(this);
        for (MenuItem item : MenuItem.values()) {
            drawerBuilder.addDrawerItems(
                    new PrimaryDrawerItem()
                            .withName(item.getNameId())
                            .withIcon(item.getIconId()));
        }
        drawerBuilder.build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentContainer == null) {
            requestChangeToNewContainer(MenuItem.values()[0]);
        }
    }
/*
    @Subscribe
    public void tickSensorData(SensorData data) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.countTicks(); i++) {
            builder.append("\ntimestump=").append(data.getTimeStump(i));
            for (int j = 0; j < data.getChannels(); j++) {
                builder.append("#").append(j).append("=").append(data.getVoltByChannel(i, j)).append("   ");
            }
            builder.append("\n");
        }
        Log.i(getClass().getSimpleName(), builder.toString());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(builder.toString());
            }
        });
    }*/

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        requestChangeToNewContainer(MenuItem.values()[position]);
        return false;
    }

    @Subscribe
    /**
     * For open new container from any ui place
     * */
    public void requestChangeToNewContainer(final MenuItem item) {
        if (currentContainer == null) {
            //TODO: start state of application. No any animations needed
            notificationCenterContainer.getRootView().setVisibility(View.GONE);
            trackerContainer.getRootView().setVisibility(View.GONE);
            monitorContainer.getRootView().setVisibility(View.GONE);
            dataBankContainer.getRootView().setVisibility(View.GONE);
        } else {
            //TODO: animate change views
            currentContainer.getRootView().setVisibility(View.GONE);
        }
        switch (item) {
            case Notification:
                currentContainer = notificationCenterContainer;
                break;
            case Tracker:
                currentContainer = trackerContainer;
                break;
            case Monitor:
                currentContainer = monitorContainer;
                break;
            case DataBank:
                currentContainer = dataBankContainer;
                break;
        }
        currentContainer.getRootView().setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle(item.getNameId());
        //TODO: stub
        currentContainer.reDraw(null);
        /*final Class requestedModel = currentContainer.getModelClass();
        if(requestedModel == NotificationCenterModel.class){
            //TODO: implements
        }
        else if(requestedModel == TrackerModel.class){
            //TODO: implements
        }
        else if(requestedModel == MonitorModel.class){
            //TODO: implements
        }
        else if(requestedModel == DataBankModel.class){
            //TODO: implements
        }
        else {
            //Error case
        }*/
    }
}
