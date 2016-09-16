package com.gattaca.team.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gattaca.team.R;
import com.gattaca.team.service.SensorData;
import com.gattaca.team.ui.container.ContainerTransferData;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.container.MainMenu;
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
        for (MainMenu item : MainMenu.values()) {
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
            requestChangeToNewContainer(new ContainerTransferData(MainMenu.values()[0]));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentContainer.changeCurrentVisibilityState(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentContainer.changeCurrentVisibilityState(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(currentContainer.getMenuItemActions(), menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        currentContainer.onMenuItemSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        requestChangeToNewContainer(new ContainerTransferData(MainMenu.values()[position]));
        return false;
    }

    @Subscribe
    /**
     * For open new container from any ui place
     * */
    public void requestChangeToNewContainer(final ContainerTransferData item) {
        if (currentContainer == null) {
            //TODO: start state of application. No any animations needed
            notificationCenterContainer.getRootView().setVisibility(View.GONE);
            trackerContainer.changeCurrentVisibilityState(true);
            monitorContainer.changeCurrentVisibilityState(true);
            dataBankContainer.changeCurrentVisibilityState(true);
        } else {
            //TODO: animate change views
            currentContainer.changeCurrentVisibilityState(true);
        }
        switch (item.getMenuItemForOpen()) {
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
        currentContainer.changeCurrentVisibilityState(false);
        getSupportActionBar().setTitle(item.getMenuItemForOpen().getNameId());
        //TODO: stub
        currentContainer.reDraw(item.getModelForSubContainer());
        invalidateOptionsMenu();
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
    }
}
