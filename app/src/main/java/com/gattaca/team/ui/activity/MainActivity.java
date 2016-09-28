package com.gattaca.team.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gattaca.team.R;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.ui.container.ActivityTransferData;
import com.gattaca.team.ui.container.ContainerTransferData;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.container.MainMenu;
import com.gattaca.team.ui.container.impl.DataBankContainer;
import com.gattaca.team.ui.container.impl.MonitorContainer;
import com.gattaca.team.ui.container.impl.NotificationCenterContainer;
import com.gattaca.team.ui.container.impl.SettingsContainer;
import com.gattaca.team.ui.container.impl.TrackerContainer;
import com.gattaca.team.ui.model.IContainerModel;
import com.gattaca.team.ui.model.impl.DataBankModel;
import com.gattaca.team.ui.model.impl.NotificationCenterModel;
import com.gattaca.team.ui.model.impl.TrackerModel;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.otto.Subscribe;

import io.realm.Realm;

public final class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {
    private IContainer
            trackerContainer,
            notificationCenterContainer,
            monitorContainer,
            dataBankContainer,
            settingsContainer,
            currentContainer;

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
        settingsContainer = new SettingsContainer(this);

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
        if (currentContainer != null) {
            currentContainer.changeCurrentVisibilityState(true);
        }
        IContainerModel model = item.getModelForSubContainer();
        switch (item.getMenuItemForOpen()) {
            case Notification:
                currentContainer = notificationCenterContainer;
                model = new NotificationCenterModel(RealmController.getAllEvents());
                break;
            case Tracker:
                currentContainer = trackerContainer;
                model = new TrackerModel(RealmController.getCurrentWeek());
                break;
            case Monitor:
                currentContainer = monitorContainer;
                break;
            case DataBank:
                currentContainer = dataBankContainer;
                model = new DataBankModel(RealmController.getAllSessions());
                break;
            case Settings:
                currentContainer = settingsContainer;
                break;
        }
        currentContainer.changeCurrentVisibilityState(false);
        getSupportActionBar().setTitle(item.getMenuItemForOpen().getNameId());
        //TODO: stub
        currentContainer.reDraw(model);
        invalidateOptionsMenu();
    }

    @Subscribe
    public void listenerForNewActivityRequest(ActivityTransferData request) {
        request.launchRequestedActivity(this);
    }

    @Override
    protected void onDestroy() {
        Realm.getDefaultInstance().close();
        super.onDestroy();
    }

    @Subscribe
    public void updateTrackerPercents(int[] percents) {

    }
}
